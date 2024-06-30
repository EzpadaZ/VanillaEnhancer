package dev.ezpadaz.vanillaenhancer.Utils.Database.Model.Generic;

import com.google.gson.annotations.SerializedName;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Model.Config.ConfigurationModel;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;

import java.util.Date;

public class TPSEventModel {
    @SerializedName("_id")
    private String id;
    @SerializedName("cdate")
    private Date cdate;
    @SerializedName("eventTPS")
    private double eventTPS;

    @SerializedName("playersOnline")
    private int playersOnline;

    @SerializedName("chunksLoaded")
    private int chunksLoaded;

    @SerializedName("panic")
    private boolean didPanic;

    public boolean getPanic() {
        return didPanic;
    }

    public int getPlayersOnline() {
        return playersOnline;
    }

    public int getChunksLoaded() {
        return chunksLoaded;
    }

    public String getId() {
        return id;
    }

    public Date getCdate() {
        return cdate;
    }

    public double getEventTPS() {
        return eventTPS;
    }

    public TPSEventModel(double recordedTPS, boolean panic) {
        World world = VanillaEnhancer.getInstance().getServer().getWorlds().get(0);

        this.id = GeneralUtils.generateUUID();
        this.cdate = GeneralUtils.getISODate();
        this.didPanic = panic;
        this.playersOnline = Bukkit.getOnlinePlayers().size();
        this.chunksLoaded = world.getLoadedChunks().length;
        this.eventTPS = recordedTPS;
    }

    public Document getDocumentFormat() {
        Document update = new org.bson.Document();
        update.append("$set", new org.bson.Document("cdate", getCdate())
                .append("eventType", "tps")
                .append("playersOnline", getPlayersOnline())
                .append("chunksLoaded", getChunksLoaded())
                .append("panic", getPanic())
                .append("eventTPS", getEventTPS()));
        return update;
    }
}
