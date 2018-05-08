package de.biomia.spigot.achievements;

import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.universal.BiomiaIDCantBeMinusOneException;
import de.biomia.universal.MySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
    MySQLConnections,

    //Events

    SpecialEggsFound, BW_ItemsBoughtNames, BW_FinalKills, BW_FinalDeaths, EasterEggsFound,
    SchnitzelFound, BooksFound, SchnitzelMonsterKilled, SchnitzelDiedByMonster;


    /**
     * Zaehle einen bestimmten Stat eines bestimmten Spielers um einen uebergebenen
     * Wert hoch.
     */
    public void increment(int biomiaID, int increment, String comment) {
        if (biomiaID == -1) {
            new BiomiaIDCantBeMinusOneException().printStackTrace();
            return;
        }
        if (increment == 0) return;
        int value = get(biomiaID, comment) + increment;
        MySQL.executeUpdate(String.format("INSERT INTO `%s`(biomiaID, value, inc%s) VALUES (%d, %d, %d%s)", this.name(), comment != null ? ", comment" : "", biomiaID, value, increment, comment != null ? ", '" + comment + "'" : ""), MySQL.Databases.stats_db);
        BiomiaAchievement.checkForAchievementUnlocks(this, biomiaID, value);
    }

    public ArrayList<Integer> getBiomiaIDSWhereValueIsX(int x, String comment) {

        ArrayList<Integer> leaderboard = new ArrayList<>();

        Connection con = MySQL.Connect(MySQL.Databases.stats_db);
        if (con != null) {
            try {
                PreparedStatement statement = con.prepareStatement(String.format("SELECT biomiaID, value FROM ( SELECT biomiaID, COUNT(`value`) AS value FROM %s GROUP BY biomiaID ) as l WHERE value = ? %s ORDER BY biomiaID DESC", name(), comment != null ? ("AND comment = '" + comment + "'") : ""));
                statement.setInt(1, x);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    leaderboard.add(rs.getInt("biomiaID"));
                }
                rs.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return leaderboard;
    }

    public HashMap<Integer, Integer> getTop(int topX, String comment) {
        HashMap<Integer, Integer> leaderboard = new HashMap<>();

        Connection con = MySQL.Connect(MySQL.Databases.stats_db);
        if (con != null) {
            try {
                PreparedStatement statement = con.prepareStatement(String.format("SELECT biomiaID, COUNT(`value`) AS value FROM %s%s GROUP BY biomiaID ORDER BY value DESC %s", this.name(), comment == null ? "" : ("WHERE comment = '" + comment + "'"), topX != -1 ? "LIMIT " + topX : ""));
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    leaderboard.put(rs.getInt("biomiaID"), rs.getInt("value"));
                }
                rs.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return leaderboard;
    }

    public int get(int biomiaID, String comment) {
        int out = MySQL.executeQuerygetint(String.format("SELECT MAX(`value`) AS value FROM `%s` where biomiaID = %d%s", name(), biomiaID, comment != null ? " AND comment = '" + comment + "'" : ""), "value", MySQL.Databases.stats_db);
        return out == -1 ? 0 : out;
    }

    public int getLastX(int biomiaID, String datetime_expr, int amount) {
        datetime_expr = BiomiaAchievement.toDayTime(datetime_expr);

        Connection con = MySQL.Connect(MySQL.Databases.stats_db);
        int minValue = 0, minInc = 0, maxValue = 0;
        if (con != null) {
            try {
                PreparedStatement statement = con.prepareStatement(String.format("SELECT `value`, `inc` FROM `%s` where biomiaID = ? AND `timestamp` >= TIMESTAMPADD(%s,-?,NOW())", name(), datetime_expr));
                statement.setInt(1, biomiaID);
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
                e.printStackTrace();
            }
            return maxValue - (minValue - minInc);
        }
        return 0;
    }

    public Date getFirstIncrementDate(OfflineBiomiaPlayer bp) {
        Date time = null;
        Connection con = MySQL.Connect(MySQL.Databases.stats_db);
        if (con != null) {
            try {
                PreparedStatement statement = con.prepareStatement(String.format("SELECT timestamp FROM %s Where biomiaID = ?", name()));
                statement.setInt(1, bp.getBiomiaPlayerID());
                ResultSet rs = statement.executeQuery();
                if (rs.next())
                    time = Timestamp.valueOf(rs.getString("timestamp"));
                rs.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return time;
    }

    public Date getLastIncrementDate(OfflineBiomiaPlayer bp) {
        Date time = null;
        Connection con = MySQL.Connect(MySQL.Databases.stats_db);
        if (con != null) {
            try {
                PreparedStatement statement = con.prepareStatement(String.format("SELECT timestamp FROM %s Where biomiaID = ? ORDER BY timestamp DESC", name()));
                statement.setInt(1, bp.getBiomiaPlayerID());
                ResultSet rs = statement.executeQuery();
                if (rs.next())
                    time = Timestamp.valueOf(rs.getString("timestamp"));
                rs.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return time;
    }

    public HashMap<String, Integer> getComments(int biomiaID) {
        HashMap<String, Integer> output = new HashMap<>();
        Connection con = MySQL.Connect(MySQL.Databases.stats_db);
        if (con != null)
            try {
                PreparedStatement statement = con.prepareStatement(String.format("SELECT `comment`, COUNT(`value`) as value FROM %s WHERE biomiaID = ? GROUP BY `comment`", name()));
                statement.setInt(1, biomiaID);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    output.put(rs.getString("comment"), rs.getInt("value"));
                }
            } catch (SQLException ignored) {
            }
        return output;
    }
}
