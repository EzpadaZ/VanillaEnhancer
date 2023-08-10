package dev.ezpadaz.vanillaenhancer.Commands.DoubleXP;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import dev.ezpadaz.vanillaenhancer.Commands.DoubleXP.Helper.XPEvent;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Model.DoubleXP.DoubleXPModel;
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
        if (DoubleXPModel.getOptedPlayer(jugador.getName())) {
            MessageHelper.send(jugador, "Ya recibes esta informacion");
        } else {
            DoubleXPModel.saveOptedPlayer(jugador.getName(), true);
            MessageHelper.send(jugador, "Recibiras la informacion a partir de ahora.");
        }
    }

    @Subcommand("optout|ou")
    @Description("Deja de mostrarte cuanta experiancia ganas.")
    public void onOptOut(Player jugador) {
        if (DoubleXPModel.getOptedPlayer(jugador.getName())) {
            DoubleXPModel.saveOptedPlayer(jugador.getName(), false);
            MessageHelper.send(jugador, "Dejaras de recibir esta informacion a partir de ahora.");
        } else {
            MessageHelper.send(jugador, "No puedes dejar de recibir lo que no recibias.");
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
