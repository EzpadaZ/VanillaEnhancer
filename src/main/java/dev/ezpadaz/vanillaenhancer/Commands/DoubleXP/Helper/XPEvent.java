package dev.ezpadaz.vanillaenhancer.Commands.DoubleXP.Helper;

import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import dev.ezpadaz.vanillaenhancer.VanillaEnhancer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class XPEvent {
    public static boolean isEnabled = false;

    public static ArrayList<String> optedInPlayers = new ArrayList<String>();
    public static ArrayList<String> bannedPlayers = new ArrayList<>();

    public static HashMap<String, Integer> doubleXpDeathCount = new HashMap<>();

    public static void checkForExpChange(PlayerExpChangeEvent event){
        int multiplier = Objects.requireNonNull(VanillaEnhancer.getInstance().config.getConfigurationSection("double-xp")).getInt("multiplier");

        if (multiplier == 0) {
            // In case default value is 0 (null)
            multiplier = 2;
        }

        int obtainedExp = event.getAmount();
        int doubleExp = obtainedExp * multiplier;
        Player jugador = event.getPlayer();
        boolean logEnabled = Objects.requireNonNull(VanillaEnhancer.getInstance().config.getConfigurationSection("double-xp")).getBoolean("log");

        if (isEnabled) {
            if (bannedPlayers.contains(jugador.getName())) {
                if (logEnabled && optedInPlayers.contains(jugador.getName())) {
                    MessageHelper.send(jugador, "You received: " + doubleExp + " XP (Reduced)");
                }
                event.setAmount(obtainedExp);
            } else {
                if (logEnabled && optedInPlayers.contains(jugador.getName())) {
                    MessageHelper.send(jugador, "You received: " + doubleExp + " (x" + multiplier + ") XP");
                }
                event.setAmount(doubleExp);
            }
        } else {
            if (logEnabled && optedInPlayers.contains(jugador.getName()))
                MessageHelper.send(event.getPlayer(), "You received: " + obtainedExp + " XP");
            event.setAmount(obtainedExp);
        }
    }

    public static void checkForPlayerDeath(PlayerDeathEvent event){
        if (isEnabled) {
            int deathCooldown = Objects.requireNonNull(VanillaEnhancer.getInstance().config.getConfigurationSection("double-xp")).getInt("death-cooldown");
            // if double XP is enabled we must cool down the player if he dies more than 3 times when the event is enabled.
            Player jugador = event.getEntity();
            Integer deathCount = doubleXpDeathCount.get(jugador.getName());

            if (deathCount == null) {
                deathCount = 1;
            }

            doubleXpDeathCount.put(jugador.getName(), deathCount + 1);

            if (deathCount >= deathCooldown) {
                bannedPlayers.add(jugador.getName());
                MessageHelper.send(jugador, "You died too much on 2XP Day, You are no longer participating.");
            }
        }
    }
}
