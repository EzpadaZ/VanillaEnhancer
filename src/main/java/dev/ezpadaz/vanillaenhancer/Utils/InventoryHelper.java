package dev.ezpadaz.vanillaenhancer.Utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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


    public void removeItems(Player player, Material type, int amount) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack[] inventory = playerInventory.getContents();

        int tempAmount = amount;

        for (ItemStack item : inventory) {
            // this itemstack matches the itemType.
            if (item != null && item.getType() == type) {
                int itemAmount = item.getAmount(); // check how many items it has.

                if (itemAmount > tempAmount) {
                    // This itemstack has more items of the ones im looking for.
                    item.setAmount(itemAmount - tempAmount);
                    tempAmount = 0;
                } else {
                    playerInventory.remove(item);
                    tempAmount -= itemAmount;
                }
            }
        }

    }
    // Remove X amount of items from player inventory.
    // Add X Amount of Items to Player Inventory.
}
