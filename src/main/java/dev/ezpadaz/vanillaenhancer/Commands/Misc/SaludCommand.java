package dev.ezpadaz.vanillaenhancer.Commands.Misc;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.archyx.aureliumskills.api.AureliumAPI;
import dev.ezpadaz.vanillaenhancer.Utils.DependencyHelper;
import dev.ezpadaz.vanillaenhancer.Utils.GeneralUtils;
import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("salud")
public class SaludCommand extends BaseCommand {
    @Default
    public void obtenerSalud(Player jugador) {
        double vidaActual = jugador.getHealth(); //paper MC
        double maxVida = jugador.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double currentMana = (DependencyHelper.hasAureliumSkills) ? AureliumAPI.getMana(jugador) : 0.0;
        float currentSaturation = jugador.getSaturation();
        int currentHunger = jugador.getFoodLevel();

        List<String> mensajes = new ArrayList<>();
        mensajes.add("Salud actual: &f" + GeneralUtils.formatDouble(vidaActual) + " - &6Vida Maxima: &f" + GeneralUtils.formatDouble(maxVida));
        mensajes.add("Mana actual: &f" + currentMana);
        mensajes.add("Saturacion actual: &f" + currentSaturation);
        mensajes.add("Hambre actual: &f" + currentHunger);
        MessageHelper.sendMultipleMessage(jugador, "&bInformacion que cura", "INFO", mensajes);
    }

}
