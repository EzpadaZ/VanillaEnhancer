package dev.ezpadaz.vanillaenhancer.Utils;

import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageHelper {
    public static void send(CommandSender sender, String message) {
        send(sender, message, "&6[&5VE&6]&f ");
    }

    public static void send(CommandSender sender, String message, String prefix) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }

    public static void global(String message){
        for(Player jugador: Bukkit.getServer().getOnlinePlayers()){
            send(jugador, message);
        }
    }

    public static void consoleDebug(String message) {
        boolean isDebugEnabled = VanillaEnhancer.getInstance().config.getBoolean("debug");

        if (isDebugEnabled) console(message);
    }

    public static void console(String message) {
        send(VanillaEnhancer.getInstance().getServer().getConsoleSender(), "&6[&5VE&6]&f " + message, "&a");
    }
}
