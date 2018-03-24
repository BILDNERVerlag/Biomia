package de.biomia.spigot.achievements;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.universal.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Stats {

    static final HashMap<BiomiaStat, ArrayList<Achievements>> stats = new HashMap<>();

    private static boolean log = false;

    public enum BiomiaStat {

        EasterRewardsErhalten,
        CoinsAccumulated, CoinsSpent,
        Logins,
        MinutesPlayed,
        MysteryChestsOpened,
        BlocksPlaced, BlocksDestroyed,
        MonstersKilled, PlayersKilled,
        HealthLost, HealthRegenerated, HungerLost, HungerRegenerated,
        DeathCause, KilledByMonster, KilledByPlayer,
        MessagesSent,
        ItemsEnchanted, ItemsPickedUp, ItemsDropped, ItemsBroken,
        ChestsOpened,
        EXPGained,
        ProjectilesShot,
        FishCaught,
        FoodEaten, PotionsConsumed,
        KilometresRun,
        SheepsSheared,
        TeleportsMade,
        GadgetsUsed, HeadsUsed, ParticlesUsed, SuitsUsed, PetsUsed,
        ReportsMade,
        SW_GamesPlayed, BW_GamesPlayed,
        SW_Deaths, SW_Wins, SW_Kills, SW_Leaves, SW_ChestsOpened, KitsBought, KitsChanged, KitsShown,
        BW_Deaths, BW_Wins, BW_Kills, BW_Leaves, BW_ItemsBought, BW_DestroyedBeds, BW_ShopUsed,
        Q_accepted, Q_returned, Q_NPCTalks, Q_CoinsEarned, Q_Kills, Q_Deaths,
        Bau_PlotsClaimed, Bau_PlotsReset,
        FB_CBClaimed, FB_CBUnclaimed, FB_ItemsBought, FB_ItemsSold, FB_WarpsUsed,

        //Events

        SpecialEggsFound, BW_ItemsBoughtNames, BW_FinalKills, BW_FinalDeaths, EasterEggsFound
    }

    /**
     * Zaehle einen bestimmten Stat eines bestimmten Spielers um 1 hoch.
     */
    public static void incrementStat(BiomiaStat stat, int biomiaID) {
        incrementStatBy(stat, biomiaID, 1);
    }

    /**
     * Zaehle einen bestimmten Stat eines bestimmten Spielers um einen uebergebenen
     * Wert hoch.
     */
    public static void incrementStatBy(BiomiaStat stat, int biomiaID, int increment) {
        int value = getStat(stat, biomiaID) + increment;
        MySQL.executeUpdate("INSERT INTO `" + stat.toString() + "`(ID, value, inc) VALUES (" + biomiaID + ", " + value + ", " + increment + ")", MySQL.Databases.stats_db);
        checkForAchievementUnlocks(stat, biomiaID, value);
    }

    public static void incrementStat(BiomiaStat stat, int biomiaID, String comment) {
        int value = getStat(stat, biomiaID) + 1;
        MySQL.executeUpdate("INSERT INTO `" + stat.toString() + "`(ID, value, comment) VALUES (" + biomiaID + ", " + value + ", '" + comment + "')", MySQL.Databases.stats_db);
        checkForAchievementUnlocks(stat, biomiaID, value);
    }

    public static void incrementStat(BiomiaStat stat, Player player, String comment) {
        int biomiaID = Biomia.getBiomiaPlayer(player).getBiomiaPlayerID();
        incrementStat(stat, biomiaID, comment);
    }

    public static void incrementStatBy(BiomiaStat stat, Player player, int increment) {
        int biomiaID = Biomia.getBiomiaPlayer(player).getBiomiaPlayerID();
        incrementStatBy(stat, biomiaID, increment);
    }

    public static void incrementStat(BiomiaStat stat, Player player) {
        int biomiaID = Biomia.getBiomiaPlayer(player).getBiomiaPlayerID();
        incrementStatBy(stat, biomiaID, 1);
    }

    public static HashMap<String, Integer> getComments(BiomiaStat stat, int biomiaID) {
        //SELECT `comment` FROM tabelle WHERE ID = biomiaID
        HashMap<String, Integer> output = new HashMap<>();
        Connection con = MySQL.Connect(MySQL.Databases.stats_db);
        if (con != null)
            try {
                PreparedStatement statement = con.prepareStatement("SELECT `comment` FROM `" + stat.toString() + "` where ID = ?");
                statement.setInt(1, biomiaID);
                ResultSet rs = statement.executeQuery();
                String comment;
                while (rs.next()) {
                    comment = rs.getString("comment");
                    output.put(comment, output.computeIfAbsent(comment, j -> 0) + 1);
                }

            } catch (SQLException ignored) {
            }
        return output;
    }

    public static int getStat(BiomiaStat stat, Player player) {
        int out = MySQL.executeQuerygetint("SELECT MAX(`value`) AS value FROM `" + stat.toString() + "` where ID = " + Biomia.getBiomiaPlayer(player).getBiomiaPlayerID(), "value", MySQL.Databases.stats_db);
        return out == -1 ? 0 : out;
    }

    public static int getStat(BiomiaStat stat, int biomiaID) {
        int out = MySQL.executeQuerygetint("SELECT MAX(`value`) AS value FROM `" + stat.toString() + "` where ID = " + biomiaID, "value", MySQL.Databases.stats_db);
        return out == -1 ? 0 : out;
    }

    public static int getStat(BiomiaStat stat, Player player, String comment) {
        int out = MySQL.executeQuerygetint("SELECT MAX(`value`) AS value FROM `" + stat.toString() + "` where ID = " + Biomia.getBiomiaPlayer(player).getBiomiaPlayerID() + " AND comment = '" + comment + "'", "value", MySQL.Databases.stats_db);
        return out == -1 ? 0 : out;
    }

    public static int getStat(BiomiaStat stat, int biomiaID, String comment) {
        int out = MySQL.executeQuerygetint("SELECT MAX(`value`) AS value FROM `" + stat.toString() + "` where ID = " + biomiaID + " AND comment = '" + comment + "'", "value", MySQL.Databases.stats_db);
        return out == -1 ? 0 : out;
    }

    public static int getStatLastX(BiomiaStat stat, int biomiaID, String datetime_expr, int amount) {
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

        Connection con = MySQL.Connect(MySQL.Databases.stats_db);
        int minValue = 0, minInc = 0, maxValue = 0;
        if (con != null) {
            boolean withComment = false;
            try {
                PreparedStatement statement = con.prepareStatement("SELECT `value`, `inc` FROM `" + stat.toString() + "` where ID = ? AND `timestamp` >= TIMESTAMPADD(" + datetime_expr + ",-?,NOW())");
                statement.setInt(1, biomiaID);
                //statement.setString(2, datetime_expr);
                statement.setInt(2, amount);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    maxValue = minValue = rs.getInt("value");
                    minInc = rs.getInt("inc");
                }
                if (!rs.isLast()) {
                    if (rs.last()) {
                        maxValue = rs.getInt("value");
                    }
                }


            } catch (SQLException e) {
                if (e.getMessage().contains("Unknown column")) {
                    withComment = true;
                } else {
                    e.printStackTrace();
                }
            }
            if (withComment) {
                try {
                    PreparedStatement statement = con.prepareStatement("SELECT `value` FROM `" + stat.toString() + "` where ID = ? AND `timestamp` >= TIMESTAMPADD(" + datetime_expr + ",-?,NOW())");
                    statement.setInt(1, biomiaID);
                    //statement.setString(2, datetime_expr);
                    statement.setInt(2, amount);
                    ResultSet rs = statement.executeQuery();

                    if (rs.next()) {
                        maxValue = minValue = rs.getInt("value");
                    }
                    if (!rs.isLast()) {
                        if (rs.last()) {
                            maxValue = rs.getInt("value");
                        }
                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (minValue == 0) {
                    return 0;
                }
                return maxValue - (minValue - 1);
            } else {
                return maxValue - (minValue - minInc);
            }
        }
        return 0;
    }

    /**
     * Immer, wenn sich der Wert eines Stats aendert, checkt diese Methode, ob ein
     * Achievement unlocked werden soll
     */
    private static void checkForAchievementUnlocks(BiomiaStat stat, int biomiaID, int value) {
        ArrayList<Achievements> achievements = stats.get(stat);

        StringBuilder out = new StringBuilder();

        out.append("\n\u00A77<\u00A7cChecking \u00A77").append(stat).append(" \u00A7cfor \u00A7b").append(BiomiaPlayer.getName(biomiaID)).append("\u00A77,\n");
        if (achievements != null) {
            out.append("\u00A77-\u00A7c# of Achievements: \u00A7b").append(achievements.size()).append("\u00A77,\n");
            for (Achievements each : achievements) {
                if (each.getComment() != null) {
                    if (Stats.getComments(stat, biomiaID).get(each.getComment()) >= each.getTargetValue()) {
                        if (unlock(each.getAchievement(), biomiaID)) {
                            out.append("\u00A77-\u00A7cComment is \u00A77'\u00A7b").append(each.getComment()).append("\u00A77' (\u00A7c").append(Stats.getComments(stat, biomiaID).get(each.getComment())).append("\u00A77>=\u00A7c").append(each.getTargetValue()).append("\u00A77),\n");
                            out.append("\u00A77-\u00A7cUnlocked \u00A7b").append(each.getAchievement().name()).append("\u00A77>");
                        } else out.append("\u00A77-\u00A7cNo new unlocks (already unlocked).\u00A77>");
                    } else {
                        out.append("\u00A77-\u00A7cComment is \u00A77'\u00A7b").append(each.getComment()).append("\u00A77' (\u00A7c").append(Stats.getComments(stat, biomiaID).get(each.getComment())).append("\u00A77<=\u00A7c").append(each.getTargetValue()).append("\u00A77),\n");
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

    //SELECT `inc`,`wert` FROM `TestTabelle` WHERE `time` >= TIMESTAMPADD(DAY,-3,NOW());

    public static void logSwitch() {
        log = !log;
    }

    public static boolean isLogging() {
        return log;
    }
}
