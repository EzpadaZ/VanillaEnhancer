package dev.ezpadaz.vanillaenhancer.Commands.Admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import dev.ezpadaz.vanillaenhancer.Utils.Overseer.Overseer;
import dev.ezpadaz.vanillaenhancer.Utils.SettingsHelper;
import org.bukkit.entity.Player;

@CommandAlias("commander")
@Description("VE Commander Panel")
public class Commander extends BaseCommand {

    @Subcommand("settings")
    @Description("Settings Commander")
    public class SettingsCommander extends BaseCommand {
        public void settingsReload(Player jugador) {
            if (jugador.isOp()) {
                SettingsHelper.getInstance().clearSettingCache();
                MessageHelper.send(jugador, "&6Se ha reiniciado la cache de ajustes.");
            }
        }
    }

    @Subcommand("ov")
    @Description("Overseer Commander")
    public class OverseerCommander extends BaseCommand {
        @Subcommand("reload")
        public void osReload(Player jugador) {
            if (jugador.isOp()) {
                Overseer.getInstance().reloadOverseer();
                MessageHelper.send(jugador, "&6Se ha reiniciado el overseer.");
            }
        }

        @Subcommand("panic")
        @Description("Causa un OS TPS Panic")
        public void osPanic(Player jugador) {
            if (jugador.isOp()) {
                Overseer.getInstance().panic(6.66);
                MessageHelper.send(jugador, "&cSe ha activado el kernel panic manual.");
            }
        }
    }
}
