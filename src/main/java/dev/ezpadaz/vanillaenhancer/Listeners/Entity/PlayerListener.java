package dev.ezpadaz.vanillaenhancer.Listeners.Entity;

import dev.ezpadaz.vanillaenhancer.Commands.DoubleXP.Helper.XPEvent;
import dev.ezpadaz.vanillaenhancer.Utils.GameplayEnhancer;
import dev.ezpadaz.vanillaenhancer.Utils.Telemetry.PlayerTelemetry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // player joins the server
        PlayerTelemetry.savePlayerData(event.getPlayer(), true);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        // player exited the server.
        PlayerTelemetry.savePlayerSession(event.getPlayer()); // save session
        PlayerTelemetry.savePlayerData(event.getPlayer(), false); // update user.
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        XPEvent.checkForExpChange(event);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // death event.
        XPEvent.checkForPlayerDeath(event);
        PlayerTelemetry.savePlayerDeath(event.getEntity().getPlayer());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // might be removed later.
        GameplayEnhancer.saveEnchantedBook(event); // allows for ANY enchanted book to be saved in a chiseledbookshelf.
    }
}
