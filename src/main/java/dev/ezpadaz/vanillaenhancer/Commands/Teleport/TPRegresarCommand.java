package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import dev.ezpadaz.vanillaenhancer.Utils.InventoryHelper;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

@CommandAlias("regresar|back")
@Description("Te regresa a la ubicacion anterior de un viaje.")
public class TPRegresarCommand extends BaseCommand {

    @Default
    public void onComebackTrip(Player jugador) {
        TeleportCommander commander = TeleportCommander.getInstance();
        Location ubicacion = commander.getPreviousLocation(jugador.getName());

        if (ubicacion != null) {
            // We have an available location to get back to.
            if (InventoryHelper.hasItem(jugador, commander.MATERIAL_TYPE, commander.TRAVEL_BACK_MATERIAL_COST)) {
                commander.unsafeTeleportPlayer(jugador, ubicacion, commander.TELEPORT_DELAY);
                commander.removeLastLocation(jugador.getName());
                new InventoryHelper().removeItems(jugador, commander.MATERIAL_TYPE, commander.TRAVEL_BACK_MATERIAL_COST);
            } else {
                MessageHelper.send(jugador, "&cNo tienes como pagar culero.");
            }
        } else {
            MessageHelper.send(jugador, "&cNo tienes a donde volver.");
        }
    }

    @Subcommand("cancelar|c")
    @Description("Cancela el retorno que tienes activo.")
    public void onComebackCancel(Player jugador) {
        TeleportCommander commander = TeleportCommander.getInstance();
        Location ubicacion = commander.getPreviousLocation(jugador.getName());

        if (ubicacion != null) {
            commander.removeLastLocation(jugador.getName());
            MessageHelper.send(jugador, "&6He cancelado tu ultima ubicacion :p");
        } else {
            MessageHelper.send(jugador, "&cNo hay nada que cancelar.");
        }
    }

    @Subcommand("donde|d")
    @Description("Te muestra las coordenadas del lugar a donde regresarias")
    public void onComebackWhereTo(Player jugador) {
        TeleportCommander commander = TeleportCommander.getInstance();
        Location ubicacion = commander.getPreviousLocation(jugador.getName());
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        if (ubicacion != null) {
            MessageHelper.send(jugador, "&6Tu ultima ubicacion es: &a[&6X: &c%s&6, &6Y: &c%s&6, &6Z: &c%s&a]".formatted(
                    Double.parseDouble(decimalFormat.format(ubicacion.getX())),
                    Double.parseDouble(decimalFormat.format(ubicacion.getY())),
                    Double.parseDouble(decimalFormat.format(ubicacion.getZ()))));
        } else {
            MessageHelper.send(jugador, "&cNo tienes una ubicacion para mostrar");
        }
    }
}
