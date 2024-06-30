package dev.ezpadaz.vanillaenhancer.Utils;

import co.aikar.commands.BaseCommand;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Serializer.LocationSerializer;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import javax.annotation.concurrent.Immutable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class GeneralUtils {

    /**
     * Schedules a task to be executed after a specified delay.
     *
     * @param task  The task to be executed
     * @param delay The delay in seconds before the task should be executed
     * @return The task ID
     */
    public static int scheduleTask(Runnable task, long delay) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(VanillaEnhancer.getInstance(), task, 20L * delay);
    }

    /**
     * Schedules a task to be repeatedly executed at a fixed interval.
     *
     * @param task     The task to be executed
     * @param delay    The delay in seconds before the first execution
     * @param interval The interval in seconds between each execution
     * @return The task ID
     */
    public static int scheduleRepeatingTask(Runnable task, long delay, long interval) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(VanillaEnhancer.getInstance(), task, delay * 20L, interval * 20L);
    }

    /**
     * Cancels a scheduled task.
     *
     * @param task The task ID to be canceled
     */
    public static void cancelScheduledTask(int task) {
        Bukkit.getScheduler().cancelTask(task);
    }

    /**
     * Registers a command.
     *
     * @param command The command to be registered
     */
    public static void registerCommand(BaseCommand command) {
        VanillaEnhancer.getInstance().manager.registerCommand(command);
    }

    public static void registerCompleter(String completerID, List<String> list){
        VanillaEnhancer.getInstance().manager.getCommandCompletions().registerCompletion(completerID, c -> ImmutableList.of(list.toString()));
    }

    /**
     * Retrieves a Material based on its name.
     *
     * @param name The name of the Material
     * @return The Material, or null if not found
     */
    public static Material getMaterial(String name) {
        return Material.matchMaterial(name);
    }

    /**
     * Registers an event listener.
     *
     * @param listener The listener to be registered
     */
    public static void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, VanillaEnhancer.getInstance());
    }

    /**
     * Retrieves a Player based on their name.
     *
     * @param name The name of the player
     * @return The Player object, or null if not found
     */
    public static Player getPlayer(String name) {
        return VanillaEnhancer.getInstance().getServer().getPlayer(name);
    }

    /**
     * Converts an object to its JSON representation.
     *
     * @param object The object to be converted
     * @return The JSON representation of the object
     */
    public static String getObjectJson(Object object) {
        return new Gson().toJson(object);
    }

    /**
     * Generates a UUID-based string.
     *
     * @return The generated UUID-based string
     */
    public static String generateUUID() {
        long timestamp = Instant.now().toEpochMilli();
        UUID randomUUID = UUID.randomUUID();
        return String.format("%d-%s", timestamp, randomUUID);
    }

    /**
     * Creates a Gson object with a custom Location serializer.
     *
     * @return The Gson object with the custom Location serializer
     */
    public static Gson getLocationSerializer() {
        return new GsonBuilder()
                .registerTypeAdapter(Location.class, new LocationSerializer())
                .create();
    }

    /**
     * Formats a double value to two decimal places.
     *
     * @param value The double value to be formatted
     * @return The formatted double value
     */
    public static double formatDouble(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formattedValue = decimalFormat.format(value);
        return Double.parseDouble(formattedValue);
    }

    /**
     * Retrieves the current date and time in ISO 8601 format.
     *
     * @return The current date and time
     */
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

    public static String toISOString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    public static double ISODateDifferenceInMinutes(String startTime, String endTime){
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date startDate = isoFormat.parse(startTime);
            Date endDate = isoFormat.parse(endTime);

            long duration = endDate.getTime() - startDate.getTime();
            return TimeUnit.MILLISECONDS.toMinutes(duration);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
