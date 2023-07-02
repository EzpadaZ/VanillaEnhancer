package dev.ezpadaz.vanillaenhancer.Utils.Database.Model.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Serializer.LocationSerializer;
import dev.ezpadaz.vanillaenhancer.Utils.ExperienceHelper;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import dev.ezpadaz.vanillaenhancer.Utils.InventoryHelper;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Date;

public class PlayerDeathModel {

    @SerializedName("_id")
    private String id;
    @SerializedName("playerID")
    private String playerID;
    @SerializedName("deathLocation")
    private String deathLocation;
    @SerializedName("expAtDeath")
    private String expAtDeath;
    @SerializedName("inventoryAtDeath")
    private String inventoryAtDeath;

    @SerializedName("eventDate")
    private Date eventDate;

    public PlayerDeathModel(Player jugador) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Location.class, new LocationSerializer())
                .create();

        this.id = GeneralUtils.generateUUID();
        this.playerID = jugador.getUniqueId().toString();
        this.eventDate = GeneralUtils.getISODate();
        this.deathLocation = gson.toJson(jugador.getLocation());
        this.expAtDeath = Integer.toString(ExperienceHelper.getPlayerExp(jugador));
        this.inventoryAtDeath = GeneralUtils.getObjectJson(InventoryHelper.playerInventoryToBase64(jugador.getInventory()));
    }

    public Document getDocumentFormat() {
        Document document = new Document();
        document.append("$set", new Document("deathLocation", getDeathLocation())
                .append("eventType", "death")
                .append("playerID", getPlayerID())
                .append("eventDate", getEventDate())
                .append("inventoryAtDeath", getInventoryAtDeath())
                .append("expAtDeath", getExpAtDeath()));
        return document;
    }

    public String getPlayerID() {
        return playerID;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public String getId() {
        return id;
    }

    public String getDeathLocation() {
        return deathLocation;
    }

    public String getExpAtDeath() {
        return expAtDeath;
    }

    public String getInventoryAtDeath() {
        return inventoryAtDeath;
    }
}
