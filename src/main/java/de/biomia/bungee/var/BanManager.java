package de.biomia.bungee.var;

import de.biomia.bungee.BungeeMain;
import de.biomia.bungee.OfflineBungeeBiomiaPlayer;
import de.biomia.universal.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BanManager {

    public static void getAllBans() {

        ArrayList<Bans> all = new ArrayList<>();

        try {
            PreparedStatement sql = MySQL.Connect(MySQL.Databases.biomia_db).prepareStatement("Select * from BanList");
            ResultSet rs = sql.executeQuery();
            while (rs.next()) {
                int biomiaID = rs.getInt("biomiaID");
                String reason = rs.getString("Grund");
                int timestamp = rs.getInt("timestamp");
                int length = rs.getInt("l\u00e4nge");
                boolean perm = rs.getBoolean("permanent");
                int von = rs.getInt("von");

                all.add(new Bans(perm, timestamp + length, reason, biomiaID, von, timestamp));
            }
            rs.close();
            sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        BungeeMain.activeBans = all;

    }

    public static void getAllCachedBans() {

        ArrayList<Bans> all = new ArrayList<>();
        try {
            PreparedStatement sql = MySQL.Connect(MySQL.Databases.biomia_db).prepareStatement("Select * from CachedBanList");
            ResultSet rs = sql.executeQuery();
            while (rs.next()) {
                int biomiaID = rs.getInt("biomiaID");
                String reason = rs.getString("Grund");
                int length = rs.getInt("bis");
                int timestamp = rs.getInt("timestamp");
                boolean perm = rs.getBoolean("permanent");
                int von = rs.getInt("von");

                all.add(new Bans(perm, length, reason, biomiaID, von, timestamp));
            }
            rs.close();
            sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        BungeeMain.cachedBans = all;

    }

    public static void moveToCache(Bans ban, OfflineBungeeBiomiaPlayer entbanntVon) {

        MySQL.executeUpdate("DELETE FROM `BanList` WHERE biomiaID = '" + ban.getBiomiaID() + "'", MySQL.Databases.biomia_db);
        BungeeMain.activeBans.remove(ban);

        try {
            PreparedStatement sql = MySQL.Connect(MySQL.Databases.biomia_db).prepareStatement(
                    "Insert into CachedBanList (`biomiaID`, `Grund`, `bis`, `permanent`, wurdeEntbannt, von, timestamp, entbanntVon) VALUES (?, ?, ?, ? ,false, ?, ?, ?)");
            sql.setInt(1, ban.getBiomiaID());
            sql.setString(2, ban.getGrund());
            sql.setInt(3, ban.getBis());
            sql.setBoolean(4, ban.isPerm());
            sql.setInt(5, ban.getVon());
            sql.setInt(6, ban.getTimestamp());
            sql.setInt(7, entbanntVon == null ? 0 : entbanntVon.getBiomiaPlayerID());
            sql.executeUpdate();
            sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        BungeeMain.cachedBans.add(ban);
    }

    // BugReports Reporter
    public static ArrayList<String> getAllFinishedBugReports() {
        ArrayList<String> all = new ArrayList<>();

        try {
            PreparedStatement sql = MySQL.Connect(MySQL.Databases.biomia_db).prepareStatement("Select `Reporter` FROM `BugReports` WHERE finished = true");
            ResultSet rs = sql.executeQuery();
            while (rs.next()) {
                String reporter = rs.getString("Reporter");
                all.add(reporter);
            }
            rs.close();
            sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }

    public static void removeAllFinishedBugReports() {
        MySQL.executeUpdate("UPDATE `BugReports` SET finished = 2 where finished = true", MySQL.Databases.biomia_db);
    }
}
