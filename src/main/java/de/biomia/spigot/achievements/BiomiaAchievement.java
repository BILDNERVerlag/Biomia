package de.biomia.spigot.achievements;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.universal.MySQL;
import org.bukkit.Bukkit;

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

        StringBuilder out = new StringBuilder();

        out.append("\n\u00A77<\u00A7cChecking \u00A77").append(stat).append(" \u00A7cfor \u00A7b").append(BiomiaPlayer.getName(biomiaID)).append("\u00A77,\n");
        if (achievements != null) {
            out.append("\u00A77-\u00A7c# of Achievement: \u00A7b").append(achievements.size()).append("\u00A77,\n");
            for (Achievement each : achievements) {
                if (each.getComment() != null) {
                    int i = stat.get(biomiaID, each.getComment());
                    if (i >= each.getTargetValue()) {
                        if (unlock(each.getAchievement(), biomiaID)) {
                            out.append("\u00A77-\u00A7cComment is \u00A77'\u00A7b").append(each.getComment()).append("\u00A77' (\u00A7c").append(i).append("\u00A77>=\u00A7c").append(each.getTargetValue()).append("\u00A77),\n");
                            out.append("\u00A77-\u00A7cUnlocked \u00A7b").append(each.getAchievement().name()).append("\u00A77>");
                        } else out.append("\u00A77-\u00A7cNo new unlocks (already unlocked).\u00A77>");
                    } else {
                        out.append("\u00A77-\u00A7cComment is \u00A77'\u00A7b").append(each.getComment()).append("\u00A77' (\u00A7c").append(i).append("\u00A77<=\u00A7c").append(each.getTargetValue()).append("\u00A77),\n");
                        out.append("\u00A77-\u00A7cNo new unlocks.\u00A77>");
                    }
                } else if (value >= each.getTargetValue()) {
                    if (unlock(each.getAchievement(), biomiaID)) {
                        out.append("\u00A77-\u00A7cNo comment. \u00A7b").append(value).append("\u00A77>=\u00A7b").append(each.getTargetValue()).append("\u00A77,\n");
                        out.append("\u00A77-\u00A7cUnlocked \u00A7b").append(each.getAchievement().name()).append("\u00A77>");
                    } else
                        out.append("\u00A77-\u00A7cNo new unlocks. (already unlocked ").append(each.getAchievement().name()).append(")\u00A77>");
                } else out.append("\u00A77-\u00A7cNo new unlocks.\u00A77>");
            }
        } else out.append("\u00A77-\u00A7cNo achievements.\u00A77>");
        if (log) Bukkit.broadcastMessage(out.toString());
    }

    /**
     * Versucht ein bestimmtes Achievement fuer einen bestimmten Spieler zu unlocken;
     * bricht ab, falls der Spieler das Achievement bereits hat. Gibt true zurueck,
     * falls ein Achievement unlocked wird (ansonsten false).
     */
    private static boolean unlock(BiomiaAchievement bA, int biomiaID) {
        return MySQL.executeUpdate("INSERT INTO `" + bA.toString() + "` (`ID`) VALUES (" + biomiaID + ")", MySQL.Databases.achiev_db);
    }

    public static void logSwitch() {
        log = !log;
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
