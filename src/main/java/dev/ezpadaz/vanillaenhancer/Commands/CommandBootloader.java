package dev.ezpadaz.vanillaenhancer.Commands;

import dev.ezpadaz.vanillaenhancer.Commands.Admin.TestCommand;
import dev.ezpadaz.vanillaenhancer.Commands.DoubleXP.XPCommand;
import dev.ezpadaz.vanillaenhancer.Commands.Teleport.*;

public class CommandBootloader {
    public CommandBootloader() {
        new XPCommand();
        new TestCommand();
        TeleportCommander.getInstance().setupCommander();
    }
}
