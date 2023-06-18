package dev.ezpadaz.vanillaenhancer.Commands.TimeNow;

import dev.ezpadaz.vanillaenhancer.Commands.BaseCommand;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class TimeNowCommand {

    public TimeNowCommand() {
         new BaseCommand("timenow", 0, true) {

            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
                return null;
            }

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                long currentTime = getServer().getPlayer(sender.getName()).getWorld().getTime();
                // Formatear la hora en formato legible
                // SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                // String formattedTime = sdf.format(new Date(currentTime));

                long hours = (currentTime / 1000 + 6) % 24; // Convert to hours, add 6 to offset from 0
                long minutes = (currentTime % 1000) * 60 / 1000; // Convert to minutes
                String time = String.format("%02d:%02d", hours, minutes); // Format as HH:MM
                // Enviar el mensaje al chat
                // getServer().broadcastMessage(ChatColor.DARK_GREEN + "La hora del juego es: " + formattedTime);
                MessageHelper.send(sender, "&aLa hora del juego es: " + time);
                return true;
            }

            @Override
            public String getUsage() {
                return "/timenow";
            }
        };
    }
}
