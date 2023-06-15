package dev.ezpadaz.vanillaenhancer.Utils;

import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.Bukkit;

public class GeneralUtils {
    public static void scheduleTask(Runnable task, long delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(VanillaEnhancer.getInstance(), task, 20L * delay);
    }
}
