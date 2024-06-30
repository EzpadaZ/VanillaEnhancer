package dev.ezpadaz.vanillaenhancer.Utils.Database.Model.Player;

import com.google.gson.annotations.SerializedName;
import dev.ezpadaz.vanillaenhancer.Utils.ExperienceHelper;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import dev.ezpadaz.vanillaenhancer.Utils.InventoryHelper;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.Date;

public class PlayerModel {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("currentlyOnline")
    private boolean currentlyOnline;

    @SerializedName("lastSeen")
    private Date lastSeen;

    @SerializedName("lastKnowLocation")
    private String lastKnownLocation;

    @SerializedName("inventory")
    private String inventoryJson;

    @SerializedName("address")
    private String address;

    @SerializedName("expPoints")
    private String expPoints;

    public PlayerModel(Player jugador) {
        this.id = jugador.getUniqueId().toString();
        this.name = jugador.getName();
        this.lastKnownLocation = GeneralUtils.getLocationSerializer().toJson(jugador.getLocation());
        this.expPoints = Integer.toString(ExperienceHelper.getPlayerExp(jugador));
        this.address = jugador.getAddress().toString();
        this.inventoryJson = GeneralUtils.getObjectJson(InventoryHelper.playerInventoryToBase64(jugador.getInventory()));
    }

    public PlayerModel(Document doc) {
        this.id = doc.getString("_id");
        this.name = doc.getString("name");
        this.currentlyOnline = doc.getBoolean("currentlyOnline");
        this.lastSeen = doc.getDate("lastSeen");
        this.lastKnownLocation = doc.getString("lastKnownLocation");
        this.expPoints = doc.getString("expPoints");
        this.address = doc.getString("address");
        this.inventoryJson = doc.getString("inventoryJson");
    }

    public PlayerModel(Player jugador, boolean isOnline) {
        this.id = jugador.getUniqueId().toString();
        this.name = jugador.getName();
        this.currentlyOnline = isOnline;
        this.lastSeen = GeneralUtils.getISODate();
        this.lastKnownLocation = GeneralUtils.getLocationSerializer().toJson(jugador.getLocation());
        this.expPoints = Integer.toString(ExperienceHelper.getPlayerExp(jugador));
        this.address = jugador.getAddress().toString();
        this.inventoryJson = GeneralUtils.getObjectJson(InventoryHelper.playerInventoryToBase64(jugador.getInventory()));
    }

    public Document getDocumentFormat() {
        Document update = new Document();
        update.append("$set", new Document("name", getName())
                .append("lastKnowLocation", getLastKnownLocation())
                .append("currentlyOnline", currentlyOnline)
                .append("lastSeen", getLastSeen())
                .append("inventoryJson", getInventoryJson())
                .append("address", getAddress())
                .append("expPoints", getExpPoints()));
        return update;
    }

    public String getId() {
        return id;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public String getAddress() {
        return address;
    }

    public String getExpPoints() {
        return expPoints;
    }

    public String getName() {
        return name;
    }

    public String getLastKnownLocation() {
        return lastKnownLocation;
    }

    public String getInventoryJson() {
        return inventoryJson;
    }

    public Boolean getCurrentlyOnline() {
        return currentlyOnline;
    }
}
