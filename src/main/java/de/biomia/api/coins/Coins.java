package de.biomia.api.coins;

import de.biomia.api.BiomiaPlayer;
import de.biomia.api.OfflineBiomiaPlayer;
import de.biomia.api.achievements.statEvents.general.CoinAddEvent;
import de.biomia.api.achievements.statEvents.general.CoinTakeEvent;
import de.biomia.api.mysql.MySQL;
import org.bukkit.Bukkit;

public class Coins {

    public static int getCoins(OfflineBiomiaPlayer bp) {
        return MySQL.executeQuerygetint("SELECT * FROM `BiomiaCoins` where ID = " + bp.getBiomiaPlayerID(), "coins", MySQL.Databases.biomia_db);
    }

    public static void setCoins(int coins, OfflineBiomiaPlayer bp) {
        MySQL.executeUpdate("UPDATE `BiomiaCoins` SET `coins` = " + coins + " WHERE `ID` = " + bp.getBiomiaPlayerID(), MySQL.Databases.biomia_db);
    }

    public static void takeCoins(int coins, OfflineBiomiaPlayer bp) {

        int actualCoins = bp.getCoins();

        if (actualCoins < coins && bp.isOnline()) {
            bp.getPlayer().sendMessage("Du hast nicht genug BC! Dir fehlen noch " + (actualCoins - coins) + " BC!");
        }
        CoinTakeEvent coinEvent = new CoinTakeEvent(bp, coins);
        Bukkit.getServer().getPluginManager().callEvent(coinEvent);
        if (!coinEvent.isCancelled() && !(actualCoins < coins))
            setCoins(actualCoins - coins, bp);

    }

    //return true if not cancelled
    public static void addCoins(int coins, OfflineBiomiaPlayer bp) {
        CoinAddEvent coinEvent = new CoinAddEvent(bp, coins);
        Bukkit.getServer().getPluginManager().callEvent(coinEvent);
        if (!coinEvent.isCancelled()) {
            setCoins(bp.getCoins() + coins, bp);
            bp.getPlayer().sendMessage("\u00A77Du erh\u00e4ltst \u00A7f" + coins + "\u00A77 BC!");
        }
    }
}

