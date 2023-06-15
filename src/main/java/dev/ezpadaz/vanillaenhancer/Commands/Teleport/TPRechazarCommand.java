package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

import dev.ezpadaz.vanillaenhancer.Commands.BaseCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TPRechazarCommand {
    public TPRechazarCommand() {
        new BaseCommand("rechazar", 0, true) {
            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
                return null;
            }

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                TeleportCommander commander = TeleportCommander.getInstance();
                commander.removePlayerRequest(sender.getName());
                return true;
            }

            @Override
            public String getUsage() {
                return "/rechazar [Rechaza la solicitud de viaje pendiente (si existe)]";
            }
        };
    }
}
