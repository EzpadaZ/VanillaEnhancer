package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import org.bukkit.entity.Player;

import java.util.List;

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

        MessageHelper.sendMultipleMessage(target, "&aUver", "INFO", List.of(new String[]{
                "&6" + origen.getName() + " quiere ir hacia ti.",
                "Escribe &a/aceptar &6/ &a/si &6/ &a/a &6 en los siguientes &c" + commander.REQUEST_TIMEOUT + " segundos &6para autorizar esta peticion.",
                "Escribe &c/no &6o &c/rechazar &6para mandarlo &calv"
        }));

        MessageHelper.sendMultipleMessage(origen, "Uber", "INFO", List.of(new String[]{
                "&6Solicitud enviada",
                "&6Tendra un costo de: &c" + commander.MATERIAL_COST + " &3" + commander.MATERIAL_NAME,
        }));
    }
}
