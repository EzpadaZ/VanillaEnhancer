package dev.ezpadaz.vanillaenhancer.Utils;

import com.mongodb.client.MongoCollection;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Database;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Model.Config.ConfigurationModel;
import org.bson.Document;

public class SettingsHelper {
    private MongoCollection collection;
    private static SettingsHelper instance;

    private SettingsHelper() {
        collection = Database.getInstance().getCollection("Config");
    }

    public void setupConfig() {
        // find it, if it isnt found, create one.
        Document existingConfig = (Document) collection.find().first();

        if (existingConfig == null) {
            collection.insertOne(new ConfigurationModel().getDocumentFormat());
        }else{
            // parse the found document to a model
            ConfigurationModel model = new ConfigurationModel().getFromDocument(existingConfig);
            // new Document means empty filter, meaning it targets the first document it gets.
            collection.replaceOne(new Document(), model.getDocumentFormat());
        }
    }

    public ConfigurationModel getSettings() {
        Document config = (Document) collection.find().first();
        return new ConfigurationModel().getFromDocument(config);
    }

    public static SettingsHelper getInstance() {
        if (instance == null) instance = new SettingsHelper();
        return instance;
    }

}
