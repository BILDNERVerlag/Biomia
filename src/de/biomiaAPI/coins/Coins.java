package de.biomiaAPI.coins;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.mysql.MySQL;

//"deprecation" is only here to not use this class directly
@Deprecated
public class Coins {

	public static int getCoins(BiomiaPlayer p) {
		int i = MySQL.executeQuerygetint("SELECT * FROM `BiomiaCoins` where ID = " + p.getBiomiaPlayerID(), "coins");
		return i;

	}

	public static boolean setCoins(int coins, BiomiaPlayer bp) {
		return MySQL.executeUpdate(
				"UPDATE `BiomiaCoins` SET `coins` = " + coins + " WHERE `ID` = " + bp.getBiomiaPlayerID());
	}

	public static boolean takeCoins(int coins, BiomiaPlayer bp) {

		double actualCoins = bp.getCoins();

		if (actualCoins < coins) {
			bp.getPlayer().sendMessage("Du hast nicht genug BC's! Dir fehlen noch " + (actualCoins - coins) + " BC's!");
			return false;
		}
		return MySQL.executeUpdate("UPDATE `BiomiaCoins` SET `coins` = " + (actualCoins - coins) + " WHERE `ID` = "
				+ bp.getBiomiaPlayerID());

	}

	public static boolean addCoins(int coinsToAdd, BiomiaPlayer bp) {

		double actualCoins = bp.getCoins();
		return MySQL.executeUpdate("UPDATE `BiomiaCoins` SET `coins` = " + (actualCoins + coinsToAdd) + " WHERE `ID` = "
				+ bp.getBiomiaPlayerID());
	}

	public static boolean takeCoins(int coins, int ID) {
		double actualCoins = getCoins(ID);
		if (actualCoins < coins) {
			return false;
		}
		return MySQL
				.executeUpdate("UPDATE `BiomiaCoins` SET `coins` = " + (actualCoins - coins) + " WHERE `ID` = " + ID);
	}

	public static boolean addCoins(int coins, int ID) {
		return MySQL
				.executeUpdate("UPDATE `BiomiaCoins` SET `coins` = " + (getCoins(ID) + coins) + " WHERE `ID` = " + ID);
	}

	public static boolean setCoins(int coins, int ID) {
		return MySQL.executeUpdate("UPDATE `BiomiaCoins` SET `coins` = " + coins + " WHERE `ID` = " + ID);
	}

	public static int getCoins(int ID) {
		int i = MySQL.executeQuerygetint(("SELECT * FROM `BiomiaCoins` where ID = " + ID), "coins");
		return i;

	}

}