package dev.ezpadaz.vanillaenhancer.Utils.Watcher;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Database;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Model.Config.ConfigurationModel;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Model.Generic.TPSEventModel;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import dev.ezpadaz.vanillaenhancer.Utils.SettingsHelper;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class Watcher {
    private static Watcher instance;

    private static String watcherPrefix = "&6[&5Watcher&6] ";

    private static int UNLOADED_CHUNKS = 0;
    private static boolean ATTEMPTED_UNLOAD = false;
    private double MIN_TPS;
    private double WARNING_TPS;
    private int INTERVAL;

    public double getMIN_TPS() {
        return MIN_TPS;
    }

    public double getWARNING_TPS() {
        return WARNING_TPS;
    }

    public int getINTERVAL() {
        return INTERVAL;
    }

    private Integer INTERNAL_SCHEDULER_TASK_ID;
    private Integer INTERNAL_NUKE_TASK_ID;


    private Watcher() {
        // nada.
        ConfigurationModel settings = SettingsHelper.getInstance().getSettings();
        MIN_TPS = settings.getMin_tps();
        WARNING_TPS = settings.getWarning_tps();
        INTERVAL = settings.getWatcher_interval();
    }

    public void init() {
        if (INTERNAL_SCHEDULER_TASK_ID == null) {
            INTERNAL_SCHEDULER_TASK_ID = GeneralUtils.scheduleRepeatingTask(() -> {

                if (INTERNAL_NUKE_TASK_ID != null) return; // prevent further checking if a nuke is scheduled.

                Server server = Bukkit.getServer();
                double[] tps;
                try {
                    tps = server.getTPS();
                } catch (Error e) {
                    Bukkit.getScheduler().cancelTask(INTERNAL_SCHEDULER_TASK_ID);
                    MessageHelper.console("&cWatcher can work only in PaperMC Software.");
                    MessageHelper.console("&cIt has been disabled to avoid further bugs.");
                    return;
                }
                // TPS 0 1M, 1 5M
                double TPS1M = tps[0];
                double TPS5M = tps[1];

                if (TPS1M <= WARNING_TPS) {
                    MessageHelper.global("&6Estoy detectando latencia en el servidor.");
                }

                // 5 minute lag with 1 minute average still below threshold.
                if (TPS5M <= MIN_TPS && TPS1M <= WARNING_TPS) {
                    // Server has been lagging for 5 minutes, nuke and reset.
                    if (ATTEMPTED_UNLOAD) {
                        // we already tried the chunk unloading and failed. panic.
                        panic(TPS5M);
                    } else {
                        MessageHelper.sendMultipleGlobal("&cAviso Importante",
                                "OS",
                                List.of(new String[]{"&cHe detectado demasiada latencia en el server", "&eIntentare matar las chunks inactivas en &c5&e segundos."}));
                        GeneralUtils.scheduleTask(this::attemptChunkUnloading, 5);
                        ATTEMPTED_UNLOAD = true;
                    }
                }
            }, 30, INTERVAL);
            MessageHelper.console("&6Watcher Status: &a[OK " + INTERNAL_SCHEDULER_TASK_ID + "]");
        }
    }

    public String getStatus() {
        double[] tps = VanillaEnhancer.getInstance().getServer().getTPS();

        if (tps[1] <= MIN_TPS) {
            return "&cPANIC";
        }

        if (tps[0] <= WARNING_TPS) {
            return "&eUNSTABLE";
        }

        return "&aSTABLE";
    }

    public Integer getTaskID() {
        return INTERNAL_SCHEDULER_TASK_ID;
    }

    public void panic(double TPS) {
        if (INTERNAL_NUKE_TASK_ID != null) return;
        logEvent(GeneralUtils.formatDouble(TPS), true);
        MessageHelper.global("&cALERTA: &6Los TPS han caido por debajo del limite, un reinicio ha sido programado en &c30 Segundos");
        INTERNAL_NUKE_TASK_ID = GeneralUtils.scheduleTask(this::nuke, 30);
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

    public void reloadWatcher() {
        ConfigurationModel settings = SettingsHelper.getInstance().getSettings();
        MIN_TPS = settings.getMin_tps();
        WARNING_TPS = settings.getWarning_tps();
        INTERVAL = settings.getWatcher_interval();
        GeneralUtils.cancelScheduledTask(INTERNAL_SCHEDULER_TASK_ID);
        INTERNAL_SCHEDULER_TASK_ID = null;
        this.init();
        MessageHelper.console("&6Watcher has been restarted.");
    }

    public void stopWatcher() {
        if (INTERNAL_SCHEDULER_TASK_ID != null) {
            GeneralUtils.cancelScheduledTask(INTERNAL_SCHEDULER_TASK_ID);
            INTERNAL_SCHEDULER_TASK_ID = null;
            if (INTERNAL_NUKE_TASK_ID != null) {
                GeneralUtils.cancelScheduledTask(INTERNAL_NUKE_TASK_ID);
                INTERNAL_NUKE_TASK_ID = null;
            }
            MessageHelper.console("&cWatcher has been killed.");
        }
    }

    public void attemptChunkUnloading() {
        World world = VanillaEnhancer.getInstance().getServer().getWorlds().get(0);
        Chunk[] loadedChunks = world.getLoadedChunks();
        int ldc = loadedChunks.length;
        UNLOADED_CHUNKS = 0;
        for (Chunk chunk : loadedChunks) {
            if (!chunk.isLoaded()) {
                continue; // Chunk is already unloaded
            }
            if (chunk.getInhabitedTime() > 0) {
                continue; // Do not continue if the chunk has players.
            }
            int chunkX = chunk.getX();
            int chunkZ = chunk.getZ();
            int playerRadius = 12; // RADIUS IN CHUNKS, 10 IS THE SERVER DEFAULT SO WE CHECK +2.

            boolean shouldUnload = true;
            for (Player player : Bukkit.getOnlinePlayers()) {
                int playerChunkX = player.getLocation().getBlockX() >> 4;
                int playerChunkZ = player.getLocation().getBlockZ() >> 4;
                if (Math.abs(playerChunkX - chunkX) <= playerRadius && Math.abs(playerChunkZ - chunkZ) <= playerRadius) {
                    shouldUnload = false; // Player is within radius, don't unload
                    break;
                }
            }

            if (shouldUnload) {
                boolean result = chunk.unload(true);
                if (result) {
                    UNLOADED_CHUNKS++;
                }
            }
        }
        if (UNLOADED_CHUNKS > 0) {
            MessageHelper.global("&6He tratado de apagar &c" + UNLOADED_CHUNKS + " &6de&c " + ldc + " &6chunks para tratar de prevenir un kernel::panic", watcherPrefix);
        } else {
            MessageHelper.global("&cNo he podido apagar ningun chunk, nos vemos en el infierno.", watcherPrefix);
        }
    }

    public static Watcher getInstance() {
        if (instance == null) instance = new Watcher();
        return instance;
    }
}
