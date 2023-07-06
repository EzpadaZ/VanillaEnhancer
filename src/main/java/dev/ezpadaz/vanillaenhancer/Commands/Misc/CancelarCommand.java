package dev.ezpadaz.vanillaenhancer.Commands.Misc;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import dev.ezpadaz.vanillaenhancer.Commands.Teleport.TeleportCommander;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("cancelar|vc")
@Description("Cancela cualquier operacion pendiente")
public class CancelarCommand extends BaseCommand {
    @Default
    @Subcommand("viaje|v")
    public static void onTripCancel(Player jugador) {
        // nada
        TeleportCommander.getInstance().cancelTrip(jugador.getName());
    }

    @Subcommand("regresar|back")
    public static void onBackCancel(Player jugador){
        TeleportCommander commander = TeleportCommander.getInstance();
        Location ubicacion = commander.getPreviousLocation(jugador.getName());

        if (ubicacion != null) {
            commander.removeLastLocation(jugador.getName());
            MessageHelper.send(jugador, "&6He cancelado tu ultima ubicacion :p");
        } else {
            MessageHelper.send(jugador, "&cNo hay nada que cancelar.");
        }
    }
}


