package dev.ezpadaz.vanillaenhancer;

import dev.ezpadaz.vanillaenhancer.Commands.CommandBootloader;
import dev.ezpadaz.vanillaenhancer.Listeners.ListenerBootloader;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Database;
import dev.ezpadaz.vanillaenhancer.Utils.DependencyHelper;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class VanillaEnhancer extends JavaPlugin {

    private static VanillaEnhancer instance;
    public FileConfiguration config = getConfig();

    public static VanillaEnhancer getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveConfig();
        instance = this;

        // Command Bootloader
        new CommandBootloader();

        // Listeners
        new ListenerBootloader();

        // Database.
        Database.getInstance().init();
        MessageHelper.consoleDebug("test");

        DependencyHelper.checkDependencies();
        MessageHelper.console("&6Plugin status: &aEnabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        MessageHelper.console("&6Plugin status: &cDisabled");
    }
}
