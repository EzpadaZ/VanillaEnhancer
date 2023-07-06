package dev.ezpadaz.vanillaenhancer.Commands.Admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.entity.Player;

@CommandAlias("os")
@Description("VE Commander Panel")
public class Commander extends BaseCommand {

    public Commander() {
        GeneralUtils.registerCommand(new WatcherCommander());
        GeneralUtils.registerCommand(new SettingsCommander());
    }

    @Subcommand("about")
    public void onAbout(Player player){
        // print about info
        MessageHelper.send(player, "&6Desarrollado por: &cMolthor226");
        MessageHelper.send(player, "&6Version: &c"+ VanillaEnhancer.getInstance().getDescription().getVersion());
    }
}
