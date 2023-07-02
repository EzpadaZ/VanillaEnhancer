package dev.ezpadaz.vanillaenhancer.Utils.Overseer;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Database;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Model.Config.ConfigurationModel;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Model.Generic.TPSEventModel;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import dev.ezpadaz.vanillaenhancer.Utils.SettingsHelper;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Server;

public class Overseer {
    private static Overseer instance;
    private double MIN_TPS;
    private double WARNING_TPS;
    private int INTERVAL;

    private Integer INTERNAL_SCHEDULER_TASK_ID;


    private Overseer() {
        // nada.
        ConfigurationModel settings = SettingsHelper.getInstance().getSettings();
        MIN_TPS = settings.getMin_tps();
        WARNING_TPS = settings.getWarning_tps();
        INTERVAL = settings.getOverseer_interval();
    }

    public void init() {
        if (INTERNAL_SCHEDULER_TASK_ID == null) {
            INTERNAL_SCHEDULER_TASK_ID = GeneralUtils.scheduleRepeatingTask(() -> {
                Server server = Bukkit.getServer();
                double[] tps;
                try {
                    tps = server.getTPS();
                } catch (Error e) {
                    Bukkit.getScheduler().cancelTask(INTERNAL_SCHEDULER_TASK_ID);
                    MessageHelper.console("&cOverseer can work only in PaperMC Software.");
                    MessageHelper.console("&cIt has been disabled to avoid further bugs.");
                    return;
                }
                // TPS 0 1M, 1 5M
                double TPS1M = tps[0];
                double TPS5M = tps[1];

                if (TPS1M <= WARNING_TPS) {
                    // warn globally.
                    logEvent(TPS1M, false);
                    MessageHelper.global("&cALERTA: &6Los TPS estan cayendo por debajo del limite aceptable.");
                }

                if (TPS5M <= MIN_TPS) {
                    // ISSUE SERVER RESTART TO AVOID LAGGING FURTHER
                    panic(TPS5M);
                }
            }, 30, INTERVAL);
            MessageHelper.console("&6Overseer Status: &a[OK "+ INTERNAL_SCHEDULER_TASK_ID+"]");
        }
    }

    public void panic(double TPS) {
        logEvent(TPS, true);
        MessageHelper.global("&cALERTA: &6Los TPS han caido por debajo del limite, un reinicio ha sido programado en &c30 Segundos");
        GeneralUtils.scheduleTask(this::nuke, 30);
    }

    // this is an emergency nuke. god bless us all.
    private void nuke() {
        Server server = Bukkit.getServer();
        server.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
        server.dispatchCommand(Bukkit.getConsoleSender(), "restart");
    }

    private void logEvent(double TPS, boolean causedPanic) {
        MongoCollection collection = Database.getInstance().getCollection("Telemetry");
        TPSEventModel model = new TPSEventModel(TPS, causedPanic);
        Document filter = new Document("_id", model.getId());
        UpdateOptions options = new UpdateOptions().upsert(true);
        collection.updateOne(filter, model.getDocumentFormat(), options);
        MessageHelper.console("&cTPS Event Detected, Check your database.");
    }

    public void reloadOverseer() {
        ConfigurationModel settings = SettingsHelper.getInstance().getSettings();
        MIN_TPS = settings.getMin_tps();
        WARNING_TPS = settings.getWarning_tps();
        INTERVAL = settings.getOverseer_interval();
        MessageHelper.console("&6Overseer has been reloaded.");
    }

    public void stopOverseer() {
        if (INTERNAL_SCHEDULER_TASK_ID != null) {
            GeneralUtils.cancelScheduledTask(INTERNAL_SCHEDULER_TASK_ID);
            MessageHelper.console("&cOverseer has been killed.");
        }
    }

    public static Overseer getInstance() {
        if (instance == null) instance = new Overseer();
        return instance;
    }
}
