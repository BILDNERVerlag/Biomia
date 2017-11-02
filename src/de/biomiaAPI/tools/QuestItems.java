package de.biomiaAPI.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.QuestEvents.GiveItemEvent;
import de.biomiaAPI.mysql.MySQL;

public class QuestItems {

	public static void addQuestItem(BiomiaPlayer biomiaPlayer, ItemStack itemStack) {
		MySQL.executeUpdate("Insert into QuestItems (`BiomiaPlayer`, `Item`) values ("
				+ biomiaPlayer.getBiomiaPlayerID() + ", '" + toBase64(itemStack) + "')");
	}

	public static void giveQuestItems(BiomiaPlayer biomiaPlayer) {
		Connection con = MySQL.Connect();
		try {
			PreparedStatement ps = con.prepareStatement("Select Item from QuestItems where BiomiaPlayer = ?");
			ps.setInt(1, biomiaPlayer.getBiomiaPlayerID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String base64Item = rs.getString("Item");
				new GiveItemEvent(fromBase64(base64Item)).executeEvent(biomiaPlayer.getQuestPlayer());
				MySQL.executeUpdate("DELETE FROM `QuestItems` WHERE `BiomiaPlayer`= " + biomiaPlayer.getBiomiaPlayerID()
						+ " AND `Item` = '" + base64Item + "'");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static String toBase64(ItemStack stack) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			dataOutput.writeObject(stack);
			dataOutput.close();

			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			return null;
		}
	}

	private static ItemStack fromBase64(String data) {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

			ItemStack stack = (ItemStack) dataInput.readObject();
			dataInput.close();
			return stack;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
