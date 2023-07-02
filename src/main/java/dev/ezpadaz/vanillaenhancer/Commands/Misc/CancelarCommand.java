package dev.ezpadaz.vanillaenhancer.Commands.Misc;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import org.bukkit.entity.Player;

@CommandAlias("cancelar|vc")
@Description("Cancela cualquier operacion pendiente")
public class CancelarCommand extends BaseCommand {
    @Default
    @Subcommand("viaje|v")
    public static void onTripCancel(Player jugador, String[] args) {
        // nada
        MessageHelper.send(jugador, "Simon.");
    }
}


