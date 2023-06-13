package dev.ezpadaz.vanillaenhancer.Listeners;

import dev.ezpadaz.doublexp.DoubleXP;
import org.bukkit.Bukkit;

public class ListenerBootloader {

    public ListenerBootloader() {
        Bukkit.getPluginManager().registerEvents(new DoubleXPEvent(), DoubleXP.getInstance());
    }
}
