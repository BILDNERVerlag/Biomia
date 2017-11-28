package de.biomiaAPI.cosmetics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.mysql.MySQL;
import de.biomiaAPI.tools.ItemBase64;

public class Cosmetic {

	private static HashMap<Group, ? super CosmeticGroup> groups = new HashMap<>();
	private static HashMap<Integer, ? super CosmeticItem> items = new HashMap<>();
	private static HashMap<BiomiaPlayer, CosmeticInventory> inventorys = new HashMap<>();
	private static HashMap<BiomiaPlayer, HashMap<Integer, Integer>> limitedItems = new HashMap<>();
	private static HashMap<Integer, GadgetListener> gadgetListener = new HashMap<>();
	private static HashMap<Integer, ParticleListener> particleListener = new HashMap<>();

	public static CosmeticInventory getInventory(BiomiaPlayer bp) {
		return inventorys.get(bp);
	}

	public static <T extends CosmeticGroup> void initGroups(T group) {
		groups.put(group.getGroup(), group);
	}

	public static <T extends CosmeticItem> void addItem(T item) {
		items.put(item.getID(), item);
	}

	public enum Group {
		HEADS, PARTICLES, PETS, GADGETS, SUITS;
	}

	public static void load(BiomiaPlayer bp) {

		ArrayList<? super CosmeticItem> cosmeticItems = new ArrayList<>();
		HashMap<Integer, Integer> hm = new HashMap<>();

		Connection con = MySQL.Connect();

		try {
			PreparedStatement ps = con
					.prepareStatement("Select * from Cosmetics where BiomiaPlayer = " + bp.getBiomiaPlayerID());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int time = rs.getInt("Time");
				int id = rs.getInt("ID");
				if (time != 0) {
					hm.put(id, time);
				}
				cosmeticItems.add((CosmeticItem) items.get(id));
			}

			limitedItems.put(bp, hm);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static <T extends CosmeticItem> void addItemToDatabase(T item) {

		MySQL.executeUpdate("INSERT INTO `CosmeticItems`(`CosmeticGroup`, `Name`) VALUES ('" + item.getGroup().name()
				+ "', '" + item.getName() + "')");
		int id = MySQL.executeQuerygetint("Select ID from CosmeticItems where Name = '" + item.getName() + "'", "ID");
		item.setNewID(id);

		Connection con = MySQL.Connect();
		PreparedStatement ps;
		switch (item.getGroup()) {
		case GADGETS:

			CosmeticGadgetItem gi = (CosmeticGadgetItem) item;
			try {
				ps = con.prepareStatement("INSERT INTO `" + item.getGroup()
						+ "`(`ID`, `Name`, `Item`, `Commonness`, `GadgetItem`) VALUES (?,?,?,?,?)");
				ps.setInt(1, id);
				ps.setString(2, gi.getName());
				ps.setString(3, ItemBase64.toBase64(gi.getItem()));
				ps.setString(4, gi.getCommonness().name());
				ps.setString(5, ItemBase64.toBase64(gi.getGadgetItem()));
				ps.executeUpdate();
				ps.cancel();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case PETS:
			CosmeticPetItem pi = (CosmeticPetItem) item;
			try {
				ps = con.prepareStatement("INSERT INTO `" + item.getGroup()
						+ "`(`ID`, `Name`, `Item`, `Commonness`, `Type`) VALUES (?,?,?,?,?)");
				ps.setInt(1, id);
				ps.setString(2, pi.getName());
				ps.setString(3, ItemBase64.toBase64(pi.getItem()));
				ps.setString(4, pi.getCommonness().name());
				ps.setString(5, pi.getEntityType().name());
				ps.executeUpdate();
				ps.cancel();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case PARTICLES:
			CosmeticParticleItem pai = (CosmeticParticleItem) item;
			try {
				ps = con.prepareStatement(
						"INSERT INTO `" + item.getGroup() + "`(`ID`, `Name`, `Item`, `Commonness`) VALUES (?,?,?,?)");
				ps.setInt(1, id);
				ps.setString(2, pai.getName());
				ps.setString(3, ItemBase64.toBase64(pai.getItem()));
				ps.setString(4, pai.getCommonness().name());
				ps.executeUpdate();
				ps.cancel();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case SUITS:
			CosmeticSuitItem si = (CosmeticSuitItem) item;
			try {
				ps = con.prepareStatement("INSERT INTO `" + item.getGroup()
						+ "`(`ID`, `Name`, `Item`, `Commonness`, `Helmet`, `Chestplate`, `Leggins`, `Boots`) VALUES (?,?,?,?,?,?,?,?)");
				ps.setInt(1, id);
				ps.setString(2, si.getName());
				ps.setString(3, ItemBase64.toBase64(si.getItem()));
				ps.setString(4, si.getCommonness().name());
				ps.setString(5, ItemBase64.toBase64(si.getHelmet()));
				ps.setString(6, ItemBase64.toBase64(si.getChestplate()));
				ps.setString(7, ItemBase64.toBase64(si.getLeggings()));
				ps.setString(8, ItemBase64.toBase64(si.getBoots()));
				ps.executeUpdate();
				ps.cancel();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case HEADS:
			CosmeticHeadItem hi = (CosmeticHeadItem) item;
			try {
				ps = con.prepareStatement("INSERT INTO `" + item.getGroup()
						+ "`(`ID`, `Name`, `Item`, `Commonness`, `Head`) VALUES (?,?,?,?,?)");
				ps.setInt(1, id);
				ps.setString(2, hi.getName());
				ps.setString(3, ItemBase64.toBase64(hi.getItem()));
				ps.setString(4, hi.getCommonness().name());
				ps.setString(5, ItemBase64.toBase64(hi.getHead()));
				ps.executeUpdate();
				ps.cancel();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	public static void addGagetListener(int id, GadgetListener gl) {
		gadgetListener.put(id, gl);
	}

	public static GadgetListener getGadgetListener(int id) {
		return gadgetListener.get(id);
	}

	public static ParticleListener getParticleListener(int id) {
		return particleListener.get(id);
	}

}
