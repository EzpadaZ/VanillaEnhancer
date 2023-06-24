package dev.ezpadaz.vanillaenhancer.Utils;


import org.bukkit.entity.Player;

public class EffectHelper {
    private static EffectHelper instance;

    private EffectHelper() {
    }

    public static EffectHelper getInstance() {
        if (instance == null) instance = new EffectHelper();
        return instance;
    }

    public void strikeLightning(Player jugador) {
        jugador.getWorld().strikeLightningEffect(jugador.getLocation());
    }
}
