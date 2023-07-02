package dev.ezpadaz.vanillaenhancer.Utils.Database.Model.Player;

import com.google.gson.annotations.SerializedName;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Date;

public class PlayerTeleportModel {

    @SerializedName("_id")
    private String id;

    @SerializedName("targetID")
    private String targetID;

    @SerializedName("originID")
    private String originID;

    @SerializedName("date")
    private Date date;

    @SerializedName("location")
    private String location;

    public PlayerTeleportModel(Player origin, Player target, Location location) {
        this.id = GeneralUtils.generateUUID();
        this.targetID = target.getUniqueId().toString();
        this.originID = origin.getUniqueId().toString();
        this.location = GeneralUtils.getLocationSerializer().toJson(location);
        this.date = GeneralUtils.getISODate();
    }

    public Document getDocumentFormat() {
        Document update = new Document();
        update.append("$set", new Document("targetID", getTargetID())
                .append("eventType", "teleport")
                .append("originID", getOriginID())
                .append("location", getLocation())
                .append("date", getDate()));
        return update;
    }

    public String getId() {
        return id;
    }

    public String getTargetID() {
        return targetID;
    }

    public String getOriginID() {
        return originID;
    }

    public Date getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}
