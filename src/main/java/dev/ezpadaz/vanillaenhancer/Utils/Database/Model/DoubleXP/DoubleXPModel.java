package dev.ezpadaz.vanillaenhancer.Utils.Database.Model.DoubleXP;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Database;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class DoubleXPModel {
    private static List<PlayerOptedPair> playerOptedList = new ArrayList<>();
    private static Long lastUpdateTime;

    public static void saveOptedPlayer(String name, boolean status) {
        MongoCollection<Document> collection = Database.getInstance().getCollection("doublexp");
        Document document = new Document("name", name)
                .append("opted", status);

        UpdateOptions updateOptions = new UpdateOptions().upsert(true);

        collection.updateOne(Filters.eq("name", name), new Document("$set", document), updateOptions);

        PlayerOptedPair existingPair = getPlayerOptedPairByName(name);
        if (existingPair != null) {
            existingPair.setOpted(status);
        }
    }

    public static void refreshList() {
        MongoCollection collection = Database.getInstance().getCollection("doublexp");
        List<Document> documents = (List<Document>) collection.find().into(new ArrayList<>());

        playerOptedList.clear(); // Clear the list before populating it again

        for (Document document : documents) {
            String playerName = document.getString("name");
            boolean opted = document.getBoolean("opted", false);
            playerOptedList.add(new PlayerOptedPair(playerName, opted));
        }

    }

    public static boolean getOptedPlayer(String name) {
        long currentTime = System.currentTimeMillis();
        if (lastUpdateTime == null || playerOptedList.isEmpty() || currentTime - lastUpdateTime >= 60 * 1000) {
            refreshList();
        }
        PlayerOptedPair object = getPlayerOptedPairByName(name);
        if (object == null) return false;
        return object.isOpted();
    }

    public static PlayerOptedPair getPlayerOptedPairByName(String playerName) {
        return playerOptedList.stream()
                .filter(pair -> pair.getPlayerName().equals(playerName))
                .findFirst()
                .orElse(null);
    }

    public static List<PlayerOptedPair> getPlayerOptedList() {
        return playerOptedList;
    }
}

class PlayerOptedPair {
    private String playerName;
    private boolean opted;

    public PlayerOptedPair(String playerName, boolean opted) {
        this.playerName = playerName;
        this.opted = opted;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isOpted() {
        return opted;
    }

    public void setOpted(boolean opted) {
        this.opted = opted;
    }
}
