package de.biomiaAPI.coins;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.achievements.Stats;
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
		Stats.saveStat(Stats.BiomiaStat.CoinsAllTime,bp,coins);
	}

	public static void takeCoins(int coins, BiomiaPlayer bp) {

		double actualCoins = bp.getCoins();

		if (actualCoins < coins) {
			bp.getPlayer().sendMessage("Du hast nicht genug BC! Dir fehlen noch " + (actualCoins - coins) + " BC!");
			return;
		}
        setCoins((int) (actualCoins - coins), bp);
	}

	public static boolean addCoins(int coinsToAdd, BiomiaPlayer bp) {
		return addCoins(coinsToAdd, bp.getBiomiaPlayerID());
	}

	public static boolean takeCoins(int coinsToTake, int ID) {
        int actualCoins = getCoins(ID);
        return !(actualCoins < coinsToTake) && setCoins(actualCoins-coinsToTake,ID);
    }

	public static boolean addCoins(int coinsToAdd, int ID) {
	    return setCoins(getCoins(ID)+coinsToAdd,ID);
	}

	public static boolean setCoins(int coins, int ID) {
		return MySQL.executeUpdate("UPDATE `BiomiaCoins` SET `coins` = " + coins + " WHERE `ID` = " + ID);
	}

	private static int getCoins(int ID) {
        return MySQL.executeQuerygetint(("SELECT * FROM `BiomiaCoins` where ID = " + ID), "coins");

	}

}