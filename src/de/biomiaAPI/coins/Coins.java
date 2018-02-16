package de.biomiaAPI.coins;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.achievements.statEvents.CoinEvent;
import de.biomiaAPI.mysql.MySQL;
import org.bukkit.Bukkit;

public class Coins {

    public static int getCoins(BiomiaPlayer p) {
        return MySQL.executeQuerygetint("SELECT * FROM `BiomiaCoins` where ID = " + p.getBiomiaPlayerID(), "coins", MySQL.Databases.biomia_db);
    }

    public static void setCoins(int coins, BiomiaPlayer bp) {
        MySQL.executeUpdate("UPDATE `BiomiaCoins` SET `coins` = " + coins + " WHERE `ID` = " + bp.getBiomiaPlayerID(), MySQL.Databases.biomia_db);
    }

    public static void takeCoins(int coins, BiomiaPlayer bp) {

        int actualCoins = bp.getCoins();

        if (actualCoins < coins) {
            bp.getPlayer().sendMessage("Du hast nicht genug BC! Dir fehlen noch " + (actualCoins - coins) + " BC!");
        }
        CoinEvent coinEvent = new CoinEvent(coins, false, bp);
        Bukkit.getServer().getPluginManager().callEvent(coinEvent);
        if (!coinEvent.isCancelled() && !(actualCoins < coins))
            setCoins(actualCoins - coins, bp);

    }

    //return true if not cancelled
    public static boolean addCoins(int coinsToAdd, BiomiaPlayer bp) {
        CoinEvent coinEvent = new CoinEvent(coinsToAdd, false, bp);
        Bukkit.getServer().getPluginManager().callEvent(coinEvent);
        if (!coinEvent.isCancelled()) {
            setCoins(bp.getCoins() + coinsToAdd, bp);
            bp.getPlayer().sendMessage("\u00A77Du erh\u00e4ltst \u00A7f" + coinsToAdd + "\u00A77 BC!");
            return true;
        }
        return false;
    }

    private static int getCoins(int ID) {
        return MySQL.executeQuerygetint(("SELECT * FROM `BiomiaCoins` where ID = " + ID), "coins", MySQL.Databases.biomia_db);

    }

}

