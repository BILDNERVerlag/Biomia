package de.biomiaAPI.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL{

    public static Connection Connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
            String dbPass = "O78s3SObra0QzDZh";
            String dbUser = "biomia_usertest";
            String dbName = "biomia_db";
            String dbPort = "3306";
            String dbHost = "89.163.160.106";
            return DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?" + "user="
					+ dbUser + "&" + "password=" + dbPass + "&verifyServerCertificate=false&useSSL=true");

		} catch (ClassNotFoundException e) {
			System.out.println("Treiber nicht gefunden");
		} catch (SQLException e) {
			System.out.println("Verbindung nicht moglich");
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			e.printStackTrace();
		}
		return null;
	}

	public static boolean execute(String cmd) {
		Connection con = Connect();

		if (con != null) {
			try {
				PreparedStatement sql = con.prepareStatement(cmd);
				sql.execute();
				sql.close();
				con.close();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static String executeQuery(String cmd, String gettingspalte) {
		Connection con = Connect();

		if (con != null) {
			try {
				PreparedStatement sql = con.prepareStatement(cmd);
				ResultSet rs = sql.executeQuery();
				String s = null;
                //noinspection LoopStatementThatDoesntLoop
                while (rs.next()) {
					s = rs.getString(gettingspalte);
					break;
				}
				rs.close();
				sql.close();
				con.close();
				return s;
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	public static boolean executeQuerygetbool(String cmd, String gettingspalte) {
		Connection con = Connect();

		if (con != null) {
			try {
				PreparedStatement sql = con.prepareStatement(cmd);
				ResultSet rs = sql.executeQuery();
				boolean b = false;
                //noinspection LoopStatementThatDoesntLoop
                while (rs.next()) {
					b = rs.getBoolean(gettingspalte);
					break;
				}
				rs.close();
				sql.close();
				con.close();
				return b;
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return false;
	}

	public static int executeQuerygetint(String cmd, String gettingspalte) {
		Connection con = Connect();
		if (con != null) {
			try {
				PreparedStatement sql = con.prepareStatement(cmd);
				ResultSet rs = sql.executeQuery();
				int i = -1;
				//noinspection LoopStatementThatDoesntLoop,LoopStatementThatDoesntLoop
				while (rs.next()) {
					i = rs.getInt(gettingspalte);
					break;
				}
				rs.close();
				sql.close();
				con.close();
				return i;
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return -1;
	}

	public static boolean executeUpdate(String cmd) {
		Connection con = Connect();

		if (con != null) {
			try {
				PreparedStatement sql = con.prepareStatement(cmd);
				sql.executeUpdate();
				sql.close();
				con.close();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
