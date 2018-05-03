package de.biomia.universal;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.achievements.BiomiaStat;

import java.sql.*;
import java.util.HashMap;
import java.util.Properties;

public abstract class MySQL {

    public enum Databases {
        biomia_db, cosmetics_db, quests_db, stats_db, achiev_db
    }

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
        Properties properties = new Properties();
        properties.setProperty("user", "biomia");
        properties.setProperty("password", "O78s3SObra0QzDZh");
        properties.setProperty("useSSL", "true");
        properties.setProperty("autoReconnect", "true");
        properties.setProperty("verifyServerCertificate", "false");

        Class.forName("com.mysql.jdbc.Driver");
        String dbName = db.name();
        String dbPort = "3306";
        String dbHost = "89.163.160.106";
        return DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName, properties);
    }

    public static Connection Connect(Databases db) {
        Connection connection = connections.computeIfAbsent(db, con -> {
            try {
                Connection conn = newConnection(db);
                BiomiaStat.MySQLConnections.increment(0, 1, Biomia.getServerInstance().getServerType().name() + ", " + db.name());
                return conn;
            } catch (ClassNotFoundException e) {
                System.out.println("Driver Not Found");
            } catch (SQLException e) {
                handleSQLException(e);
            }
            return null;
        });
        try {
            if (connection == null || connection.isClosed()) {
                connection = newConnection(db);
                BiomiaStat.MySQLConnections.increment(0, 1, Biomia.getServerInstance().getServerType().name() + ", " + db.name() + ", RECONNECT!");
                connections.put(db, connection);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Driver Not Found");
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
                if (rs.next()) {
                    s = rs.getString(gettingspalte);
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

    public static int executeQuerygetint(String cmd, String gettingspalte, Databases db) {
        Connection con = Connect(db);
        int i = -1;
        if (con != null) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = con.prepareStatement(cmd);
                rs = ps.executeQuery();
                if (rs.next())
                    i = rs.getInt(gettingspalte);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeQuietly(rs, ps);
            }
        }
        return i;
    }

    public static boolean executeUpdate(String cmd, Databases db) {
        Connection con = Connect(db);

        if (con != null) {
            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(cmd);
                ps.executeUpdate();
                return true;
            } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
                //no stack trace in this case
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeQuietly(null, ps);
            }
        }
        return false;
    }

    private static void executeStatement(String cmd, Connection con) {
        if (con != null) {
            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(cmd);
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeQuietly(null, ps);
            }
        }
    }

    private static void closeQuietly(ResultSet rs, PreparedStatement ps) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        } catch (SQLException ignored) {
        }
    }

}
