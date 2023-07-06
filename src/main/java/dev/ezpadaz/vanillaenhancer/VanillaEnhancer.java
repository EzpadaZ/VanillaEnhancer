package dev.ezpadaz.vanillaenhancer;

import co.aikar.commands.PaperCommandManager;
import dev.ezpadaz.vanillaenhancer.Commands.CommandBootloader;
import dev.ezpadaz.vanillaenhancer.Commands.Teleport.TeleportCommander;
import dev.ezpadaz.vanillaenhancer.Listeners.ListenerBootloader;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Database;
import dev.ezpadaz.vanillaenhancer.Utils.DependencyHelper;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import dev.ezpadaz.vanillaenhancer.Utils.Watcher.Watcher;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class VanillaEnhancer extends JavaPlugin {
    private static VanillaEnhancer instance;
    public FileConfiguration config = getConfig();

    public PaperCommandManager manager;

    public static VanillaEnhancer getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        instance = this;
        Database.getInstance().init();
        manager = new PaperCommandManager(instance);
        DependencyHelper.checkDependencies();
        // Command Bootloader
        new CommandBootloader();

        // Listeners
        new ListenerBootloader();

        // Database.
        MessageHelper.console("&6Command Manager: &a[OK]");
        MessageHelper.console("&6Running on: &c" + getServer().getVersion());
        MessageHelper.console("&6Plugin Version: &c" + getDescription().getVersion());
        MessageHelper.console("&6Plugin Status: &a[OK]");

        // The TPS Guardian.
        Watcher.getInstance().init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Watcher.getInstance().stopWatcher();
        TeleportCommander.getInstance().cancelAllActiveTasks();
        MessageHelper.console("&6Plugin status: &cDisabled");
    }
}
