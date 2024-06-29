package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

import dev.ezpadaz.vanillaenhancer.Utils.Database.Model.Config.ConfigurationModel;
import dev.ezpadaz.vanillaenhancer.Utils.EffectHelper;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import dev.ezpadaz.vanillaenhancer.Utils.SettingsHelper;
import dev.ezpadaz.vanillaenhancer.Utils.Telemetry.PlayerTelemetry;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Objects;

public class TeleportCommander {
    private static TeleportCommander instance;
    private static boolean TRAVEL_ACTIVE = false;
    public HashMap<String, TeleportDAO> teleportQueue;
    public HashMap<String, Location> locationMemory;

    public HashMap<String, Integer> activeTasks;
    public int MATERIAL_COST;
    public int TRAVEL_BACK_MATERIAL_COST;
    public Material MATERIAL_TYPE;
    public String MATERIAL_NAME;
    public int REQUEST_TIMEOUT;
    public int TELEPORT_DELAY;

    private TeleportCommander() {
        teleportQueue = new HashMap<>();
        locationMemory = new HashMap<>();
        activeTasks = new HashMap<>();
        configure();
    }

    public static TeleportCommander getInstance() {
        if (instance == null) instance = new TeleportCommander();
        return instance;
    }

    public void configure() {
        ConfigurationModel settings = SettingsHelper.getInstance().getSettings();
        MATERIAL_COST = settings.getTp_cost();
        TRAVEL_BACK_MATERIAL_COST = MATERIAL_COST / 2;

        MATERIAL_TYPE = GeneralUtils.getMaterial(settings.getTp_material());
        ItemStack itemStack = new ItemStack(MATERIAL_TYPE);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta.hasDisplayName()) {
            MATERIAL_NAME = String.valueOf(itemMeta.displayName());
        } else {
            MATERIAL_NAME = itemMeta.getDisplayName();
        }

        if (MATERIAL_NAME.equals("")) {
            MATERIAL_NAME = settings.getTp_material_name();
        }

        REQUEST_TIMEOUT = settings.getTp_threshold();
        TELEPORT_DELAY = settings.getTp_delay();
        TRAVEL_ACTIVE = settings.getTp_enabled();
    }

    public boolean getTravelEnabled() {
        return TRAVEL_ACTIVE;
    }

    // Creates the command instances.
    public void setupCommander() {
        GeneralUtils.registerCommand(new TPRechazarCommand());
        GeneralUtils.registerCommand(new TPRegresarCommand());
        GeneralUtils.registerCommand(new TPVoyCommand());
        GeneralUtils.registerCommand(new TPVenCommand());
        GeneralUtils.registerCommand(new TPAceptarCommand());
    }

    public void addPlayerRequest(String destino, TeleportDAO data) {
        if (teleportQueue.get(destino) != null) {
            // remove previous request.
            removeTeleportTask(data.getOrigen()); // removes the previous owner task.
            teleportQueue.remove(destino); // cleans the queue for this player.
        }

        teleportQueue.put(destino, data); // puts new data.
        int taskID = GeneralUtils.scheduleTask(() -> {
            Player target = Bukkit.getPlayer(destino);

            if (target != null && teleportQueue.containsKey(destino)) {
                MessageHelper.send(target, "&cLa petición que te habían enviado ha caducado.");
            }

            teleportQueue.remove(destino);
        }, REQUEST_TIMEOUT);

        activeTasks.put(data.getOrigen(), taskID); // saves the requester name with the assigned task id.
    }

    public void removeTeleportTask(String origen) {
        Integer task = activeTasks.get(origen);

        if (task != null) {
            Bukkit.getScheduler().cancelTask(task);

            Player jugador = GeneralUtils.getPlayer(origen);
            if (jugador != null) MessageHelper.send(jugador, "&cHe cancelado el viaje :-)");
        }
    }

    public void cancelTrip(String origen) {
        TeleportDAO object = searchForOrigin(origen); // get the object

        if (object == null) {
            MessageHelper.send(GeneralUtils.getPlayer(origen), "&cNo hay nada que cancelar."); // warn the trip was cancelled.
            return;
        }
        Player jugador = GeneralUtils.getPlayer(object.getDestino()); // get the player from object
        removeTeleportTask(origen); // remove the task id.
        if (jugador != null) {
            MessageHelper.send(jugador, "&c%s &6ha cancelado el viaje.".formatted(origen)); // warn the trip was cancelled.
        }
        removePlayerRequest(object.getDestino(), true);
    }

    @Nullable
    private TeleportDAO searchForOrigin(String origin) {
        for (TeleportDAO object : teleportQueue.values()) {
            if (object.getOrigen().equals(origin)) {
                return object;
            }
        }
        return null;
    }

    public void cancelAllActiveTasks() {
        for (Integer ID : activeTasks.values()) {
            Bukkit.getScheduler().cancelTask(ID);
        }
        MessageHelper.console("&cAll active teleport tasks have been killed.");
    }

    public void addLocationToMemory(String owner, Location object) {
        locationMemory.put(owner, object);
        Player p = GeneralUtils.getPlayer(owner);
        if (p != null)
            MessageHelper.send(p, "&6Para volver a tu ubicacion anterior usa &a/regresar &6Se te cobrara &c" + (TRAVEL_BACK_MATERIAL_COST) + " &3Lingotes de Oro.");

    }

    public void removeLastLocation(String owner) {
        locationMemory.remove(owner);
    }

    public Location getPreviousLocation(String owner) {
        return locationMemory.get(owner);
    }

    public void teleportPlayer(Player target, Player origin, Location destination, long TELEPORT_DELAY) {
        if (isSafe(destination) && !origin.isFlying() && !origin.isGliding()) {
            EffectHelper.getInstance().smokeEffect(target);
            GeneralUtils.scheduleTask(() -> {
                TeleportCommander.getInstance().addLocationToMemory(target.getName(), target.getLocation());
                EffectHelper.getInstance().strikeLightning(target);
                target.teleport(destination, PlayerTeleportEvent.TeleportCause.PLUGIN);
                EffectHelper.getInstance().strikeLightning(target);
                MessageHelper.send(target, "&6Peticion aceptada con exito!.");
                MessageHelper.send(origin, "&c%s &6acepto tu peticion, he retirado el costo de tu inventario".formatted(target.getName()));
                PlayerTelemetry.savePlayerTeleport(target, origin, destination);
            }, TELEPORT_DELAY);
        } else {
            MessageHelper.send(target, "&cHe cancelado el viaje ya que no es seguro.");
            MessageHelper.send(origin, "&cNo pude realizar el viaje por que algo en tu ubicacion no es seguro para el otro sujeto.");
        }
    }

    public void unsafeTeleportPlayer(Player target, Location destination, long TELEPORT_DELAY) {
        if (isSafe(destination)) {
            EffectHelper.getInstance().smokeEffect(target);
            GeneralUtils.scheduleTask(() -> {
                EffectHelper.getInstance().strikeLightning(target);
                target.teleport(destination, PlayerTeleportEvent.TeleportCause.PLUGIN);
                EffectHelper.getInstance().strikeLightning(target);
            }, TELEPORT_DELAY);
        } else {
            MessageHelper.send(target, "&cHe cancelado el viaje ya que no es seguro.");
        }
    }

    public boolean isSafe(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        Material bloque = location.getWorld().getBlockAt(x, y, z).getType();
        Material debajo = location.getWorld().getBlockAt(x, y - 1, z).getType();
        Material inferior = location.getWorld().getBlockAt(x, y - 2, z).getType();

        if ((bloque == Material.AIR) && (debajo == Material.AIR) && (inferior == Material.AIR)) {
            // el jugador origen esta cayendo.
            return false;
        }

        boolean isLava = bloque == Material.LAVA || bloque == Material.LAVA_BUCKET;

        return !(isLava && !bloque.isSolid());
    }

    public void removePlayerRequest(String destino, boolean silent) {
        Player jugador = Bukkit.getServer().getPlayer(destino);

        if (jugador != null && teleportQueue.containsKey(destino)) {
            if (!silent) {
                Player origen = GeneralUtils.getPlayer(teleportQueue.get(destino).getOrigen());
                MessageHelper.send(jugador, "&6He cancelado las solicitudes pendientes que tenias :-)");
                MessageHelper.send(origen, "&c%s &6te mando a la verga, ni modo.".formatted(jugador.getName()));
            }
            teleportQueue.remove(destino);
        } else {
            MessageHelper.send(jugador, "&cNo tenias solicitudes pendientes.");
        }
    }

    public TeleportDAO getPlayerRequest(String destino) {
        return teleportQueue.get(destino);
    }
}
