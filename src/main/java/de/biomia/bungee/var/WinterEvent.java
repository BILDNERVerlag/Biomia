package de.biomia.bungee.var;//package de.biomia.var;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Random;
//
//import MySQL;
//
//public class WinterEvent {
//
//	public static void randomWin(int day) {
//		int bioid = pickRandomWinner();
//		addBiomiaPlayerToWinner(bioid, day);
//	}
//
//	private static int pickRandomWinner() {
//		ArrayList<Integer> member = new ArrayList<>();
//		Connection con = MySQL.Connect();
//		try {
//			PreparedStatement ps = con.prepareStatement("Select BiomiaPlayer from WinterEvent");
//			ResultSet rs = ps.executeQuery();
//			while (rs.next()) {
//				member.add(rs.getInt("BiomiaPlayer"));
//			}
//			rs.close();
//			con.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return member.get(new Random().nextInt(member.size()));
//	}
//
//	private static void addBiomiaPlayerToWinner(int bpID, int day) {
//		MySQL.executeUpdate("INSERT INTO `WinterEventWinner`(`BiomiaPlayer`, `day`, `received`) VALUES (" + bpID + "," + day + ", false)");
//	}
//
//	public static void removePlayerFromWinner(int bpID) {
//		MySQL.executeUpdate("UPDATE `WinterEventWinner` SET `received`= true WHERE BiomiaPlayer = " + bpID);
//	}
//
//	public static ArrayList<Integer> getPlayerFromWinner(int bpID) {
//
//		Connection con = MySQL.Connect();
//		ArrayList<Integer> list = new ArrayList<>();
//		try {
//			PreparedStatement ps = con.prepareStatement("SELECT * FROM `WinterEventWinner` WHERE BiomiaPlayer = ? AND received = false");
//			ps.setInt(1, bpID);
//			ResultSet rs = ps.executeQuery();
//			while (rs.next()) {
//				list.add(rs.getInt("day"));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return list;
//	}
//
//
//}
