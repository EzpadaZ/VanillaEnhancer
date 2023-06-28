package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

import dev.ezpadaz.vanillaenhancer.Commands.BaseCommand;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static dev.ezpadaz.vanillaenhancer.VanillaEnhancer.getInstance;

public class TPVoyCommand {
    public TPVoyCommand() {
        new BaseCommand("voy", 1, false) {
            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
                List<String> list = new ArrayList<>();
                Collection test = VanillaEnhancer.getInstance().getServer().getOnlinePlayers();
                for (Object player : test) {
                    if (player instanceof Player) {
                        Player spigotPlayer = (Player) player;
                        list.add(spigotPlayer.getName());
                    }
                }
                return list;
            }

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {

                Player target = Bukkit.getPlayer(arguments[0]);
                Player origen = Bukkit.getPlayer(sender.getName());

                if (origen == null) {
                    MessageHelper.send(origen, "??????????????");
                    return true;
                }

                if (target == null) {
                    MessageHelper.send(origen, "&cEl jugador no esta en linea o no existe.");
                    return true;
                }

                if (origen.getName().equals(target.getName())) {
                    MessageHelper.send(origen, "&cNo puedes mandarte esta solicitud a ti mismo.");
                    return true;
                }

                if (!origen.getInventory().contains(TeleportCommander.getInstance().MATERIAL_TYPE, TeleportCommander.getInstance().MATERIAL_COST)) {
                    MessageHelper.send(origen, "&6No tienes &c%s &3%s &6para realizar el viaje :-(".formatted(TeleportCommander.getInstance().MATERIAL_COST, TeleportCommander.getInstance().MATERIAL_NAME));
                    return true;
                }

                TeleportCommander commander = TeleportCommander.getInstance();
                commander.addPlayerRequest(target.getName(), new TeleportDAO(origen.getName(), target.getName(), true));

                String peticionText = "&c%s &6quiere ir hacia tu ubicacion, escribe &a/aceptar&6 en los siguientes &c%s segundos &6para autorizar esta peticion.".formatted(sender.getName(), commander.REQUEST_TIMEOUT);
                MessageHelper.send(target, peticionText);
                MessageHelper.send(origen, "&6Solicitud enviada!\nEsto tendra un costo de: &c%s &3%s &6cuando &c%s &6acepte el proceso.".formatted(TeleportCommander.getInstance().MATERIAL_COST, TeleportCommander.getInstance().MATERIAL_NAME, target.getName()));
                return true;
            }

            @Override
            public String getUsage() {
                return "/voy {nombre} [Te lleva a ti padrino al sujeto que especifiques.]";
            }
        };
    }
}
