package dev.ezpadaz.vanillaenhancer.Listeners.Entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        // death event.
    }
}
