package de.biomia.spigot.tools;

import de.biomia.universal.UniversalBiomia;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import org.bukkit.entity.Player;

public class RankManager {

    public static LuckPermsApi api = LuckPerms.getApi();

    public static String getRank(Player p) {
        User user = api.getUser(p.getUniqueId());
        if (user != null)
            return user.getPrimaryGroup();
        return "";
    }

    public static String getRank(String s) {
        User user = api.getUser(s);
        if (user != null)
            return user.getPrimaryGroup();
        return "";
    }

    public static void setRank(Player p, String rank) {
        User user = api.getUser(p.getUniqueId());
        if (user != null)
            user.setPrimaryGroup(rank);
    }

    public static void setRank(String playerName, String rank) {
        User user = api.getUser(playerName);
        if (user != null)
            user.setPrimaryGroup(rank);
    }

    public static boolean isPremium(String name) {
        String rank = getRank(name);
        return rank.contains("premium");
    }

    public static int getPremiumLevel(String name) {
        String s = getRank(name).replace("premium", "");

        switch (s) {
        case "eins":
            return 1;
        case "zwei":
            return 2;
        case "drei":
            return 3;
        case "vier":
            return 4;
        case "fünf":
            return 5;
        case "sechs":
            return 6;
        case "sieben":
            return 7;
        case "acht":
            return 8;
        case "neun":
            return 9;
        case "zehn":
            return 10;
        default:
            return -1;
        }
    }

    public static String getPrefix(Player p) {
        return getPrefix(getRank(p));
    }

    public static String getPrefix(String s) {
        return UniversalBiomia.RANK_NAMES_PREFIXES.get(s);
    }
}