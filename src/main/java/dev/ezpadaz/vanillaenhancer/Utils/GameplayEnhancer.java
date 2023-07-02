package dev.ezpadaz.vanillaenhancer.Utils;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.ChiseledBookshelf;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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

    public static void infiniteArrowEnchantment(EntityShootBowEvent event) {
        Map<Enchantment, Integer> bowEnchants = Objects.requireNonNull(event.getBow()).getEnchantments();
        Entity shooter = event.getEntity();

        if (shooter instanceof Player jugador) {
            if (bowEnchants.containsKey(Enchantment.ARROW_INFINITE)) {
                event.setConsumeItem(false);
                jugador.updateInventory();
            }
        }
    }
}
