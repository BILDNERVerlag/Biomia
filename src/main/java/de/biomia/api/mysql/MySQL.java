package de.biomia.api.mysql;

import java.sql.*;
import java.util.HashMap;

public class MySQL {

    public enum Databases {
        biomia_db, paf_db, perm_db, cosmetics_db, plots_db, quests_db, stats_db, misc_db, achiev_db
    }

    /*
    All versions that do not use the new Databases enum are from now on deprecated.
     */

    private static final HashMap<Databases, Connection> connections = new HashMap<>();

    public static void closeConnections() {
        connections.values().forEach(each -> {
            try {
                each.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void handleSQLException(SQLException e) {
        System.out.println("Verbindung nicht m\u00F6glich");
        System.out.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("VendorError: " + e.getErrorCode());
        e.printStackTrace();
    }

    private static Connection newConnection(Databases db) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        String dbPass = "O78s3SObra0QzDZh";
        String dbUser = "biomia_usertest";
        String dbName = db.name();
        String dbPort = "3306";
        String dbHost = "89.163.160.106";
        return DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?" + "user="
                + dbUser + "&" + "password=" + dbPass + "&verifyServerCertificate=false&useSSL=true");
    }

    public static Connection Connect(Databases db) {
        Connection connection = connections.computeIfAbsent(db, con -> {
            try {
                return newConnection(db);
            } catch (ClassNotFoundException e) {
                System.out.println("Treiber nicht gefunden");
            } catch (SQLException e) {
                handleSQLException(e);
            }
            return null;
        });
        try {
            if (connection.isClosed()) {
                connection = newConnection(db);
                connections.put(db, connection);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Treiber nicht gefunden");
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return connection;
    }

    public static void execute(String cmd, Databases db) {
        Connection con = Connect(db);

        executeStatement(cmd, con);
    }

    public static String executeQuery(String cmd, String gettingspalte, Databases db) {
        Connection con = Connect(db);

        if (con != null) {
            PreparedStatement sql = null;
            ResultSet rs = null;
            try {
                sql = con.prepareStatement(cmd);
                rs = sql.executeQuery();
                String s = null;
                //noinspection LoopStatementThatDoesntLoop
                while (rs.next()) {
                    s = rs.getString(gettingspalte);
                    break;
                }
                return s;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeQuietly(rs, sql);
            }

        }
        return null;
    }

    private static void closeQuietly(ResultSet rs, PreparedStatement ps) {
        //TODO: implement in every method
        try {
            if (rs != null) rs.close();
        } catch (Exception ignored) {
        }
        try {
            if (ps != null) ps.close();
        } catch (Exception ignored) {
        }
    }

//    public static boolean executeQuerygetbool(String cmd, String gettingspalte, Databases db) {
//        Connection con = Connect(db);
//
//        if (con != null) {
//            try {
//                PreparedStatement sql = con.prepareStatement(cmd);
//                ResultSet rs = sql.executeQuery();
//                boolean b = false;
//                if (rs.next())
//                    b = rs.getBoolean(gettingspalte);
//                rs.close();
//                sql.close();
//                return b;
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//        }
//        return false;
//    }

    public static int executeQuerygetint(String cmd, String gettingspalte, Databases db) {
        Connection con = Connect(db);
        int i = -1;
        if (con != null) {
            try {
                PreparedStatement sql = con.prepareStatement(cmd);
                ResultSet rs = sql.executeQuery();
                if (rs.next())
                    i = rs.getInt(gettingspalte);
                rs.close();
                sql.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public static boolean executeUpdate(String cmd, Databases db) {
        Connection con = Connect(db);

        if (con != null) {
            try {
                PreparedStatement sql = con.prepareStatement(cmd);
                sql.executeUpdate();
                sql.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static void executeStatement(String cmd, Connection con) {
        if (con != null) {
            try {
                PreparedStatement sql = con.prepareStatement(cmd);
                sql.execute();
                sql.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
