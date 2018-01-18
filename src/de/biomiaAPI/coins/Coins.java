package de.biomiaAPI.coins;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.mysql.MySQL;

//"deprecation" is only here to not use this class directly
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
public class Coins {

	public static int getCoins(BiomiaPlayer p) {
        return MySQL.executeQuerygetint("SELECT * FROM `BiomiaCoins` where ID = " + p.getBiomiaPlayerID(), "coins");

	}

	public static void setCoins(int coins, BiomiaPlayer bp) {
		MySQL.executeUpdate("UPDATE `BiomiaCoins` SET `coins` = " + coins + " WHERE `ID` = " + bp.getBiomiaPlayerID());
	}

	public static void takeCoins(int coins, BiomiaPlayer bp) {

		double actualCoins = bp.getCoins();

		if (actualCoins < coins) {
			bp.getPlayer().sendMessage("Du hast nicht genug BC! Dir fehlen noch " + (actualCoins - coins) + " BC!");
			return;
		}
		MySQL.executeUpdate("UPDATE `BiomiaCoins` SET `coins` = " + (actualCoins - coins) + " WHERE `ID` = "
				+ bp.getBiomiaPlayerID());

	}

	public static boolean addCoins(int coinsToAdd, BiomiaPlayer bp) {

		double actualCoins = bp.getCoins();
		return MySQL.executeUpdate("UPDATE `BiomiaCoins` SET `coins` = " + (actualCoins + coinsToAdd) + " WHERE `ID` = "
				+ bp.getBiomiaPlayerID());
	}

	public static boolean takeCoins(int coins, int ID) {
        double actualCoins = getCoins(ID);
        return !(actualCoins < coins) && MySQL.executeUpdate("UPDATE `BiomiaCoins` SET `coins` = " + (actualCoins - coins) + " WHERE `ID` = " + ID);
    }

	public static boolean addCoins(int coins, int ID) {
		return MySQL
				.executeUpdate("UPDATE `BiomiaCoins` SET `coins` = " + (getCoins(ID) + coins) + " WHERE `ID` = " + ID);
	}

	public static boolean setCoins(int coins, int ID) {
		return MySQL.executeUpdate("UPDATE `BiomiaCoins` SET `coins` = " + coins + " WHERE `ID` = " + ID);
	}

	private static int getCoins(int ID) {
        return MySQL.executeQuerygetint(("SELECT * FROM `BiomiaCoins` where ID = " + ID), "coins");

	}

}