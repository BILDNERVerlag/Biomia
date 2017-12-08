package de.biomiaAPI.tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.mysql.MySQL;

public class SkyWarsKit {

	public static boolean addKit(BiomiaPlayer biomiaPlayer, int kitID, int... months) {
		return MySQL.executeUpdate("Insert into SkyWarsKits (`BiomiaPlayer`, `kitID`, `available`) values ("
				+ biomiaPlayer.getBiomiaPlayerID() + ", " + kitID + ", '" + Arrays.toString(months) + "')");
	}

	public static boolean addKit(BiomiaPlayer biomiaPlayer, int kitID) {		
		return MySQL.executeUpdate("Insert into SkyWarsKits (`BiomiaPlayer`, `kitID`) values ("
				+ biomiaPlayer.getBiomiaPlayerID() + ", " + kitID + ")");
		
	}

	public static boolean removeKit(BiomiaPlayer biomiaPlayer, int kitID) {
		return MySQL.executeUpdate("Delete from SkyWarsKits where BiomiaPlayer = " + biomiaPlayer.getBiomiaPlayerID()
				+ " and kitID = " + kitID);
	}

	public static ArrayList<Integer> getAvailableKit(BiomiaPlayer biomiaPlayer) {
		ArrayList<Integer> availableKits = new ArrayList<>();
		Connection con = MySQL.Connect();
		try {
			PreparedStatement ps = con
					.prepareStatement("Select kitID, available from SkyWarsKits where BiomiaPlayer = ?");
			ps.setInt(1, biomiaPlayer.getBiomiaPlayerID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String available = rs.getString("available");
				Calendar c = Calendar.getInstance();

				if (available == null || Arrays.asList(available.substring(1, available.length() - 1).split(", "))
						.contains(c.get(Calendar.MONTH) + "")) {
					availableKits.add(rs.getInt("kitID"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return availableKits;

	}
}
