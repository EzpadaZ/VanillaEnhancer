package dev.ezpadaz.vanillaenhancer.Commands.Admin;

import dev.ezpadaz.vanillaenhancer.Commands.BaseCommand;
import dev.ezpadaz.vanillaenhancer.Utils.InventoryHelper;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TestCommand {
    public TestCommand() {
        new BaseCommand("test", 0, true) {

            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
                return null;
            }

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                if (sender instanceof Player p) {
                    new InventoryHelper().hasItem(p, Material.GOLD_INGOT, 32);
                }
                return true;
            }

            @Override
            public String getUsage() {
                return "Test command.";
            }
        };
    }
}
