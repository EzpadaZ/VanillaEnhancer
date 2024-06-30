package dev.ezpadaz.vanillaenhancer.Utils.Database.Model.Player;

import com.google.gson.annotations.SerializedName;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import org.bson.Document;
import org.bukkit.entity.Player;

public class PlayerSessionModel {

    @SerializedName("_id")
    private String id;
    @SerializedName("userId")
    private String userId;
    @SerializedName("startDate")
    private String startDate;
    @SerializedName("endDate")
    private String endDate;
    @SerializedName("totalPlaytime")
    private double totalPlaytime;

    public PlayerSessionModel(Player jugador, String startDate) {
        this.id = GeneralUtils.generateUUID();
        this.userId = jugador.getUniqueId().toString();
        this.startDate = startDate;
        this.endDate = GeneralUtils.toISOString(GeneralUtils.getISODate());
        this.totalPlaytime = GeneralUtils.ISODateDifferenceInMinutes(this.startDate, this.endDate);
    }

    public Document getDocumentFormat() {
        Document update = new Document();
        update.append("$set", new Document("userId", getUserId())
                .append("startDate", getStartDate())
                .append("endDate", getEndDate())
                .append("totalPlaytime", getTotalPlaytime()));
        return update;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public double getTotalPlaytime() {
        return totalPlaytime;
    }

}
