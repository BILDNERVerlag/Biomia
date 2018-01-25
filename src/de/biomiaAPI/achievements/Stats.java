package de.biomiaAPI.achievements;

import java.util.Date;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.mysql.MySQL;
import org.bukkit.Bukkit;

public class Stats {

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
     * BiomiaPlayerID, String timestamp (wann es unlocked wurde, standardmaessig -1)
     */

    public enum BiomiaStat {
        QuestServerLogins, CoinsAllTime, CoinsCurrently, MysteryBoxesOpened
    }

    /**
     * Gib einem bestimmten Spieler einen bestimmten Wert in einem bestimmten Stat
     */
    public static void saveStat(BiomiaStat stat, int biomiaPlayerID, int value) {
        MySQL.executeUpdate("INSERT INTO `BiomiaStat" + stat.toString() + "`(ID, value) VALUES (" + biomiaPlayerID + ", " + value + ") ON DUPLICATE KEY UPDATE value = " + value, MySQL.Databases.stats_db);
        //MySQL.executeUpdate("UPDATE `BiomiaStat" + stat.toString() + "` SET `value` = " + value + " WHERE `ID` = " + biomiaPlayerID, MySQL.Databases.stats_db);
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
        return MySQL.executeQuerygetint("SELECT * FROM `BiomiaStat" + stat.toString() + "` where ID = " + biomiaPlayerID, "value", MySQL.Databases.stats_db);
    }

    /**
     * Immer, wenn sich der Wert eines Stats aendert, checkt diese Methode, ob ein
     * Achievement unlocked werden soll
     */
    public static void checkForAchievementUnlocks(BiomiaStat stat, int biomiaPlayerID, int value) {
        // Step 1: Checke um welchen Stat es geht
        // Step 2: Checke ob der Stat einen bestimmten Wert erreicht hat
        // Step 3: Wenn ja, versuche Achievement zu unlocken
        switch (stat) {
            case CoinsAllTime:
                if (value > 5000)
                    tryToUnlock(BiomiaAchievement.VerdieneFuenftausendCoins, biomiaPlayerID);
                break;
            case QuestServerLogins:
                if (value > 4)
                    tryToUnlock(BiomiaAchievement.LogDichFuenfmalAufDemQuestServerEin, biomiaPlayerID);
                break;
        }
    }

    /**
     * Versucht ein bestimmtes Achievement fuer einen bestimmten Spieler zu unlocken;
     * bricht ab, falls der Spieler das Achievement bereits hat. Gibt true zurueck,
     * falls ein Achievement unlocked wird (ansonsten false).
     */
    public static void tryToUnlock(BiomiaAchievement bA, int biomiaPlayerID) {
        if (!hasAchievement(bA, biomiaPlayerID))
            MySQL.executeUpdate("INSERT INTO `BiomiaAchievement" + bA.toString() + "` (`ID`, `timestamp`) VALUES (" + biomiaPlayerID + ", " + new Date().toString() + ")", MySQL.Databases.stats_db);
    }

    public static boolean hasAchievement(BiomiaAchievement bA, int biomiaPlayerID) {
        return (MySQL.executeQuery("SELECT * FROM `BiomiaAchievement" + bA.toString() + "` where ID = " + biomiaPlayerID, "`ID`", MySQL.Databases.stats_db) != null);
    }

    public static void checkIfPlayerInTable() {

    }

}
