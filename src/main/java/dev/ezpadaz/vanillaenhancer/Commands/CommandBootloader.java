package dev.ezpadaz.vanillaenhancer.Commands;

import dev.ezpadaz.vanillaenhancer.Commands.Admin.Commander;
import dev.ezpadaz.vanillaenhancer.Commands.Admin.TestCommand;
import dev.ezpadaz.vanillaenhancer.Commands.DoubleXP.XPCommand;
import dev.ezpadaz.vanillaenhancer.Commands.Misc.CancelarCommand;
import dev.ezpadaz.vanillaenhancer.Commands.Misc.SaludCommand;
import dev.ezpadaz.vanillaenhancer.Commands.Teleport.*;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;

import java.util.List;

public class CommandBootloader {
    public CommandBootloader() {

        GeneralUtils.registerCompleter("boolean", List.of(new String[]{"true", "false"}));

        TeleportCommander.getInstance().setupCommander();
        GeneralUtils.registerCommand(new XPCommand());
        GeneralUtils.registerCommand(new TestCommand());
        GeneralUtils.registerCommand(new CancelarCommand());
        GeneralUtils.registerCommand(new TCCommand());
        GeneralUtils.registerCommand(new Commander());
        GeneralUtils.registerCommand(new SaludCommand());


    }
}
