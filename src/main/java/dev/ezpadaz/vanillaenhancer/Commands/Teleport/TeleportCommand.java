package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

import dev.ezpadaz.vanillaenhancer.Commands.BaseCommand;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand {
    public TeleportCommand() {

        new BaseCommand("tp", 1, true){
            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {

                Player jugador = sender.getServer().getPlayer(sender.getName());

                if(jugador == null) {
                    // que se envie un mensaje
                    return true;
                }



                return true;
            }

            @Override
            public String getUsage() {
                return "/tp {target}";
            }
        };

    }
}
