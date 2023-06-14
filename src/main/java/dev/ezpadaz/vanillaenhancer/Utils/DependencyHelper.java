package dev.ezpadaz.vanillaenhancer.Utils;

import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.plugin.PluginManager;

public class DependencyHelper {
    public static boolean hasReviveMe = false;
    public static boolean hasPlaceholderApi = false;

    public static void checkDependencies() {
        PluginManager pm = VanillaEnhancer.getInstance().getServer().getPluginManager();

        if(pm.getPlugin("ReviveMe") != null){
            hasReviveMe = true;
            MessageHelper.console("ReviveMe detected, enabled support for it.");
        }

        if(pm.getPlugin("PlaceholderAPI") != null){
            hasPlaceholderApi = true;
            MessageHelper.console("PlaceholderAPI detected, enabled support for it.");
        }

    }

}
