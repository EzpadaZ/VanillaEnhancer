package dev.ezpadaz.vanillaenhancer.Utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryHelper {
    // Operations related to the inventory.

    // Return true if player has X amount of item in inventory.
    public boolean hasItem(Player jugador, Material type, int amount) {
        if (jugador == null) return false;

        boolean found = jugador.getInventory().contains(type, amount);
        if (found) {
            MessageHelper.consoleDebug("Found " + type + " in " + jugador.getName() + " inventory.");
        }
        return found;
    }

    // Remove X amount of items from player inventory.
    // Add X Amount of Items to Player Inventory.
}
