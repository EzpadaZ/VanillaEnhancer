package dev.ezpadaz.vanillaenhancer.Commands.DoubleXP;

import dev.ezpadaz.vanillaenhancer.Commands.BaseCommand;
import dev.ezpadaz.vanillaenhancer.Commands.DoubleXP.Helper.XPEvent;
import dev.ezpadaz.vanillaenhancer.Utils.ExperienceHelper;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class XPCommand {
    public XPCommand() {
        
        new BaseCommand("dxp", 1, false) {

            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
                String[] help = new String[]{"get","status"};
                return List.of(help);
            }

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                String cmd = arguments[0];
                Player jugador = sender.getServer().getPlayer(sender.getName());

                switch (cmd) {
                    case "enable" -> {
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
                        return true;
                    }
                    case "disable" -> {
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
                        return true;
                    }
                    case "status" -> {
                        MessageHelper.send(sender, "Double XP Event: &c" + XPEvent.isEnabled);
                        return true;
                    }
                    case "get" -> {
                        if (jugador != null) {
                            int totalExpPoints = ExperienceHelper.getPlayerExp(jugador);
                            DecimalFormat formatter = new DecimalFormat("#,###");
                            String formattedExp = formatter.format(totalExpPoints);
                            MessageHelper.send(sender, "Tienes: " + formattedExp + " EXP");
                        }
                        return true;
                    }
                    case "optin" -> {
                        if (jugador != null) {
                            if (XPEvent.optedInPlayers.contains(jugador.getName())) {
                                MessageHelper.send(jugador, "You are already receiving XP Updates");
                            } else {
                                XPEvent.optedInPlayers.add(jugador.getName());
                                MessageHelper.send(jugador, "You have opted-in to receive XP Updates");
                            }
                        }
                        return true;
                    }
                    case "optout" -> {
                        if (jugador != null) {
                            if (XPEvent.optedInPlayers.contains(jugador.getName())) {
                                XPEvent.optedInPlayers.remove(jugador.getName());
                                MessageHelper.send(jugador, "You have opted out of receiving XP Updates");
                            } else {
                                MessageHelper.send(jugador, "You can't opt-out if you are not receiving updates already.");
                            }
                        }
                        return true;
                    }
                    case "wipecd" -> {
                        if (sender instanceof ConsoleCommandSender) {
                            // only allow disable by console.
                            XPEvent.bannedPlayers = new ArrayList<>();
                            MessageHelper.console("Wiped banned players from 2XP Event");
                        } else {
                            if (sender instanceof Player p) {
                                MessageHelper.send(p, "This is a console-command only.");
                            }
                        }
                        return true;
                    }
                    default -> {
                        return true;
                    }
                }
            }

            @Override
            public String getUsage() {
                return "/dxp {get | status}";
            }
        };
    }
}
