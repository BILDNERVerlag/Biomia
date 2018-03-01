package de.biomia.api.pex;

import de.biomia.Main;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Rank {

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

    public static String getRankID(String rank) {
        for (int i = 0; i < Main.group.size(); i++)
            if (Main.group.get(i).equalsIgnoreCase(rank)) {
                if (i < 10)
                    return "00" + i;
                else
                    return "0" + i;
            }
        return "-1";
    }

    public static boolean isPremium(Player p) {
        String rank = Rank.getRank(p).toLowerCase();
        return rank.contains("premium");
    }

    public static boolean isPremium(String name) {
        String rank = Rank.getRank(name).toLowerCase();
        return rank.contains("premium");
    }

    public static int getPremiumLevel(Player p) {
        String s = Rank.getRank(p).replace("Premium", "");

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

    public static int getPremiumLevel(String name) {
        String s = Rank.getRank(name).replace("Premium", "");

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
        return Main.prefixes.get(getRank(p));
    }

    public static String getPrefix(String s) {
        return Main.prefixes.get(s);
    }
}