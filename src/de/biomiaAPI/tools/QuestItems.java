package de.biomiaAPI.tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import org.bukkit.inventory.ItemStack;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.QuestEvents.GiveItemEvent;
import de.biomiaAPI.mysql.MySQL;

public class QuestItems {

	public static void addQuestItem(BiomiaPlayer biomiaPlayer, ItemStack itemStack) {
		MySQL.executeUpdate("Insert into QuestItems (`BiomiaPlayer`, `Item`) values ("
				+ biomiaPlayer.getBiomiaPlayerID() + ", '" + ItemBase64.toBase64(itemStack) + "')");
	}

	public static void giveQuestItems(BiomiaPlayer biomiaPlayer) {
		Connection con = MySQL.Connect();
		try {
			PreparedStatement ps = Objects.requireNonNull(con).prepareStatement("Select Item from QuestItems where BiomiaPlayer = ?");
			ps.setInt(1, biomiaPlayer.getBiomiaPlayerID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String base64Item = rs.getString("Item");
				new GiveItemEvent(ItemBase64.fromBase64(base64Item)).executeEvent(biomiaPlayer.getQuestPlayer());
				MySQL.executeUpdate("DELETE FROM `QuestItems` WHERE `BiomiaPlayer`= " + biomiaPlayer.getBiomiaPlayerID()
						+ " AND `Item` = '" + base64Item + "'");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
