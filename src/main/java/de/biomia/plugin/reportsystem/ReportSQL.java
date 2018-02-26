package de.biomia.plugin.reportsystem;

import de.biomia.api.mysql.MySQL;
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
        if (!hasReportet(report.getReporterBiomiaID(), report.getReporteterBiomiaID())) {
            MySQL.executeUpdate(
                    "INSERT INTO `PlayerReports`(`Reporter`, `Reporteter`, `Grund`, `Time`) VALUES ("
                            + report.getReporterBiomiaID() + "," + report.getReporteterBiomiaID() + ",'"
                            + report.getGrund() + "', " + System.currentTimeMillis() / 1000 + ")", MySQL.Databases.biomia_db);
            ReportManager.sendReport(report);
            return true;
        } else {
            Player plReporter = Bukkit.getPlayer(report.getReporterName());
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
                    PlayerReport report = new PlayerReport(rs.getInt("Reporter"), reportetplayerID, rs.getString("Grund"));
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
                    reports.add(new PlayerReport(rs.getInt("Reporter"), biomiaIDReportedPlayer, rs.getString("Grund")));
                }
                rs.close();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return reports;
    }
}
