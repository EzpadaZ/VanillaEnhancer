package dev.ezpadaz.vanillaenhancer.Utils.Database;

import com.mongodb.client.*;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import dev.ezpadaz.vanillaenhancer.Utils.SettingsHelper;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Database {
    private static Database instance;
    private static MongoClient client;
    private static MongoDatabase database;
    private static String connectionString = "";

    private Database() {
    }

    public void init() {
        FileConfiguration config = VanillaEnhancer.getInstance().config;
        connectionString = config.getString("mongodb") == null ? "mongodb://localhost:27017/minecraft" : config.getString("mongodb");

        // Get the logger instance for the MongoDB Java driver
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver.client");

        // Disable default handlers to silence output
        mongoLogger.setUseParentHandlers(false);

        // Create a new console handler and set its level to WARNING
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.WARNING);

        // Add the console handler to the MongoDB logger
        mongoLogger.addHandler(consoleHandler);

        try {
            client = MongoClients.create(connectionString);
            database = client.getDatabase("minecraft");
            MessageHelper.console("&6Connected to: &c" + connectionString);
            SettingsHelper.getInstance().setupConfig();
        } catch (Error e) {
            PluginManager pm = Bukkit.getPluginManager();
            Plugin self = pm.getPlugin("VanillaEnhancer");

            if (self != null) {
                MessageHelper.console("&cWe fucking died trying to connect to MongoDB.");
                MessageHelper.console("&c"+e.getMessage());
                pm.disablePlugin(self);
            }
        }
    }

    public MongoCollection getCollection(String collection) {
        return database.getCollection(collection);
    }

    public void close() {
        try {
            client.close();
        } catch (Exception e) {
            // do nothing.
        }
    }

    public static Database getInstance() {
        if (instance == null) instance = new Database();
        return instance;
    }
}
