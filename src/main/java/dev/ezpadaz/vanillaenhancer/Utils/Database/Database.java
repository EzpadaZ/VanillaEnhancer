package dev.ezpadaz.vanillaenhancer.Utils.Database;

import com.mongodb.client.*;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.configuration.file.FileConfiguration;

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

        MessageHelper.console("Trying to connect to: " + connectionString);
        try {
            client = MongoClients.create(connectionString);
            database = client.getDatabase("minecraft");
            MessageHelper.console("&6Conectado a la base de datos en: &c" + connectionString);
        } catch (Exception e) {
            MessageHelper.console("&cNo se pudo conectar a MongoDB :-(");
        }
    }

    public MongoCollection getCollection(String collection){
        return database.getCollection(collection);
    }

    public static Database getInstance() {
        if (instance == null) instance = new Database();
        return instance;
    }
}
