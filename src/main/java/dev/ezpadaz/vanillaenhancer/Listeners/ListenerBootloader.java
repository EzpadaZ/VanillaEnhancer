package dev.ezpadaz.vanillaenhancer.Listeners;

import dev.ezpadaz.vanillaenhancer.Listeners.Entity.Dep.AureliumSk;
import dev.ezpadaz.vanillaenhancer.Listeners.Entity.EntityListener;
import dev.ezpadaz.vanillaenhancer.Listeners.Entity.PlayerListener;
import dev.ezpadaz.vanillaenhancer.Utils.DependencyHelper;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;

public class ListenerBootloader {

    public ListenerBootloader() {
        GeneralUtils.registerListener(new PlayerListener());
        GeneralUtils.registerListener(new EntityListener());

        if (DependencyHelper.hasAureliumSkills) {
            GeneralUtils.registerListener(new AureliumSk());
        }
    }
}
