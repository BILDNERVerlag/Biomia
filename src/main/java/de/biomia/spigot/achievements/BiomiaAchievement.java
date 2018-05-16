package de.biomia.spigot.achievements;

import de.biomia.universal.MySQL;

import java.util.ArrayList;
import java.util.HashMap;

public enum BiomiaAchievement {
    VerdieneFuenftausendCoins, Nimm20Fallschaden, OeffneZehnTruhen;

    static final HashMap<BiomiaStat, ArrayList<Achievement>> stats = new HashMap<>();
    private static boolean log = false;

    /**
     * Immer, wenn sich der Wert eines Stats aendert, checkt diese Methode, ob ein
     * Achievement unlocked werden soll
     */
    public static void checkForAchievementUnlocks(BiomiaStat stat, int biomiaID, int value) {
        ArrayList<Achievement> achievements = stats.get(stat);

        if (achievements != null) {
            for (Achievement each : achievements) {
                if (each.getComment() != null) {
                    int i = stat.get(biomiaID, each.getComment());
                    if (i >= each.getTargetValue()) {
                        unlock(each.getAchievement(), biomiaID);
                    }
                } else if (value >= each.getTargetValue()) {
                    unlock(each.getAchievement(), biomiaID);
                }
            }
        }
    }

    /**
     * Versucht ein bestimmtes Achievement fuer einen bestimmten Spieler zu unlocken;
     * bricht ab, falls der Spieler das Achievement bereits hat. Gibt true zurueck,
     * falls ein Achievement unlocked wird (ansonsten false).
     */
    private static void unlock(BiomiaAchievement bA, int biomiaID) {
        MySQL.executeUpdate(String.format("INSERT INTO `%s` (`ID`) VALUES (%d)", bA.toString(), biomiaID), MySQL.Databases.achiev_db);
    }

    public static boolean logSwitch() {
        log = !log;
        return log;
    }

    public static boolean isLogging() {
        return log;
    }

    public static String toDayTime(String s) {
        switch (s = s.toUpperCase()) {
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
                s = "DAY";
        }
        return s;
    }
}
