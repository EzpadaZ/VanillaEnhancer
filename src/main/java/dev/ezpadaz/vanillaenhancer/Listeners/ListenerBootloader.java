package dev.ezpadaz.vanillaenhancer.Listeners;

import dev.ezpadaz.vanillaenhancer.Listeners.Events.DoubleXPEvent;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.Bukkit;

public class ListenerBootloader {

    public ListenerBootloader() {
        Bukkit.getPluginManager().registerEvents(new DoubleXPEvent(), VanillaEnhancer.getInstance());
    }
}
