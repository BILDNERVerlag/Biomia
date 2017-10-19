package de.biomiaAPI.coins;

import java.util.UUID;

import org.bukkit.entity.Player;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.mysql.MySQL;

@Deprecated
public class Coins {

	public static int getCoins(Player p) {
		int i = MySQL.executeQuerygetint(
				"SELECT * FROM `player_money` where uuid = '" + p.getUniqueId().toString() + "'", "money");
		return i;

	}

	public static boolean setCoins(int money, BiomiaPlayer bp) {
		return MySQL.executeUpdate("UPDATE `player_money` SET `money` = " + money + " WHERE `uuid` = '"
				+ bp.getPlayer().getUniqueId().toString() + "'");
	}

	public static boolean takeCoins(int money, BiomiaPlayer bp) {

		double coins = bp.getCoins();

		if (money > coins) {
			bp.getPlayer().sendMessage("Du hast nicht genug BC's! Dir fehlen noch " + (money - coins) + " BC's!");
			return false;
		}
		return MySQL.executeUpdate("UPDATE `player_money` SET `money` = " + (coins - money) + " WHERE `uuid` = '"
				+ bp.getPlayer().getUniqueId().toString() + "'");

	}

	public static boolean addCoins(int money, BiomiaPlayer bp) {

		double coins = bp.getCoins();

		return MySQL.executeUpdate("UPDATE `player_money` SET `money` = " + (coins + money) + " WHERE `uuid` = '"
				+ bp.getPlayer().getUniqueId().toString() + "'");
	}

	public static boolean takeCoins(int money, UUID uuid) {

		double coins = getCoins(uuid);

		if (money > coins) {
			return false;
		}
		return MySQL.executeUpdate(
				"UPDATE `player_money` SET `money` = " + (coins - money) + " WHERE `uuid` = '" + uuid + "'");

	}

	public static boolean addCoins(int money, UUID uuid) {
		return MySQL.executeUpdate(
				"UPDATE `player_money` SET `money` = " + (getCoins(uuid) + money) + " WHERE `uuid` = '" + uuid + "'");
	}

	public static boolean setCoins(int money, UUID uuid) {
		return MySQL.executeUpdate("UPDATE `player_money` SET `money` = " + money + " WHERE `uuid` = '" + uuid + "'");

	}

	public static int getCoins(UUID uuid) {

		int i = MySQL.executeQuerygetint(("SELECT * FROM `player_money` where uuid = '" + uuid + "'"), "money");
		return i;

	}

}