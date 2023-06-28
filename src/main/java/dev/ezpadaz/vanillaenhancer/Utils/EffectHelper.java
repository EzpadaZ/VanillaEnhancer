package dev.ezpadaz.vanillaenhancer.Utils;


import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.SmokeEffect;
import de.slikey.effectlib.effect.SphereEffect;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class EffectHelper {
    private static EffectHelper instance;
    private static EffectManager manager;

    private EffectHelper() {
        manager = new EffectManager(VanillaEnhancer.getInstance());
    }

    public static EffectHelper getInstance() {
        if (instance == null) instance = new EffectHelper();
        return instance;
    }

    public void strikeLightning(Player jugador) {
        jugador.getWorld().strikeLightningEffect(jugador.getLocation());
    }

    public void smokeEffect(Player jugador) {
        SphereEffect se = new SphereEffect(manager);
        se.setEntity(jugador);
        se.iterations = 2*20; // 20 ticks = 1 sec, * 2 = 2 seconds.
        se.particles *= 2;
        se.radius *= 2;
        se.particleOffsetY = -2.0f;
        se.yOffset = -2.0f;
        se.particle = Particle.CAMPFIRE_COSY_SMOKE;
        se.start();
    }
}
