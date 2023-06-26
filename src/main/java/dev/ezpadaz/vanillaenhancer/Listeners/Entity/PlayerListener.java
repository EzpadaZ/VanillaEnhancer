package dev.ezpadaz.vanillaenhancer.Listeners.Entity;

import dev.ezpadaz.vanillaenhancer.Commands.DoubleXP.Helper.XPEvent;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Operations.PlayerOperations;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // player joins the server
        PlayerOperations.savePlayerData(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        // player joined the server.
        PlayerOperations.savePlayerData(event.getPlayer());
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        XPEvent.checkForExpChange(event);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // death event.
        XPEvent.checkForPlayerDeath(event);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // might be removed later.
        GeneralUtils.saveEnchantedBook(event); // allows for ANY enchanted book to be saved in a chiseledbookshelf.
    }
}
