package dev.ezpadaz.vanillaenhancer.Commands.Admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;

@CommandAlias("test|ts")
@Description("Function Test Command")
public class TestCommand extends BaseCommand {
    @Default
    public void test(Player jugador, String[] args){}
}
