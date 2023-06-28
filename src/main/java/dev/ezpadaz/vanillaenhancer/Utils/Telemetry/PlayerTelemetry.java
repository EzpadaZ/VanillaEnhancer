package dev.ezpadaz.vanillaenhancer.Utils.Telemetry;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Database;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Model.PlayerDeathModel;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Model.PlayerModel;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Model.PlayerTeleportModel;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerTelemetry {
    public static void savePlayerData(Player jugador, boolean isOnline) {
        MongoCollection users = Database.getInstance().getCollection("Users");
        PlayerModel modelo = new PlayerModel(jugador, isOnline);
        Document filter = new Document("_id", modelo.getId());

        UpdateOptions options = new UpdateOptions().upsert(true);
        users.updateOne(filter, modelo.getDocumentFormat(), options);
    }

    public static void savePlayerDeath(Player jugador) {
        MongoCollection telemetry = Database.getInstance().getCollection("Telemetry");
        PlayerDeathModel model = new PlayerDeathModel(jugador);

        Document filter = new Document("_id", model.getId());
        UpdateOptions options = new UpdateOptions().upsert(true);
        telemetry.updateOne(filter, model.getDocumentFormat(), options);
    }

    public static void savePlayerTeleport(Player target, Player origin, Location location) {
        MongoCollection telemetry = Database.getInstance().getCollection("Telemetry");
        PlayerTeleportModel model = new PlayerTeleportModel(origin, target, location);

        Document filter = new Document("_id", model.getId());
        UpdateOptions options = new UpdateOptions().upsert(true);
        telemetry.updateOne(filter, model.getDocumentFormat(), options);
    }
}
