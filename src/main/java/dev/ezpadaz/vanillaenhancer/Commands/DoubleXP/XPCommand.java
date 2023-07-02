package dev.ezpadaz.vanillaenhancer.Commands.DoubleXP;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import dev.ezpadaz.vanillaenhancer.Commands.DoubleXP.Helper.XPEvent;
import dev.ezpadaz.vanillaenhancer.Utils.ExperienceHelper;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;

@CommandAlias("dxp|exp")
@Description("Administra el modulo de doble experiencia")
public class XPCommand extends BaseCommand {
    @Subcommand("enable|e|on|activate")
    @Description("Activa la doble experiencia")
    public void onEnableCommand(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            // only allow enable by console.
            if (XPEvent.isEnabled) {
                MessageHelper.console("Double XP Event was already active.");
            } else {
                XPEvent.isEnabled = true;
                MessageHelper.console("Double XP Event enabled!");
            }
        } else {
            if (sender instanceof Player p) {
                MessageHelper.send(p, "This is a console-command only.");
            }
        }
    }

    @Subcommand("disable|d|off|deactivate")
    @Description("Desactiva la doble experiencia")
    public void onDisableCommand(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            // only allow disable by console.
            if (XPEvent.isEnabled) {
                XPEvent.isEnabled = false;
                MessageHelper.console("Double XP Event disabled!");
                XPEvent.bannedPlayers = new ArrayList<>();
            }
        } else {
            if (sender instanceof Player p) {
                MessageHelper.send(p, "This is a console-command only.");
            }
        }
    }

    @Subcommand("status|s")
    @Description("Obtiene el estado actual de la doble experiencia")
    public void onStatusCommand(Player jugador) {
        MessageHelper.send(jugador, "Double XP Event: &c" + XPEvent.isEnabled);
    }

    @Subcommand("get|ver|g")
    @Description("Obtiene tu experiencia actual")
    public void onGetCommand(Player jugador) {
        int totalExpPoints = ExperienceHelper.getPlayerExp(jugador);
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedExp = formatter.format(totalExpPoints);
        MessageHelper.send(jugador, "Tienes: " + formattedExp + " EXP");
    }

    @Subcommand("optin|oi")
    @Description("Te muestra cuanta experiencia ganas")
    public void onOptIn(Player jugador) {
        if (XPEvent.optedInPlayers.contains(jugador.getName())) {
            MessageHelper.send(jugador, "You are already receiving XP Updates");
        } else {
            XPEvent.optedInPlayers.add(jugador.getName());
            MessageHelper.send(jugador, "You have opted-in to receive XP Updates");
        }
    }

    @Subcommand("optout|ou")
    @Description("Deja de mostrarte cuanta experiancia ganas.")
    public void onOptOut(Player jugador) {
        if (XPEvent.optedInPlayers.contains(jugador.getName())) {
            XPEvent.optedInPlayers.remove(jugador.getName());
            MessageHelper.send(jugador, "You have opted out of receiving XP Updates");
        } else {
            MessageHelper.send(jugador, "You can't opt-out if you are not receiving updates already.");
        }
    }

    @Subcommand("wipecd|wcd")
    @Description("Contacta al adminsitrador para mas informacion.")
    public void onWipeCD(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            // only allow disable by console.
            XPEvent.bannedPlayers = new ArrayList<>();
            MessageHelper.console("Wiped banned players from 2XP Event");
        } else {
            if (sender instanceof Player p) {
                MessageHelper.send(p, "This is a console-command only.");
            }
        }
    }
}
