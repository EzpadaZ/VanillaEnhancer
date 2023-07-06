package dev.ezpadaz.vanillaenhancer.Commands.Admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import dev.ezpadaz.vanillaenhancer.Utils.SettingsHelper;
import dev.ezpadaz.vanillaenhancer.Utils.Watcher.Watcher;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("os")
@Subcommand("watcher")
public class WatcherCommander extends BaseCommand {
    private static Integer LAG_TASK_ID;

    @Subcommand("kill")
    public void osKill(Player jugador) {
        if (jugador.isOp() || jugador.getName().equalsIgnoreCase("Molthor226")) {
            Watcher.getInstance().stopWatcher();
            MessageHelper.send(jugador, "&6Se ha apagado el watcher.");
        } else {
            MessageHelper.send(jugador, "&cNalgas perro, no se quien eres.");
        }
    }

    @Subcommand("start")
    public void osStart(Player jugador) {
        if (jugador.isOp() || jugador.getName().equalsIgnoreCase("Molthor226")) {
            Watcher.getInstance().init();
            MessageHelper.send(jugador, "&6El Watcher esta activo.");
        } else {
            MessageHelper.send(jugador, "&cAsi de buebos?");
        }
    }

    @Subcommand("status")
    public void osStatus(Player jugador) {
        double[] tps = VanillaEnhancer.getInstance().getServer().getTPS();
        String state = SettingsHelper.getInstance().getSettings().getWatcher_enabled() ? "&aENABLED" : "&cDEAD";
        String tid = Watcher.getInstance().getTaskID() == null ? "DEAD" : Integer.toString(Watcher.getInstance().getTaskID());
        int numChuksLoaded = (Bukkit.getWorld("fauno") != null) ? Bukkit.getWorld("fauno").getLoadedChunks().length : 0;
        double t1 = GeneralUtils.formatDouble(tps[0]);
        double t2 = GeneralUtils.formatDouble(tps[1]);
        List<String> strings = new ArrayList<>();
        strings.add("Status: &a[" + state + "&a]");
        strings.add("Task ID: &a[&c" + tid + "&a]");
        strings.add("TPS [1M]: &a[" + (t1 > Watcher.getInstance().getWARNING_TPS() ? "&a" + t1 : "&e" + t1) + "]");
        strings.add("TPS [5M]: &a[" + (t2 > Watcher.getInstance().getMIN_TPS() ? (t2 < Watcher.getInstance().getWARNING_TPS() ? "&e" + t2 : "&a" + t2) : "&c" + t2) + "]");
        strings.add("PLAYERS: &a[" + VanillaEnhancer.getInstance().getServer().getOnlinePlayers().size() + "]");
        strings.add("CHUNKS: &a[" + numChuksLoaded + "]");
        strings.add("REPORT: &a[" + Watcher.getInstance().getStatus() + "&a]");
        MessageHelper.sendMultipleMessage(jugador, "&6Watcher Status", "OS", strings);
    }

    @Subcommand("reload")
    public void osReload(Player jugador) {
        if (jugador.isOp() || jugador.getName().equalsIgnoreCase("Molthor226")) {
            Watcher.getInstance().reloadWatcher();
            MessageHelper.send(jugador, "&6Se ha reiniciado el watcher.");
        } else {
            MessageHelper.send(jugador, "&cAsi de buebos?");
        }

    }

    @Subcommand("panic")
    @Description("Causa un OS TPS Panic")
    public void osPanic(Player jugador) {
        if (jugador.isOp() || jugador.getName().equalsIgnoreCase("Molthor226")) {
            Watcher.getInstance().panic(6.66);
            MessageHelper.send(jugador, "&cSe ha activado el kernel panic manual.");
        } else {
            MessageHelper.send(jugador, "&cUy Kieto");
        }
    }

    @Subcommand("unload")
    public void osUnload(Player jugador) {
        if (jugador.isOp() || jugador.getName().equalsIgnoreCase("Molthor226")) {
            MessageHelper.sendMultipleGlobal("&cAviso Importante",
                    "OS",
                    List.of(new String[]{"&cSe ha detectado demasiada latencia entre ticks", "&eIntentare matar las chunks inactivas en &c5&e Segundos."}));
            GeneralUtils.scheduleTask(Watcher.getInstance()::attemptChunkUnloading, 5);
        } else {
            MessageHelper.send(jugador, "&cUy Kieto");
        }
    }

    // This will lag the server 500 ms every second.
    // A tick has 20 interactions per second
    // Lagging it by 500 ms should result in 10TPS.
    @Subcommand("lagserver")
    public void osFakeLag(Player jugador) {
        if (jugador.isOp() || jugador.getName().equalsIgnoreCase("Molthor226")) {
            LAG_TASK_ID = GeneralUtils.scheduleRepeatingTask(() -> {
                try {
                    Thread.sleep(7000);
                } catch (Exception e) {
                    MessageHelper.send(jugador, "&cNo se ha podido crear el lag.");
                }
            }, 1, 10);
            MessageHelper.send(jugador, "&6Se ha iniciado el Lagger.");
        }else {
            MessageHelper.send(jugador, "&c:O");
        }
    }

    @Subcommand("delagserver")
    public void osCancelFakeLag(Player jugador) {
        if (jugador.isOp() || jugador.getName().equalsIgnoreCase("Molthor226")) {
            if (LAG_TASK_ID != null) {
                GeneralUtils.cancelScheduledTask(LAG_TASK_ID);
                MessageHelper.send(jugador, "&cHe detenido el lag intencional");
            }
        }else {
            MessageHelper.send(jugador, "&cNALGAS.");
        }
    }
}
