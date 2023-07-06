package dev.ezpadaz.vanillaenhancer.Utils.Database.Model.Generic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import org.bson.Document;

import java.util.Date;
import java.util.HashMap;

public class EventLogModel {
    @SerializedName("_id")
    private String id;

    @SerializedName("eventType")
    private String eventType;

    @SerializedName("metadata")
    private HashMap<String, Object> metadata;

    @SerializedName("eventDate")
    private Date eventDate;

    public EventLogModel(String eventType, HashMap<String, Object> metadata) {
        this.id = GeneralUtils.generateUUID();
        this.eventType = eventType;
        this.metadata = metadata;
        this.eventDate = GeneralUtils.getISODate();
    }

    /**
     * Generates a Document object representing the event log in a format suitable for MongoDB storage.
     *
     * @return Document object representing the event log
     */
    public Document getDocumentFormat() {
        // Create a Gson instance to convert metadata to JSON
        Gson gson = new GsonBuilder().create();
        String metadataJson = gson.toJson(metadata);

        // Create the MongoDB document
        Document document = new Document();
        document.append("$set", new Document("eventType", eventType)
                .append("metadata", metadataJson)
                .append("eventDate", eventDate));
        return document;
    }

    /**
     * Gets the ID of the event log.
     *
     * @return ID of the event log
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the event type of the event log.
     *
     * @return Event type of the event log
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Gets the metadata associated with the event log.
     *
     * @return Metadata of the event log
     */
    public HashMap<String, Object> getMetadata() {
        return metadata;
    }

    /**
     * Gets the date and time of the event log.
     *
     * @return Date and time of the event log
     */
    public Date getEventDate() {
        return eventDate;
    }
}