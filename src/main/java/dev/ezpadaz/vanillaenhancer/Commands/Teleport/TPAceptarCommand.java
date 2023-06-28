package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

import dev.ezpadaz.vanillaenhancer.Commands.BaseCommand;
import dev.ezpadaz.vanillaenhancer.Utils.EffectHelper;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import dev.ezpadaz.vanillaenhancer.Utils.InventoryHelper;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

public class TPAceptarCommand {
    public TPAceptarCommand() {
        new BaseCommand("aceptar", 0, true) {
            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
                return null;
            }

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                TeleportCommander commander = TeleportCommander.getInstance();
                TeleportDAO executor = commander.getPlayerRequest(sender.getName());

                if (executor == null) {
                    MessageHelper.send(sender, "&cNo tenias ninguna peticion pendiente!.");
                    return true;
                }

                Player origen = GeneralUtils.getPlayer(executor.getOrigen());
                Player target = GeneralUtils.getPlayer(sender.getName());

                if (origen == null) {
                    MessageHelper.send(sender, "&cEl usuario que origino esta peticion no existe o no se encuentra.");
                    return true;
                }

                if (target == null) {
                    MessageHelper.send(sender, "&cEsta peticion no existe o no se encuentra.");
                    return true;
                }

                if (!origen.getInventory().contains(TeleportCommander.getInstance().MATERIAL_TYPE, TeleportCommander.getInstance().MATERIAL_COST)) {
                    MessageHelper.send(sender, "&cHe cancelado el proceso ya que no se pudo cobrar el precio de viaje.");
                    MessageHelper.send(origen, "&c%s &6no me robes perro!".formatted(origen.getName()));
                    return true;
                }

                if (executor.isGoingTo()) {
                    Location location = target.getLocation();
                    EffectHelper.getInstance().smokeEffect(origen);
                    commander.teleportPlayer(origen, target, location, commander.TELEPORT_DELAY);
                } else {
                    Location location = origen.getLocation();
                    EffectHelper.getInstance().smokeEffect(target);
                    commander.teleportPlayer(target, origen, location, commander.TELEPORT_DELAY);
                }

                new InventoryHelper().removeItems(origen, TeleportCommander.getInstance().MATERIAL_TYPE, TeleportCommander.getInstance().MATERIAL_COST);
                commander.removePlayerRequest(target.getName(), true);
                return true;
            }

            @Override
            public String getUsage() {
                return "/aceptar [Acepta la solicitud de viaje que tengas en tu pinche cola]";
            }
        };
    }
}
