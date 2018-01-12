package de.biomiaAPI.achievements;

import java.util.Date;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.mysql.MySQL;

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
     * BiomiaPlayerID, String timestamp (wann es unlocked wurde, standardm��ig -1)
     */

    private enum BiomiaStat {
        QuestServerLogins, CoinsAllTime
    }

    /**
     * Gib einem bestimmten Spieler einen bestimmten Wert in einem bestimmten Stat
     */
    private static void saveStat(BiomiaStat stat, BiomiaPlayer bp, int value) {
        MySQL.executeUpdate("UPDATE `BiomiaStat" + stat.toString() + "` SET `value` = " + value + " WHERE `ID` = " + bp.getBiomiaPlayerID());
        checkForAchievementUnlocks(stat, bp, value);
    }

    /**
     * Z�hle einen bestimmten Stat eines bestimmten Spielers um 1 hoch.
     */
    public static void incrementStat(BiomiaStat stat, BiomiaPlayer bp) {
        saveStat(stat, bp, getStat(stat, bp) + 1);
    }

    /**
     * Z�hle einen bestimmten Stat eines bestimmten Spielers um einen beliebigen
     * Wert hoch.
     */
    public static void incrementStatBy(BiomiaStat stat, BiomiaPlayer bp, int increment) {
        saveStat(stat, bp, getStat(stat, bp) + increment);
    }

    private static int getStat(BiomiaStat stat, BiomiaPlayer bp) {
        return MySQL.executeQuerygetint("SELECT * FROM `BiomiaStat" + stat.toString() + "` where ID = " + bp.getBiomiaPlayerID(), "value");
    }

    /**
     * Immer, wenn sich der Wert eines Stats �ndert, checkt diese Methode, ob ein
     * Achievement unlocked werden soll
     */
    private static void checkForAchievementUnlocks(BiomiaStat stat, BiomiaPlayer bp, int value) {
        // Step 1: Checke um welchen Stat es geht
        // Step 2: Checke ob der Stat einen bestimmten Wert erreicht hat
        // Step 3: Wenn ja, versuche Achievement zu unlocken
        switch (stat) {
            case CoinsAllTime:
                if (value > 5000)
                    tryToUnlock(BiomiaAchievement.VerdieneFuenftausendCoins, bp);
                break;
            case QuestServerLogins:
                if (value > 4)
                    tryToUnlock(BiomiaAchievement.LogDichFuenfmalAufDemQuestServerEin, bp);
                break;
        }
    }

    /**
     * Versucht ein bestimmtes Achievement f�r einen bestimmten Spieler zu unlocken;
     * bricht ab, falls der Spieler das Achievement bereits hat. Gibt true zur�ck,
     * falls ein Achievement unlocked wird (ansonsten false).
     */
    private static void tryToUnlock(BiomiaAchievement bA, BiomiaPlayer bp) {
        if (!hasAchievement(bA, bp))
            MySQL.executeUpdate("INSERT INTO `BiomiaAchievement" + bA.toString() + "` (`ID`, `timestamp`) VALUES (" + bp.getBiomiaPlayerID() + ", " + new Date().toString() + ")");
    }

    private static boolean hasAchievement(BiomiaAchievement bA, BiomiaPlayer bp) {
        return (MySQL.executeQuery("SELECT * FROM `BiomiaAchievement" + bA.toString() + "` where ID = " + bp.getBiomiaPlayerID(), "`ID`") != null);
    }

}
