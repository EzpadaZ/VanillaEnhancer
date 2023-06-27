package dev.ezpadaz.vanillaenhancer.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Serializer.LocationSerializer;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


public class GeneralUtils {
    public static int scheduleTask(Runnable task, long delay) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(VanillaEnhancer.getInstance(), task, 20L * delay);
    }

    public static Player getPlayer(String name) {
        return VanillaEnhancer.getInstance().getServer().getPlayer(name);
    }

    public static String getObjectJson(Object object) {
        return new Gson().toJson(object);
    }

    public static String generateUUID() {
        long timestamp = Instant.now().toEpochMilli();
        UUID randomUUID = UUID.randomUUID();
        return String.format("%d-%s", timestamp, randomUUID);
    }

    public static Gson getLocationSerializer() {
        return new GsonBuilder()
                .registerTypeAdapter(Location.class, new LocationSerializer())
                .create();
    }

    public static String getISODate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return now.format(formatter);
    }
}
