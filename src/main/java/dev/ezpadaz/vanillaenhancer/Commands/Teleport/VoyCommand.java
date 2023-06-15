package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

import dev.ezpadaz.vanillaenhancer.Commands.BaseCommand;
import org.bukkit.command.CommandSender;

public class VoyCommand {
    public VoyCommand() {
        new BaseCommand("voy", 1, false){
            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                return false;
            }

            @Override
            public String getUsage() {
                return "";
            }
        };
    }
}
