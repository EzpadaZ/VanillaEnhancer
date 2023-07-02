package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import org.bukkit.entity.Player;


@CommandAlias("teleportcommander|tc")
@Description("Tools for managing the TeleportCommander")
public class TCCommand extends BaseCommand {

    @Subcommand("reload|r")
    @Description("Reloads Configuration for the Teleport Commander")
    public static void onReloadCommand(Player jugador) {
        if (jugador.isOp()) {
            TeleportCommander.getInstance().configure();
            MessageHelper.send(jugador, "&6Se ha recargado la configuracion del TeleportCommander.");
        } else {
            MessageHelper.send(jugador, "&cTu quien eres?");
        }
    }
}
