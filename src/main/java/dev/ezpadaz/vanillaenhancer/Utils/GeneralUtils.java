package dev.ezpadaz.vanillaenhancer.Utils;

import com.google.gson.Gson;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GeneralUtils {
    public static void scheduleTask(Runnable task, long delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(VanillaEnhancer.getInstance(), task, 20L * delay);
    }

    public static Player getPlayer(String name) {
        return VanillaEnhancer.getInstance().getServer().getPlayer(name);
    }

    public static String getObjectJson(Object object){
        return new Gson().toJson(object);
    }
}
