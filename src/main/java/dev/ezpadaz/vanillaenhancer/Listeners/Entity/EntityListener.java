package dev.ezpadaz.vanillaenhancer.Listeners.Entity;

import dev.ezpadaz.vanillaenhancer.Utils.GameplayEnhancer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class EntityListener implements Listener {

    @EventHandler
    public void onCartMove(VehicleMoveEvent event){
        GameplayEnhancer.cartSpeedIncrease(event);
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event){
        GameplayEnhancer.infiniteArrowEnchantment(event);
    }
}
