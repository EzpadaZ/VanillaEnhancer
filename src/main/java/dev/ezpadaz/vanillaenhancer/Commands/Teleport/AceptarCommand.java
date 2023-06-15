package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

import dev.ezpadaz.vanillaenhancer.Commands.BaseCommand;
import dev.ezpadaz.vanillaenhancer.Utils.InventoryHelper;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class AceptarCommand {
    public AceptarCommand() {
        new BaseCommand("aceptar", 0, true) {
            int MATERIAL_COST = 32;
            Material MATERIAL_TYPE = Material.GOLD_INGOT;
            String MATERIAL_NAME = "Lingotes de oro";

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                TeleportCommander commander = TeleportCommander.getInstance();
                TeleportDAO executor = commander.getPlayerRequest(sender.getName());

                if (executor == null) {
                    MessageHelper.send(sender, "&cNo tenias ninguna peticion pendiente!.");
                    return true;
                }

                Player origen = VanillaEnhancer.getInstance().getServer().getPlayer(executor.getOrigen());
                Player target = VanillaEnhancer.getInstance().getServer().getPlayer(sender.getName());


                if (origen == null) {
                    MessageHelper.send(sender, "&cEl usuario que origino esta peticion no existe o no se encuentra.");
                    return true;
                }

                if (target == null) {
                    MessageHelper.send(sender, "&cEsta peticion no existe o no se encuentra.");
                    return true;
                }

                if (!origen.getInventory().contains(MATERIAL_TYPE, MATERIAL_COST)) {
                    MessageHelper.send(sender, "&cHe cancelado el proceso ya que no se pudo cobrar el precio de viaje.");
                    MessageHelper.send(origen, "&c%s &6no me robes perro!".formatted(origen.getName()));
                    return true;
                }

                if (executor.isGoingTo()) {
                    Location location = target.getLocation();
                    if (commander.isSafe(location) && !target.isFlying() && !target.isGliding()) {
                        // player is safe to go to, is not flying nor gliding (some mofos, man).
                        origen.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
                    }
                } else {
                    Location location = origen.getLocation();
                    if (commander.isSafe(location) && !origen.isFlying() && !origen.isGliding()) {
                        // player is safe to go to, is not flying nor gliding (some mofos, man).
                        target.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
                    }
                }

                MessageHelper.send(sender, "&6Peticion aceptada con exito!.");
                MessageHelper.send(origen, "&c%s &6acepto tu peticion!".formatted(sender.getName()));

                new InventoryHelper().removeItems(origen, MATERIAL_TYPE, MATERIAL_COST);


                MessageHelper.send(origen, "&6Te he cobrado el costo del viaje.");
                commander.removePlayerRequest(target.getName());
                return true;
            }

            @Override
            public String getUsage() {
                return "/aceptar [Acepta la solicitud de viaje que tengas en cola]";
            }
        };
    }
}
