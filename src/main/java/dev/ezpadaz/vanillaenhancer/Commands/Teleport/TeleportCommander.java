package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TeleportCommander {
    public static TeleportCommander instance;
    public HashMap<String, TeleportDAO> teleportQueue;


    public int REQUEST_TIMEOUT = 60;

    private TeleportCommander() {
        teleportQueue = new HashMap<>();
    }

    public static TeleportCommander getInstance() {
        if (instance == null) instance = new TeleportCommander();
        return instance;
    }

    public void addPlayerRequest(String destino, TeleportDAO data) {
        teleportQueue.remove(destino);

        teleportQueue.put(destino, data);
        Bukkit.getScheduler().scheduleSyncDelayedTask(VanillaEnhancer.getInstance(), () -> {

            Player target = Bukkit.getPlayer(destino);

            if (target != null && teleportQueue.containsKey(destino)) {
                MessageHelper.send(target, "&cLa peticion que te habian enviado ha caducado.");
            }

            teleportQueue.remove(destino);

        }, 20L * REQUEST_TIMEOUT);
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

    public void removePlayerRequest(String destino) {
        Player jugador = Bukkit.getServer().getPlayer(destino);

        if (jugador != null && teleportQueue.containsKey(destino)) {
            MessageHelper.send(jugador, "&6He cancelado las solicitudes pendientes que tenias :-)");
            teleportQueue.remove(destino);
        } else {
            MessageHelper.send(jugador, "&cNo tenias solicitudes pendientes.");
        }


    }

    public TeleportDAO getPlayerRequest(String destino) {
        return teleportQueue.get(destino);
    }
}
