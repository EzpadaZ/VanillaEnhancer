package dev.ezpadaz.vanillaenhancer.Commands;

import dev.ezpadaz.vanillaenhancer.Commands.Admin.TestCommand;
import dev.ezpadaz.vanillaenhancer.Commands.DoubleXP.XPCommand;
import dev.ezpadaz.vanillaenhancer.Commands.Teleport.AceptarCommand;
import dev.ezpadaz.vanillaenhancer.Commands.Teleport.VenCommand;
import dev.ezpadaz.vanillaenhancer.Commands.Teleport.VoyCommand;

public class CommandBootloader {
    public CommandBootloader() {
        new XPCommand();
        new TestCommand();
        new VoyCommand();
        new VenCommand();
        new AceptarCommand();
    }
}
