package de.biomiaAPI.achievements;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.mysql.MySQL;
import org.bukkit.Bukkit;

public class Stats {

    static HashMap<BiomiaStat, ArrayList<BiomiaAchievement>> stats = new HashMap<>();

    public enum BiomiaStat {
        CoinsAccumulated,
        LoginsGeneral, LoginsQuestServer, LoginsFreebuildServer, LoginsBauServer,
        MysteryChestsOpened, SkyWarsGamesPlayed, BedWarsGamesPlayed
    }

    /**
     * Zaehle einen bestimmten Stat eines bestimmten Spielers um 1 hoch.
     */
    public static void incrementStat(BiomiaStat stat, int biomiaPlayerID) {
        incrementStatBy(stat, biomiaPlayerID, 1);
    }

    /**
     * Zaehle einen bestimmten Stat eines bestimmten Spielers um einen uebergebenen
     * Wert hoch.
     */
    public static void incrementStatBy(BiomiaStat stat, int biomiaPlayerID, int increment) {
        int value = getStat(stat, biomiaPlayerID) + increment;
        MySQL.executeUpdate("INSERT INTO `" + stat.toString() + "`(ID, value, inc) VALUES (" + biomiaPlayerID + ", " + value + ", " + increment + ")", MySQL.Databases.stats_db);
        checkForAchievementUnlocks(stat, biomiaPlayerID, value);
    }

    public static int getStat(BiomiaStat stat, int biomiaPlayerID) {
        int out = MySQL.executeQuerygetint("SELECT MAX(`value`) AS value FROM `" + stat.toString() + "` where ID = " + biomiaPlayerID, "value", MySQL.Databases.stats_db);
        return out == -1 ? 0 : out;
    }

    public static int getStatLastX(BiomiaStat stat, int biomiaPlayerID, String datetime_expr, int days) {
        switch (datetime_expr.toUpperCase()) {
            case "SECOND":
            case "MINUTE":
            case "HOUR":
            case "DAY":
            case "WEEK":
            case "MONTH":
            case "QUARTER":
            case "YEAR":
                //all good
                break;
            default:
                datetime_expr = "DAY";
        }

        int maxValue = MySQL.executeQuerygetint("SELECT MAX(`value`) AS value FROM `" + stat.toString() + "` where ID = " + biomiaPlayerID + " AND `timestamp` >= TIMESTAMPADD(" + datetime_expr + ",-" + days + ",NOW())", "value", MySQL.Databases.stats_db);
        int minValue = MySQL.executeQuerygetint("SELECT MIN(`value`) AS value FROM `" + stat.toString() + "` where ID = " + biomiaPlayerID + " AND `timestamp` >= TIMESTAMPADD(" + datetime_expr + ",-" + days + ",NOW())", "value", MySQL.Databases.stats_db);
        int minInc = MySQL.executeQuerygetint("SELECT `inc` FROM `" + stat.toString() + "` where ID = " + biomiaPlayerID + " AND value = " + minValue, "inc", MySQL.Databases.stats_db);

        int out = maxValue - minValue + minInc;
      
        return out == -1 ? 0 : out;
    }

    /**
     * Immer, wenn sich der Wert eines Stats aendert, checkt diese Methode, ob ein
     * Achievement unlocked werden soll
     */
    public static void checkForAchievementUnlocks(BiomiaStat stat, int biomiaPlayerID, int value) {
        // Step 1: Checke um welchen Stat es geht
        // Step 2: Checke ob der Stat einen bestimmten Wert erreicht hat
        // Step 3: Wenn ja, versuche Achievement zu unlocken

        ArrayList<BiomiaAchievement> achievements = stats.get(stat);
        if (achievements != null)
            achievements.forEach(each -> {
                if (value > each.getMindestWert())
                    unlock(each.getAchievement(), biomiaPlayerID);
            });
    }

    /**
     * Versucht ein bestimmtes Achievement fuer einen bestimmten Spieler zu unlocken;
     * bricht ab, falls der Spieler das Achievement bereits hat. Gibt true zurueck,
     * falls ein Achievement unlocked wird (ansonsten false).
     */
    public static void unlock(BiomiaAchievement.AchievementType bA, int biomiaPlayerID) {
        MySQL.executeUpdate("INSERT INTO `" + bA.toString() + "` (`ID`) VALUES (" + biomiaPlayerID + ")", MySQL.Databases.achiev_db);
    }


    //SELECT `inc`,`wert` FROM `TestTabelle` WHERE `time` >= TIMESTAMPADD(DAY,-3,NOW());
}
