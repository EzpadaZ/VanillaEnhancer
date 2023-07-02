package dev.ezpadaz.vanillaenhancer.Commands.DoubleXP.Helper;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.api.event.XpGainEvent;
import dev.ezpadaz.vanillaenhancer.Utils.Database.Model.Config.ConfigurationModel;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import dev.ezpadaz.vanillaenhancer.Utils.SettingsHelper;
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

    public static void checkForExpChange(PlayerExpChangeEvent event) {
        ConfigurationModel settings = SettingsHelper.getInstance().getSettings();
        int multiplier =  settings.getDxp_multiplier();

        if (multiplier == 0) {
            // In case default value is 0 (null)
            multiplier = 2;
        }

        int obtainedExp = event.getAmount();
        int doubleExp = obtainedExp * multiplier;
        Player jugador = event.getPlayer();

        if (isEnabled) {
            if (bannedPlayers.contains(jugador.getName())) {
                if (optedInPlayers.contains(jugador.getName())) {
                    MessageHelper.send(jugador, "You received: " + obtainedExp + " XP (Reduced)");
                }
                event.setAmount(obtainedExp);
            } else {
                if (optedInPlayers.contains(jugador.getName())) {
                    MessageHelper.send(jugador, "You received: " + doubleExp + " ("+obtainedExp+" x " + multiplier + ") XP");
                }
                event.setAmount(doubleExp);
            }
            return;
        }
        if (optedInPlayers.contains(jugador.getName()))
            MessageHelper.send(event.getPlayer(), "You received: " + obtainedExp + " XP");
        event.setAmount(obtainedExp);

    }

    public static void checkForSkillXpChange(XpGainEvent event) {
        ConfigurationModel settings = SettingsHelper.getInstance().getSettings();
        double xpAmount = event.getAmount();
        int multiplier = settings.getDxp_multiplier();
        if (multiplier == 0) {
            // In case default value is 0 (null)
            multiplier = 2;
        }

        double doubleXpAmount = xpAmount * multiplier;
        if(isEnabled){
            event.setAmount(doubleXpAmount);
        }else{
            event.setAmount(xpAmount);
        }
    }

    public static void checkForPlayerDeath(PlayerDeathEvent event) {
        ConfigurationModel settings = SettingsHelper.getInstance().getSettings();
        if (isEnabled) {
            int deathCooldown = settings.getDxp_death_cooldown();
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
