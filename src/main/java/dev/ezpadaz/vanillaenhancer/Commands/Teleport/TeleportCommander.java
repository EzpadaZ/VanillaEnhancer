package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

import dev.ezpadaz.vanillaenhancer.Utils.EffectHelper;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;

public class TeleportCommander {
    private static TeleportCommander instance;
    private static boolean isTravelEnabled = false;
    public HashMap<String, TeleportDAO> teleportQueue;
    public HashMap<String, Location> locationMemory;
    public int MATERIAL_COST = 16;
    public int TRAVEL_BACK_MATERIAL_COST = MATERIAL_COST / 2;
    public Material MATERIAL_TYPE = Material.GOLD_INGOT;
    public String MATERIAL_NAME = "Lingotes de oro";
    public int REQUEST_TIMEOUT = 60;
    public int PREVIOUS_LOCATION_TIMEOUT = 300; // 5-minutes.
    public int TELEPORT_DELAY = 1;
    private TeleportCommander() {
        teleportQueue = new HashMap<>();
        locationMemory = new HashMap<>();
    }

    public static TeleportCommander getInstance() {
        if (instance == null) instance = new TeleportCommander();
        return instance;
    }

    public boolean getTravelEnabled() {
        return isTravelEnabled;
    }

    public void setTravelEnabled(boolean value){
        isTravelEnabled = value;
    }

    // Creates the command instances.
    public void setupCommander() {
        new TPVoyCommand();
        new TPRechazarCommand();
        new TPAceptarCommand();
        new TPVenCommand();
        new TPRegresarCommand();
    }

    public void addPlayerRequest(String destino, TeleportDAO data) {
        teleportQueue.remove(destino);

        teleportQueue.put(destino, data);
        GeneralUtils.scheduleTask(() -> {
            Player target = Bukkit.getPlayer(destino);

            if (target != null && teleportQueue.containsKey(destino)) {
                MessageHelper.send(target, "&cLa petición que te habían enviado ha caducado.");
            }

            teleportQueue.remove(destino);
        }, REQUEST_TIMEOUT);

    }

    public void addLocationToMemory(String owner, Location object) {
        locationMemory.put(owner, object);
        Player p = GeneralUtils.getPlayer(owner);
        if(p != null) MessageHelper.send(p, "&6Tienes&c "+(PREVIOUS_LOCATION_TIMEOUT / 60)+" &6minutos para usar &a/regresar&6\nSe te cobrara &c"+(MATERIAL_COST/2)+" &3Lingotes de Oro.");

        GeneralUtils.scheduleTask(() -> {
            if(locationMemory.get(owner) != null){
                locationMemory.remove(owner);
                Player jugador = GeneralUtils.getPlayer(owner);
                if(jugador != null) MessageHelper.send(jugador, "Ha expirado tu tiempo para regresar a tu ubicacion anterior :-(");
            }
        }, PREVIOUS_LOCATION_TIMEOUT);
    }

    public void removeLastLocation(String owner){
        locationMemory.remove(owner);
    }

    public Location getPreviousLocation(String owner){
        return locationMemory.get(owner);
    }

    public void teleportPlayer(Player player, Location destination, long TELEPORT_DELAY) {
        if (isSafe(destination) && !player.isFlying() && !player.isGliding()) {
            GeneralUtils.scheduleTask(() -> {
                EffectHelper.getInstance().strikeLightning(player);
                player.teleport(destination, PlayerTeleportEvent.TeleportCause.PLUGIN);
                EffectHelper.getInstance().strikeLightning(player);
            }, TELEPORT_DELAY);
        } else {
            MessageHelper.send(player, "&cHe cancelado el viaje ya que no es seguro.");
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
                MessageHelper.send(jugador, "&6He cancelado las solicitudes pendientes que tenias :-)");
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
