package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

import dev.ezpadaz.vanillaenhancer.Commands.BaseCommand;
import org.bukkit.command.CommandSender;

public class RechazarCommand {
    public RechazarCommand() {
        new BaseCommand("rechazar", 0, true) {
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
