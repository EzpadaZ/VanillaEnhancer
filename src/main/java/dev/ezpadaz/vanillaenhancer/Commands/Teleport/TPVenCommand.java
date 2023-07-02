package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


@CommandAlias("ven|traer")
@Description("Trae a un jugador a tu ubicacion.")
public class TPVenCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onTripRequest(Player origen, String[] args) {
        TeleportCommander commander = TeleportCommander.getInstance();
        Player target = Bukkit.getPlayer(args[0]);

        if (origen == null) {
            MessageHelper.send(origen, "??????????????");
            return;
        }

        if (target == null) {
            MessageHelper.send(origen, "&cEl jugador no esta en linea o no existe.");
            return;
        }

        if (origen.getName().equals(target.getName())) {
            MessageHelper.send(origen, "&cNo puedes mandarte esta solicitud a ti mismo.");
            return;
        }

        if (!origen.getInventory().contains(commander.MATERIAL_TYPE, commander.MATERIAL_COST)) {
            MessageHelper.send(origen, "&6No tienes &c%s &3%s &6para realizar el viaje :-(".formatted(commander.MATERIAL_COST, commander.MATERIAL_NAME));
            return;
        }

        if (!commander.getTravelEnabled()) {
            MessageHelper.send(origen, "&cLos viajes se encuentran desactivados de momento, contacta al administrador :-)");
            return;
        }

        commander.addPlayerRequest(target.getName(), new TeleportDAO(origen.getName(), target.getName(), false));
        String peticionText = "&c%s &6quiere que vayas hacia su ubicacion, escribe &a/aceptar&6/&a/si&6/&a/a&6 en los siguientes &c%s segundos &6para autorizar esta peticion.".formatted(origen.getName(), commander.REQUEST_TIMEOUT);
        MessageHelper.send(target, peticionText);
        MessageHelper.send(origen, "&6Solicitud enviada!\n\nEsto tendra un costo de: &c%s &3%s &6cuando &c%s &6acepte el proceso.".formatted(commander.MATERIAL_COST, commander.MATERIAL_NAME, target.getName()));
    }

    @Subcommand("cancelar|c")
    public void onTripCancel(Player origen) {
        TeleportCommander.getInstance().cancelTrip(origen.getName());
    }
}
