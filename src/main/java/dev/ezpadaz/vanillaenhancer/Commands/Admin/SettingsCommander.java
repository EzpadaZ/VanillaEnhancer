package dev.ezpadaz.vanillaenhancer.Commands.Admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import dev.ezpadaz.vanillaenhancer.Utils.SettingsHelper;
import org.bukkit.entity.Player;

@CommandAlias("os")
@Subcommand("settings|s")
@Description("VE Commander Panel")
public class SettingsCommander extends BaseCommand {
    @Subcommand("reload|r")
    public void settingsReload(Player jugador) {
        if (jugador.isOp() || jugador.getName().equalsIgnoreCase("Molthor226")) {
            SettingsHelper.getInstance().clearSettingCache();
            MessageHelper.send(jugador, "&6Se ha reiniciado la cache de ajustes.");
        } else {
            MessageHelper.send(jugador, "&cNo te conozco.");
        }
    }
}
