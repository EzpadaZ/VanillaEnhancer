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

@CommandAlias("salud|health|gethealth")
public class SaludCommand extends BaseCommand {
    @Default
    public void obtenerSalud(Player jugador) {
        List<String> mensajes = new ArrayList<>();
        mensajes.add("Salud: &f" + GeneralUtils.formatDouble(jugador.getHealth()) + " &6/&f " + GeneralUtils.formatDouble(jugador.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
        mensajes.add("Mana: &f" + ((DependencyHelper.hasAureliumSkills) ? AureliumAPI.getMana(jugador) : 0.0));
        mensajes.add("Saturacion: &f" + jugador.getSaturation());
        mensajes.add("Hambre: &f" + jugador.getFoodLevel());
        MessageHelper.sendMultipleMessage(jugador, "&bDatos de Jugador", "INFO", mensajes);
    }
}
