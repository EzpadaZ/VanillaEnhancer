package dev.ezpadaz.vanillaenhancer.Utils;

import dev.ezpadaz.vanillaenhancer.Utils.Database.Model.Config.ConfigurationModel;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MessageHelper {
    private static String PREFIX = "&6[&5VE&6]&f ";
    public static void send(CommandSender sender, String message) {
        send(sender, message, "&6[&5VE&6]&f ");
    }

    public static void send(CommandSender sender, String message, String prefix) {
        String translatedMessage = ChatColor.translateAlternateColorCodes('&', prefix + message);
        Component component = Component.text(translatedMessage).color(NamedTextColor.WHITE);
        sender.sendMessage(component);

    }

    public static void sendConsole(String message){
        String translatedMessage = ChatColor.translateAlternateColorCodes('&', PREFIX + message);
        VanillaEnhancer.getInstance().getServer().getConsoleSender().sendMessage(translatedMessage);
    }

    public static void sendMultipleMessage(CommandSender sender, String titleText, String tagText, List<String> strings) {
        String separator = "&c----------------------------------";
        String tag = tagText.equals("") ? "" : "&a[&e" + tagText + "&a]&6 ";
        String title = titleText.equals("") ? "Informacion" : titleText;

        StringBuilder builtMessage;
        builtMessage = new StringBuilder(title + "\n" + separator + "\n");
        for (String text : strings) {
            builtMessage.append(tag).append(text).append("\n");
        }
        builtMessage.append(separator);

        send(sender, builtMessage.toString());
    }

    public static void global(String message) {
        for (Player jugador : Bukkit.getServer().getOnlinePlayers()) {
            send(jugador, message);
        }
    }

    public static void sendMultipleGlobal(String titleText, String tagText, String prefix, List<String> strings) {
        String separator = "&c----------------------------------";
        String tag = tagText.equals("") ? "" : "&a[&e" + tagText + "&a]&6 ";
        String title = titleText.equals("") ? "Informacion" : titleText;

        StringBuilder builtMessage;
        builtMessage = new StringBuilder(title + "\n" + separator + "\n");
        for (String text : strings) {
            builtMessage.append(tag).append(text).append("\n");
        }
        builtMessage.append(separator);
        global(builtMessage.toString(), prefix);
    }

    public static void sendMultipleGlobal(String titleText, String tagText, List<String> strings) {
        String separator = "&c----------------------------------";
        String tag = tagText.equals("") ? "" : "&a[&e" + tagText + "&a]&6 ";
        String title = titleText.equals("") ? "Informacion" : titleText;

        StringBuilder builtMessage;
        builtMessage = new StringBuilder(title + "\n" + separator + "\n");
        for (String text : strings) {
            builtMessage.append(tag).append(text).append("\n");
        }
        builtMessage.append(separator);
        global(builtMessage.toString());
    }

    public static void global(String message, String prefix) {
        for (Player jugador : Bukkit.getServer().getOnlinePlayers()) {
            send(jugador, message, prefix);
        }
    }

    public static void consoleDebug(String message) {
        ConfigurationModel settings = SettingsHelper.getInstance().getSettings();
        boolean isDebugEnabled = settings.getDebugMode();

        if (isDebugEnabled) console(message);
    }

    public static void console(String message) {
        //send(VanillaEnhancer.getInstance().getServer().getConsoleSender(), "&6[&5VE&6]&f " + message, "&a");
        sendConsole(message);
    }
}
