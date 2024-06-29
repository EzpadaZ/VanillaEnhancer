package dev.ezpadaz.vanillaenhancer.Listeners.Entity.Dep;

import dev.aurelium.auraskills.api.event.skill.XpGainEvent;
import dev.ezpadaz.vanillaenhancer.Commands.DoubleXP.Helper.XPEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AureliumSk implements Listener {

    @EventHandler
    public void onPlayerSkillExpChange(XpGainEvent event) {
        XPEvent.checkForSkillXpChange(event);
    }
}
