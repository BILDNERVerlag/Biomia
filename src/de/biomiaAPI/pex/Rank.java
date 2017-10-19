package de.biomiaAPI.pex;

import org.bukkit.entity.Player;

import de.biomiaAPI.main.Main;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Rank {

	@SuppressWarnings("deprecation")
	public static String getRank(Player p) {
		PermissionUser user = PermissionsEx.getUser(p);
		String[] groups = user.getGroupsNames();
		String group = groups[0];
		return group;
	}

	@SuppressWarnings("deprecation")
	public static String getRank(String s) {
		PermissionUser user = PermissionsEx.getUser(s);
		String[] groups = user.getGroupsNames();
		String group = groups[0];
		return group;
	}

	@SuppressWarnings("deprecation")
	public static boolean setRank(Player p, String rank) {
		PermissionUser user = PermissionsEx.getUser(p);
		String[] groups = { rank };
		user.setGroups(groups);
		return true;
	}

	@SuppressWarnings("deprecation")
	public static boolean setRank(String playerName, String rank) {
		PermissionUser user = PermissionsEx.getUser(playerName);
		String[] groups = { rank };
		user.setGroups(groups);
		return true;
	}	
	
	public static String getRankID(String rank) {
		for (int i = 0; i < Main.group.size(); i++) {

			if (Main.group.get(i).equalsIgnoreCase(rank)) {

				if (i < 10)
					return "00" + i;
				else
					return "0" + i;

			}
		}
		return "-1";

	}

	public static boolean isPremium(String s) {
		String rank = Rank.getRank(s);

		if (!rank.contains("Premium"))
			return true;
		return false;
	}

	public static int getPremiumLevel(String s) {
		String rank = Rank.getRank(s);

		rank = rank.replaceAll("Premium", "");

		switch (rank) {
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
}
