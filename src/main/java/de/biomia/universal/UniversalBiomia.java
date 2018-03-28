package de.biomia.universal;

import java.util.HashMap;
import java.util.Map;

public abstract class UniversalBiomia {

    public static final Map<String, String> RANK_NAMES_PREFIXES = new HashMap<>();

    static {
        RANK_NAMES_PREFIXES.put("unregspieler", "\u00A78");
        RANK_NAMES_PREFIXES.put("regspieler", "\u00A77");
        RANK_NAMES_PREFIXES.put("premiumeins", "\u00A7eI | ");
        RANK_NAMES_PREFIXES.put("premiumzwei", "\u00A7eII | ");
        RANK_NAMES_PREFIXES.put("premiumdrei", "\u00A7eIII | ");
        RANK_NAMES_PREFIXES.put("premiumvier", "\u00A7eIV | ");
        RANK_NAMES_PREFIXES.put("premiumfuenf", "\u00A7eV | ");
        RANK_NAMES_PREFIXES.put("premiumsechs", "\u00A7eVI | ");
        RANK_NAMES_PREFIXES.put("premiumsieben", "\u00A7eVII | ");
        RANK_NAMES_PREFIXES.put("premiumacht", "\u00A7eVIII | ");
        RANK_NAMES_PREFIXES.put("premiumneun", "\u00A76IX | ");
        RANK_NAMES_PREFIXES.put("premiumzehn", "\u00A76X | ");
        RANK_NAMES_PREFIXES.put("youtube", "\u00A74[\u00A70Y\u00A7fT\u00A74] | ");
        RANK_NAMES_PREFIXES.put("jrbuilder", "\u00A7aJrBuilder | ");
        RANK_NAMES_PREFIXES.put("builder", "\u00A72Builder | ");
        RANK_NAMES_PREFIXES.put("supporter", "\u00A7bSup | ");
        RANK_NAMES_PREFIXES.put("moderator", "\u00A73Mod | ");
        RANK_NAMES_PREFIXES.put("srbuilder", "\u00A72SrBuilder | ");
        RANK_NAMES_PREFIXES.put("srmoderator", "\u00A79SrMod | ");
        RANK_NAMES_PREFIXES.put("admin", "\u00A75Admin | ");
        RANK_NAMES_PREFIXES.put("owner", "\u00A7cOwner | ");
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
