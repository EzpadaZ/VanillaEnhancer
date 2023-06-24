package dev.ezpadaz.vanillaenhancer.Utils.Database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Serializer.LocationSerializer;
import dev.ezpadaz.vanillaenhancer.Utils.ExperienceHelper;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import dev.ezpadaz.vanillaenhancer.Utils.InventoryHelper;
import org.bukkit.Location;
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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Location.class, new LocationSerializer())
                .create();

        this.id = jugador.getUniqueId().toString();
        this.name = jugador.getName();
        this.lastKnownLocation = gson.toJson(jugador.getLocation());
        this.expPoints = Integer.toString(ExperienceHelper.getPlayerExp(jugador));
        this.address = jugador.getAddress().toString();
        this.inventoryJson = GeneralUtils.getObjectJson(InventoryHelper.playerInventoryToBase64(jugador.getInventory()));
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
