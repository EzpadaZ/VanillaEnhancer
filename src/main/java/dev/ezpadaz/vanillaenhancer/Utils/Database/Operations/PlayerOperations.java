package dev.ezpadaz.vanillaenhancer.Utils.Database.Operations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Database;
import dev.ezpadaz.vanillaenhancer.Utils.Database.PlayerModel;
import org.bson.Document;
import org.bukkit.entity.Player;

public class PlayerOperations {
    public static void savePlayerData(Player jugador) {
        MongoCollection users = Database.getInstance().getCollection("Users");
        PlayerModel modelo = new PlayerModel(jugador);

        Document filter = new Document("_id", modelo.getId());

        Document update = new Document();
        update.append("$set", new Document("name", modelo.getName())
                .append("lastKnowLocation", modelo.getLastKnownLocation())
                .append("inventoryJson", modelo.getInventoryJson())
                .append("address", modelo.getAddress())
                .append("expPoints", modelo.getExpPoints()));

        UpdateOptions options = new UpdateOptions().upsert(true);

        users.updateOne(filter, update, options);
    }
}
