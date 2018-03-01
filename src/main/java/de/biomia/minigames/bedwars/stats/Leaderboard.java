package de.biomia.minigames.bedwars.stats;//package de.biomia.minigames.bedwars.stats;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.UUID;
//
//import org.bukkit.block.BlockFace;
//
//import de.biomia.minigames.bedwars.var.Variables;
//import de.biomia.api.mysql.MySQL;
//
//public class Leaderboard {
//
//	public static void updateThisStats() {
//
//		for (Stats stat : Variables.stats) {
//
//			if (MySQL.executeQuery("select biomiaI from BedWarsStats where uuid = '" + stat.uuid + "'", "uuid") != null) {
//
//				MySQL.executeUpdate("UPDATE `BedWarsStats` SET `kills`= " + stat.kills + " , `name`= '" + stat.name
//						+ "' ,`deaths`=" + stat.deaths + ",`wins`=" + stat.wins + ",`played_games`=" + stat.played_games
//						+ " WHERE uuid = '" + stat.uuid + "'");
//			} else {
//				MySQL.executeUpdate(
//						"INSERT INTO `BedWarsStats`(`kills`, `deaths`, `wins`, `played_games`, `rank`, `uuid`, `name`) VALUES ("
//								+ stat.kills + ", " + stat.deaths + ", " + stat.wins + ", " + stat.played_games
//								+ ", -1, '" + stat.uuid + "', '" + stat.name + "')");
//			}
//		}
//	}
//
//	public static Stats getStat(UUID uuid) {
//		Stats s = new Stats(uuid);
//
//		Connection con = MySQL.Connect();
//		if (con != null) {
//			try {
//				PreparedStatement ps = con.prepareStatement("Select * FROM `BedWarsStats` where uuid = ?");
//				ps.setString(1, uuid.toString());
//				ResultSet rs = ps.executeQuery();
//				while (rs.next()) {
//					s.kills = rs.getInt("kills");
//					s.deaths = rs.getInt("deaths");
//					s.wins = rs.getInt("wins");
//					s.played_games = rs.getInt("played_games");
//					s.rank = rs.getInt("rank");
//					s.name = rs.getString("name");
//					break;
//				}
//				rs.close();
//				ps.close();
//				con.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return s;
//	}
//
//	public static Stats getStat(int rank) {
//		Stats s = null;
//		Connection con = MySQL.Connect();
//		if (con != null) {
//			try {
//				PreparedStatement ps = con.prepareStatement("Select * FROM `BedWarsStats` where rank = ?");
//				ps.setInt(1, rank);
//				ResultSet rs = ps.executeQuery();
//				while (rs.next()) {
//					s = new Stats(UUID.fromString(rs.getString("uuid")));
//					s.kills = rs.getInt("kills");
//					s.deaths = rs.getInt("deaths");
//					s.wins = rs.getInt("wins");
//					s.played_games = rs.getInt("played_games");
//					s.rank = rs.getInt("rank");
//					s.name = rs.getString("name");
//				}
//				rs.close();
//				ps.close();
//				con.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//
//			return s;
//
//		}
//		return null;
//	}
//
//	public static void updateAllRanks() {
//
//		Connection con = MySQL.Connect();
//		if (con != null) {
//			try {
//				PreparedStatement sql = con.prepareStatement("SELECT uuid, rank FROM BedWarsStats ORDER BY wins DESC");
//				ResultSet rs = sql.executeQuery();
//				int i = 1;
//				while (rs.next()) {
//
//					if (i != rs.getInt("rank")) {
//						MySQL.executeUpdate("UPDATE `BedWarsStats` SET `rank`= " + i + " WHERE uuid = '"
//								+ rs.getString("uuid") + "'");
//					}
//
//					i++;
//				}
//				rs.close();
//				sql.close();
//				con.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static byte getFacingDirectionByte(BlockFace blockFace) {
//
//		switch (blockFace) {
//		case NORTH:
//			return 2;
//
//		case EAST:
//			return 5;
//
//		case SOUTH:
//			return 3;
//
//		case WEST:
//			return 4;
//
//		default:
//			return 1;
//		}
//	}
//}
