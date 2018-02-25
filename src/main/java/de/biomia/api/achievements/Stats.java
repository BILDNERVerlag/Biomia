package de.biomia.api.achievements;

import de.biomia.api.Biomia;
import de.biomia.api.mysql.MySQL;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Stats {

    static final HashMap<BiomiaStat, ArrayList<Achievements>> stats = new HashMap<>();

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

    public static void incrementStat(BiomiaStat stat, int biomiaPlayerID, String comment) {
        int value = getStat(stat, biomiaPlayerID) + 1;
        MySQL.executeUpdate("INSERT INTO `" + stat.toString() + "`(ID, value, comment) VALUES (" + biomiaPlayerID + ", " + value + ", '" + comment + "')", MySQL.Databases.stats_db);
        checkForAchievementUnlocks(stat, biomiaPlayerID, value);
    }

    public static void incrementStat(BiomiaStat stat, Player player, String comment) {
        int biomiaPlayerID = Biomia.getBiomiaPlayer(player).getBiomiaPlayerID();
        incrementStat(stat, biomiaPlayerID, comment);
    }

    public static void incrementStatBy(BiomiaStat stat, Player player, int increment) {
        int biomiaPlayerID = Biomia.getBiomiaPlayer(player).getBiomiaPlayerID();
        incrementStatBy(stat, biomiaPlayerID, increment);
    }

    public static void incrementStat(BiomiaStat stat, Player player) {
        int biomiaPlayerID = Biomia.getBiomiaPlayer(player).getBiomiaPlayerID();
        incrementStatBy(stat, biomiaPlayerID, 1);
    }

    public static HashMap<String, Integer> getComments(BiomiaStat stat, int biomiaPlayerID) {
        //SELECT `comment` FROM tabelle WHERE ID = biomiaPlayerID
        HashMap<String, Integer> output = new HashMap<>();
        Connection con = MySQL.Connect(MySQL.Databases.stats_db);
        if (con != null)
            try {
                PreparedStatement statement = con.prepareStatement("SELECT `comment` FROM `" + stat.toString() + "` where ID = ?");
                statement.setInt(1, biomiaPlayerID);
                ResultSet rs = statement.executeQuery();
                String comment;
                while (rs.next()) {
                    comment = rs.getString("comment");
                    output.put(comment, output.computeIfAbsent(comment, j -> 0) + 1);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        return output;
    }

    public static int getStat(BiomiaStat stat, Player player) {
        int out = MySQL.executeQuerygetint("SELECT MAX(`value`) AS value FROM `" + stat.toString() + "` where ID = " + Biomia.getBiomiaPlayer(player).getBiomiaPlayerID(), "value", MySQL.Databases.stats_db);
        return out == -1 ? 0 : out;
    }

    public static int getStat(BiomiaStat stat, int biomiaPlayerID) {
        int out = MySQL.executeQuerygetint("SELECT MAX(`value`) AS value FROM `" + stat.toString() + "` where ID = " + biomiaPlayerID, "value", MySQL.Databases.stats_db);
        return out == -1 ? 0 : out;
    }

    public static int getStat(BiomiaStat stat, Player player, String comment) {
        int out = MySQL.executeQuerygetint("SELECT MAX(`value`) AS value FROM `" + stat.toString() + "` where ID = " + Biomia.getBiomiaPlayer(player).getBiomiaPlayerID() + " AND WHERE comment = '" + comment + "'", "value", MySQL.Databases.stats_db);
        return out == -1 ? 0 : out;
    }

    public static int getStat(BiomiaStat stat, int biomiaPlayerID, String comment) {
        int out = MySQL.executeQuerygetint("SELECT MAX(`value`) AS value FROM `" + stat.toString() + "` where ID = " + biomiaPlayerID + " AND WHERE comment = '" + comment + "'", "value", MySQL.Databases.stats_db);
        return out == -1 ? 0 : out;
    }

    public static int getStatLastX(BiomiaStat stat, int biomiaPlayerID, String datetime_expr, int amount) {
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
                statement.setInt(1, biomiaPlayerID);
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
                    statement.setInt(1, biomiaPlayerID);
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

                if (minValue == 0)
                    return 0;
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
    private static void checkForAchievementUnlocks(BiomiaStat stat, int biomiaPlayerID, int value) {
        // Step 1: Checke um welchen Stat es geht
        // Step 2: Checke ob der Stat einen bestimmten Wert erreicht hat
        // Step 3: Wenn ja, versuche Achievement zu unlocken

        ArrayList<Achievements> achievements = stats.get(stat);
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
    private static void unlock(Achievements.BiomiaAchievement bA, int biomiaPlayerID) {
        MySQL.executeUpdate("INSERT IGNORE INTO `" + bA.toString() + "` (`ID`) VALUES (" + biomiaPlayerID + ")", MySQL.Databases.achiev_db);
    }

    //SELECT `inc`,`wert` FROM `TestTabelle` WHERE `time` >= TIMESTAMPADD(DAY,-3,NOW());
}
