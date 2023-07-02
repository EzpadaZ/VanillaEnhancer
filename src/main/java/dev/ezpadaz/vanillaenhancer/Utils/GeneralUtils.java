package dev.ezpadaz.vanillaenhancer.Utils;

import co.aikar.commands.BaseCommand;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Serializer.LocationSerializer;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;


public class GeneralUtils {
    public static int scheduleTask(Runnable task, long delay) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(VanillaEnhancer.getInstance(), task, 20L * delay);
    }

    public static int scheduleRepeatingTask(Runnable task, long delay, long interval) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(VanillaEnhancer.getInstance(), task, delay * 20L, interval * 20L);
    }

    public static void cancelScheduledTask(int task){
        Bukkit.getScheduler().cancelTask(task);
    }

    public static void registerCommand(BaseCommand command) {
        VanillaEnhancer.getInstance().manager.registerCommand(command);
    }

    public static Material getMaterial(String name) {
        return Material.matchMaterial(name);
    }

    public static void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, VanillaEnhancer.getInstance());
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

    public static Date getISODate() {
        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedDate = now.format(formatter);

        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return isoFormat.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the exception according to your needs
            return null;
        }
    }
}
