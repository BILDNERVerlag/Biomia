package de.biomia.spigot.general.reportsystem;

import de.biomia.spigot.Biomia;
import de.biomia.bungee.BungeeMain;
import de.biomia.bungee.var.Bans;
import de.biomia.universal.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReportSQL {

    public static void addBugReport(Player reporter, String reason) {
        MySQL.executeUpdate("INSERT INTO `BugReports`(`Reporter`, `Grund`, `Time`) VALUES ('" + reporter.getName()
                + "','" + reason + "'," + System.currentTimeMillis() / 1000 + ")", MySQL.Databases.biomia_db);
    }

    public static boolean addPlayerReport(PlayerReport report) {
        if (!hasReportet(report.getReporterBiomiaPlayer().getBiomiaPlayerID(), report.getReporteterBiomiaPlayer().getBiomiaPlayerID())) {
            MySQL.executeUpdate(
                    "INSERT INTO `PlayerReports`(`Reporter`, `Reporteter`, `Grund`, `Time`) VALUES ("
                            + report.getReporterBiomiaPlayer().getBiomiaPlayerID() + "," + report.getReporteterBiomiaPlayer().getBiomiaPlayerID() + ",'"
                            + report.getGrund() + "', " + System.currentTimeMillis() / 1000 + ")", MySQL.Databases.biomia_db);
            ReportManager.sendReport(report);
            return true;
        } else {
            Player plReporter = Bukkit.getPlayer(report.getReporterBiomiaPlayer().getName());
            plReporter.sendMessage("\u00A7cDu hast den Spieler bereits reportet!");
            return false;
        }
    }


    public static int getLevel(int reporteterID) {
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        if (con != null) {
            try {
                PreparedStatement ps = con.prepareStatement("SELECT `Reporteter` FROM `PlayerReports` where Reporteter = " + reporteterID);
                ResultSet rs = ps.executeQuery();
                int i = 0;
                while (rs.next()) {
                    i++;
                }
                rs.close();
                ps.close();
                return i;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private static boolean hasReportet(int reporterID, int reporteterID) {
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        if (con != null) {
            try {
                PreparedStatement ps = con.prepareStatement("SELECT `Reporteter` FROM `PlayerReports` WHERE Reporter = ? AND Reporteter = ?");
                ps.setInt(1, reporterID);
                ps.setInt(2, reporteterID);
                ResultSet rs = ps.executeQuery();
                boolean b = false;
                if (rs.next()) {
                    b = true;
                }
                rs.close();
                ps.close();
                return b;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void getAllReports() {
        ReportManager.plReports.clear();
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        if (con != null) {
            try {
                PreparedStatement ps = con.prepareStatement("SELECT Reporter, Grund, Reporteter FROM `PlayerReports`");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int reportetplayerID = rs.getInt("Reporteter");
                    PlayerReport report = new PlayerReport(Biomia.getOfflineBiomiaPlayer(rs.getInt("Reporter")), Biomia.getOfflineBiomiaPlayer(reportetplayerID), rs.getString("Grund"));
                    ReportManager.plReports.add(report);
                }
                rs.close();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    public static void removeBugReport(int id) {
        MySQL.executeUpdate("DELETE FROM `BugReports` WHERE `id` = " + id, MySQL.Databases.biomia_db);
    }

    public static ArrayList<PlayerReport> getAllReportsOf(int biomiaIDReportedPlayer) {
        ReportManager.plReports.clear();
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        ArrayList<PlayerReport> reports = new ArrayList<>();
        if (con != null) {
            try {
                PreparedStatement ps = con.prepareStatement("SELECT Reporter, Grund FROM `PlayerReports` WHERE Reporteter = " + biomiaIDReportedPlayer);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    reports.add(new PlayerReport(Biomia.getOfflineBiomiaPlayer(rs.getInt("Reporter")), Biomia.getOfflineBiomiaPlayer(biomiaIDReportedPlayer), rs.getString("Grund")));
                }
                rs.close();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return reports;
    }

    public static void getAllBans() {

        ArrayList<Bans> all = new ArrayList<>();

        try {
            PreparedStatement sql = MySQL.Connect(MySQL.Databases.biomia_db).prepareStatement("Select * from BanList");
            ResultSet rs = sql.executeQuery();
            while (rs.next()) {
                int biomiaID = rs.getInt("biomiaID");
                String reason = rs.getString("Grund");
                int timestamp = rs.getInt("timestamp");
                int length = rs.getInt("länge");
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

    public static void moveToCache(Bans ban) {

        MySQL.executeUpdate("DELETE FROM `BanList` WHERE biomiaID = '" + ban.getBiomiaID() + "'", MySQL.Databases.biomia_db);
        BungeeMain.activeBans.remove(ban);

        try {
            PreparedStatement sql = MySQL.Connect(MySQL.Databases.biomia_db).prepareStatement(
                    "Insert into CachedBanList (`biomiaID`, `Grund`, `bis`, `permanent`, wurdeEntbannt, von, timestamp) VALUES (?, ?, ?, ? ,false, ?, ?)");
            sql.setInt(1, ban.getBiomiaID());
            sql.setString(2, ban.getGrund());
            sql.setInt(3, ban.getBis());
            sql.setBoolean(4, ban.isPerm());
            sql.setInt(5, ban.getVon());
            sql.setInt(6, ban.getTimestamp());
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
