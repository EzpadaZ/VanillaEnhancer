package dev.ezpadaz.vanillaenhancer.Utils.Database.Model;

import com.google.gson.annotations.SerializedName;
import dev.ezpadaz.vanillaenhancer.Utils.ExperienceHelper;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import dev.ezpadaz.vanillaenhancer.Utils.InventoryHelper;
import org.bson.Document;
import org.bukkit.entity.Player;

public class PlayerModel {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

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

    public Document getDocumentFormat() {
        Document update = new Document();
        update.append("$set", new Document("name", getName())
                .append("lastKnowLocation", getLastKnownLocation())
                .append("inventoryJson", getInventoryJson())
                .append("address", getAddress())
                .append("expPoints", getExpPoints()));
        return update;
    }

    public String getId() {
        return id;
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
}
