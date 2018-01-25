package de.biomiaAPI.achievements;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.mysql.MySQL;

public class Stats {

    static HashMap<BiomiaStat, ArrayList<BiomiaAchievement>> stats = new HashMap<>();

    /*
     * Idee ist folgende:
     *
     * Jeder Stat hat eine eigene Tabelle in folgendem Format:
     *
     * Tabellenname = BiomiaStatName (zB BiomiaStatCoinsAllTime)
     *
     * BiomiaPlayerID, int value
     *
     * Jedes Achievement hat eine eigene Tabelle in der Datenbank in folgendem
     * Format:
     *
     * Tabellenname = BiomiaAchievementName (zB
     * BiomiaAchievementVerdieneFuenftausendCoins)
     *
     * BiomiaPlayerID
     */

    public enum BiomiaStat {
        CoinsAccumulated,
        NumberOfLoginsGeneral, NumberOfLoginsQuestServer, NumberOfLoginsFreebuildServer, NumberOfLoginsBauServer,
        MysterChestsOpened, SkyWarsGamesPlayed, BedWarsGamesPlayed
    }

    /**
     * Gib einem bestimmten Spieler einen bestimmten Wert in einem bestimmten Stat
     */
    public static void saveStat(BiomiaStat stat, int biomiaPlayerID, int value) {
        MySQL.executeUpdate("INSERT INTO `" + stat.toString() + "`(ID, value) VALUES (" + biomiaPlayerID + ", " + value + ") ON DUPLICATE KEY UPDATE value = " + value, MySQL.Databases.stats_db);
        checkForAchievementUnlocks(stat, biomiaPlayerID, value);
    }

    /**
     * Zaehle einen bestimmten Stat eines bestimmten Spielers um 1 hoch.
     */
    public static void incrementStat(BiomiaStat stat, int biomiaPlayerID) {
        saveStat(stat, biomiaPlayerID, getStat(stat, biomiaPlayerID) + 1);
    }

    /**
     * Zaehle einen bestimmten Stat eines bestimmten Spielers um einen beliebigen
     * Wert hoch.
     */
    public static void incrementStatBy(BiomiaStat stat, int biomiaPlayerID, int increment) {
        saveStat(stat, biomiaPlayerID, getStat(stat, biomiaPlayerID) + increment);
    }

    public static int getStat(BiomiaStat stat, int biomiaPlayerID) {
        int out = MySQL.executeQuerygetint("SELECT * FROM `" + stat.toString() + "` where ID = " + biomiaPlayerID, "value", MySQL.Databases.stats_db);
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

}
