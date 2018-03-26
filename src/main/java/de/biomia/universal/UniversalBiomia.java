package de.biomia.universal;

import java.util.HashMap;
import java.util.Map;

public abstract class UniversalBiomia {

    public static final Map<String, String> RANK_NAMES_PREFIXES = new HashMap<>();

    static {
        RANK_NAMES_PREFIXES.put("UnregSpieler", "\u00A78");
        RANK_NAMES_PREFIXES.put("RegSpieler", "\u00A77");
        RANK_NAMES_PREFIXES.put("PremiumEins", "\u00A7eI | ");
        RANK_NAMES_PREFIXES.put("PremiumZwei", "\u00A7eII | ");
        RANK_NAMES_PREFIXES.put("PremiumDrei", "\u00A7eIII | ");
        RANK_NAMES_PREFIXES.put("PremiumVier", "\u00A7eIV | ");
        RANK_NAMES_PREFIXES.put("PremiumFuenf", "\u00A7eV | ");
        RANK_NAMES_PREFIXES.put("PremiumSechs", "\u00A7eVI | ");
        RANK_NAMES_PREFIXES.put("PremiumSieben", "\u00A7eVII | ");
        RANK_NAMES_PREFIXES.put("PremiumAcht", "\u00A7eVIII | ");
        RANK_NAMES_PREFIXES.put("PremiumNeun", "\u00A76IX | ");
        RANK_NAMES_PREFIXES.put("PremiumZehn", "\u00A76X | ");
        RANK_NAMES_PREFIXES.put("YouTube", "\u00A74[\u00A70Y\u00A7fT\u00A74] | ");
        RANK_NAMES_PREFIXES.put("Builder", "\u00A72Builder | ");
        RANK_NAMES_PREFIXES.put("Supporter", "\u00A7bSup | ");
        RANK_NAMES_PREFIXES.put("Moderator", "\u00A73Mod | ");
        RANK_NAMES_PREFIXES.put("SrModerator", "\u00A79SrMod | ");
        RANK_NAMES_PREFIXES.put("JrBuilder", "\u00A7aJrBuilder | ");
        RANK_NAMES_PREFIXES.put("SrBuilder", "\u00A72SrBuilder | ");
        RANK_NAMES_PREFIXES.put("Admin", "\u00A75Admin | ");
        RANK_NAMES_PREFIXES.put("Owner", "\u00A7cOwner | ");
    }

    public static int getRankLevel(String rank) {
        int i = 1;
        for (String prefix : RANK_NAMES_PREFIXES.keySet()) {
            if (prefix.equalsIgnoreCase(rank)) {
                return i;
            }
            i++;
        }
        return -1;
    }
}
