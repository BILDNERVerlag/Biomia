package de.biomia.universal;

import java.util.HashMap;

public abstract class UniversalBiomia {

    public static final HashMap<String, String> RANK_NAMES_PREFIXES = new HashMap<>();

    public static String getRankLevel(String rank) {
        int i = 0;
        for (String prefix : RANK_NAMES_PREFIXES.keySet()) {
            if (prefix.equalsIgnoreCase(rank)) {
                return i < 10 ? "00" + i : "0" + i;
            }
            i++;
        }
        return "-1";
    }
}
