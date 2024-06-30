package dev.ezpadaz.vanillaenhancer.Utils;

import dev.ezpadaz.vanillaenhancer.Utils.Database.Model.Config.ConfigurationModel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.ChiseledBookshelf;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ChiseledBookshelfInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Objects;

public class GameplayEnhancer {
    // There is a bug where books can be stored in the chiseled bookshelf.
    // BUT Enchanted books from plugins cant so this is a placeholder fix for that.
    // I will make it work based on configuration on the database, but for now it remains active until bugs are detected.
    public static void saveEnchantedBook(PlayerInteractEvent event) {
        Block blockEvent = event.getClickedBlock();
        ItemStack item = event.getItem();

        if (blockEvent == null) {
            return;
        }

        if (blockEvent.getType() == Material.CHISELED_BOOKSHELF) {
            ChiseledBookshelf bookshelf = ((ChiseledBookshelf) blockEvent.getState());
            ChiseledBookshelfInventory inventory = bookshelf.getInventory();
            if (item != null && item.getType() == Material.ENCHANTED_BOOK) {
                // is a valid enchantment book, save it in the storage.
                Vector pos = event.getClickedPosition();
                int bslot = bookshelf.getSlot(pos); // gets chiseled_bookshelf looked at position.
                ItemStack tempItem = inventory.getItem(bslot);
                Player jugador = event.getPlayer();

                if (tempItem == null) {
                    inventory.setItem(bslot, item);
                    InventoryHelper.removeSpecificItem(event.getPlayer(), item);
                    jugador.playSound(jugador.getLocation(), Sound.BLOCK_CHISELED_BOOKSHELF_INSERT_ENCHANTED, 1.0f, 1.0f);
                } else {
                    ItemStack returnedItem = inventory.getItem(bslot);
                    inventory.removeItem(returnedItem);
                    jugador.getInventory().addItem(returnedItem);
                }
            }
        }
    }

    public static void cartSpeedIncrease(VehicleMoveEvent event) {
        ConfigurationModel settings = SettingsHelper.getInstance().getSettings();
        double METERS_PER_TICK = 0.4d;
        double MAX_METERS_PER_TICK = METERS_PER_TICK * settings.getCart_speed();
        if (settings.getCart_enabled()) {
            if (event.getVehicle() instanceof Minecart) {
                Minecart cart = (Minecart) event.getVehicle();
                Block rail = cart.getLocation().getBlock();
                Block under = rail.getRelative(BlockFace.DOWN);

                if (rail.getType() == Material.POWERED_RAIL || rail.getType() == Material.RAIL) {
                    if (under.getType() == Material.OBSIDIAN) {
                        // return cart to regular max speed.
                        cart.setMaxSpeed(METERS_PER_TICK);
                    } else if (under.getType() == Material.REDSTONE_BLOCK) {
                        cart.setVelocity(cart.getVelocity().multiply(settings.getCart_speed()));
                        cart.setMaxSpeed(MAX_METERS_PER_TICK);
                    }
                }
            }
        }
    }

    public static void customTntExplosion(EntityExplodeEvent event) {
        if (event.getEntity().getType() == EntityType.TNT) {
            ConfigurationModel settings = SettingsHelper.getInstance().getSettings();
            // Cancel the default explosion
            event.setCancelled(true);

            // Get the location of the original explosion
            Location explosionLocation = event.getLocation();

            // Create a new explosion with a larger radius
            World world = explosionLocation.getWorld();
            if (world != null) {
                // Double the explosion size (the second parameter is the explosion power)
                world.createExplosion(explosionLocation, (float) settings.getTnt_explosion_radius()); // Default TNT explosion power is 4.0f
            }
        }
    }

    public static void infiniteArrowEnchantment(EntityShootBowEvent event) {
        Map<Enchantment, Integer> bowEnchants = Objects.requireNonNull(event.getBow()).getEnchantments();
        Entity shooter = event.getEntity();

//        if (shooter instanceof Player jugador) {
//            if (bowEnchants.containsKey(Enchantment.INFINITY)) {
//                event.setConsumeArrow(false);
//                jugador.updateInventory();
//            }
//        }
    }
}
