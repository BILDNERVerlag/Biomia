package de.biomia.spigot.tools;

import de.biomia.universal.UniversalBiomia;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class RankManager {

    public static String getRank(Player p) {
        PermissionUser user = PermissionsEx.getUser(p);
        String[] groups = user.getGroupsNames();
        return groups[0];
    }

    public static String getRank(String s) {
        PermissionUser user = PermissionsEx.getUser(s);
        String[] groups = user.getGroupsNames();
        return groups[0];
    }

    public static void setRank(Player p, String rank) {
        PermissionUser user = PermissionsEx.getUser(p);
        String[] groups = {rank};
        user.setGroups(groups);
    }

    public static void setRank(String playerName, String rank) {
        PermissionUser user = PermissionsEx.getUser(playerName);
        String[] groups = {rank};
        user.setGroups(groups);
    }

    public static boolean isPremium(String name) {
        String rank = RankManager.getRank(name).toLowerCase();
        return rank.contains("premium");
    }

    public static int getPremiumLevel(String name) {
        String s = RankManager.getRank(name).replace("Premium", "");

        switch (s) {
        case "Eins":
            return 1;
        case "Zwei":
            return 2;
        case "Drei":
            return 3;
        case "Vier":
            return 4;
        case "Fuenf":
            return 5;
        case "Sechs":
            return 6;
        case "Sieben":
            return 7;
        case "Acht":
            return 8;
        case "Neun":
            return 9;
        case "Zehn":
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