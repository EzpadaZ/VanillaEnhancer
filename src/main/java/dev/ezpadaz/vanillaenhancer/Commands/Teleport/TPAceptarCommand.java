package dev.ezpadaz.vanillaenhancer.Commands.Teleport;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import dev.ezpadaz.vanillaenhancer.Utils.InventoryHelper;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("aceptar|a|si")
@Description("Acepta el viaje que tengas pendiente")
public class TPAceptarCommand extends BaseCommand {

    @Default
    public void onTripAccept(Player sender) {
        TeleportCommander commander = TeleportCommander.getInstance();
        TeleportDAO executor = commander.getPlayerRequest(sender.getName());

        if (executor == null) {
            MessageHelper.send(sender, "&cNo tenias ninguna peticion pendiente!.");
            return;
        }

        Player origen = GeneralUtils.getPlayer(executor.getOrigen());
        Player target = GeneralUtils.getPlayer(sender.getName());

        if (origen == null) {
            MessageHelper.send(sender, "&cEl usuario que origino esta peticion no existe o no se encuentra.");
            return;
        }

        if (target == null) {
            MessageHelper.send(sender, "&cEsta peticion no existe o no se encuentra.");
            return;
        }

        if (!origen.getInventory().contains(TeleportCommander.getInstance().MATERIAL_TYPE, TeleportCommander.getInstance().MATERIAL_COST)) {
            MessageHelper.send(sender, "&cHe cancelado el proceso ya que no se pudo cobrar el precio de viaje.");
            MessageHelper.send(origen, "&c%s &6no me robes perro!".formatted(origen.getName()));
            return;
        }

        if (executor.isGoingTo()) {
            Location location = target.getLocation();
            commander.teleportPlayer(origen, target, location, commander.TELEPORT_DELAY);
        } else {
            Location location = origen.getLocation();
            commander.teleportPlayer(target, origen, location, commander.TELEPORT_DELAY);
        }

        new InventoryHelper().removeItems(origen, TeleportCommander.getInstance().MATERIAL_TYPE, TeleportCommander.getInstance().MATERIAL_COST);
        commander.removePlayerRequest(target.getName(), true);
    }
}
