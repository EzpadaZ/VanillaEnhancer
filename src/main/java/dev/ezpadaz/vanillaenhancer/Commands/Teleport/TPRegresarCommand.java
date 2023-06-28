package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

import dev.ezpadaz.vanillaenhancer.Commands.BaseCommand;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import dev.ezpadaz.vanillaenhancer.Utils.InventoryHelper;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TPRegresarCommand {
    public TPRegresarCommand() {
        new BaseCommand("regresar", 0, true) {
            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
                return null;
            }

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                TeleportCommander commander = TeleportCommander.getInstance();
                Player jugador = GeneralUtils.getPlayer(sender.getName());
                String subcmd;
                if (arguments != null && arguments.length > 0) {
                    subcmd = arguments[0];
                } else {
                    subcmd = "";
                }

                switch (subcmd) {
                    case "cancelar":
                        if(commander.getPreviousLocation(sender.getName()) != null){
                            if(jugador != null){
                                commander.removeLastLocation(sender.getName());
                                MessageHelper.send(jugador, "&6He cancelado tu ultima ubicacion.");
                            }
                        }else{
                            if(jugador != null){

                                MessageHelper.send(jugador, "&cNo hay nada que cancelar :v.");
                            }
                        }
                        return true;
                    default:
                        if (commander.getPreviousLocation(sender.getName()) != null) {
                            if (jugador != null) {
                                if (InventoryHelper.hasItem(jugador, commander.MATERIAL_TYPE, commander.TRAVEL_BACK_MATERIAL_COST)) {
                                    commander.unsafeTeleportPlayer(jugador, commander.getPreviousLocation(sender.getName()), commander.TELEPORT_DELAY);
                                    commander.removeLastLocation(sender.getName());
                                    new InventoryHelper().removeItems(jugador, commander.MATERIAL_TYPE, (commander.TRAVEL_BACK_MATERIAL_COST));
                                } else {
                                    // no tiene, informarle amablemente que no tiene oro.
                                    MessageHelper.send(jugador, "&cNo me quieras ver la cara de pendejo, no tienes oro.");
                                }
                            }
                        } else {
                            MessageHelper.send(jugador, "&cNo tienes un lugar a donde volver");
                        }
                        return true;
                }
            }

            @Override
            public String getUsage() {
                return "/regresar  [Te regresa a tu ubicacion previa (de viaje)]";
            }
        };
    }
}
