package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;


@CommandAlias("rechazar|reject|no")
@Description("Rechaza la peticion de otro jugador de viaje hacia ti o tu hacia el.")
public class TPRechazarCommand extends BaseCommand {
    @Default
    public void onTripReject(Player jugador){
        TeleportCommander commander = TeleportCommander.getInstance();
        commander.removePlayerRequest(jugador.getName(), false);
    }
}
