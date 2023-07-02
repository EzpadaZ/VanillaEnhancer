package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import org.bukkit.entity.Player;

@CommandAlias("voy|ir|ircon")
@Description("Comando para viajar a un jugador.")
public class TPVoyCommand extends BaseCommand {
    @Default
    @CommandCompletion("@players")
    public void onTrip(Player origen, String[] args) {
        TeleportCommander commander = TeleportCommander.getInstance();
        if (args.length == 0) {
            MessageHelper.send(origen, "&cNecesitas especificar con quien quieres ir.");
            return;
        }

        Player target = GeneralUtils.getPlayer(args[0]);

        if (target == null) {
            MessageHelper.send(origen, "&cNo encontre a un jugador con este nombre.");
            return;
        }

        if (origen.getName().equals(args[0])) {
            MessageHelper.send(origen, "&cNo puedes viajar contigo mismo.");
            return;
        }

        // Validar inventario.
        if (!origen.getInventory().contains(commander.MATERIAL_TYPE, commander.MATERIAL_COST)) {
            MessageHelper.send(origen, "&cNo tienes el material suficiente para pagar este viaje");
            MessageHelper.send(origen, "&cEl costo es de: &6%s &3%s".formatted(commander.MATERIAL_COST, commander.MATERIAL_TYPE));
            return;
        }

        // Validar si esta activo
        if (!commander.getTravelEnabled()) {
            MessageHelper.send(origen, "&cLos viajes se encuentran desactivados de momento, contacta al administrador :-)");
            return;
        }

        commander.addPlayerRequest(target.getName(), new TeleportDAO(origen.getName(), target.getName(), true));
        String peticionText = "&c%s &6quiere ir hacia tu ubicacion, escribe &a/aceptar&6/&a/si&6/&a/a&6 en los siguientes &c%s segundos &6para autorizar esta peticion.".formatted(origen.getName(), commander.REQUEST_TIMEOUT);
        MessageHelper.send(target, peticionText);
        MessageHelper.send(origen, "&6Solicitud enviada!\nEsto tendra un costo de: &c%s &3%s &6cuando &c%s &6acepte el proceso.".formatted(commander.MATERIAL_COST, commander.MATERIAL_NAME, target.getName()));
    }

    @Subcommand("cancelar|c")
    public void onTripCancel(Player origen) {
        TeleportCommander.getInstance().cancelTrip(origen.getName());
    }
}
