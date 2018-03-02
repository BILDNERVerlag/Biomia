package de.biomia.general.cosmetics;

import de.biomia.BiomiaPlayer;
import de.biomia.data.MySQL;
import de.biomia.general.cosmetics.gadgets.GadgetListener;
import de.biomia.general.cosmetics.items.*;
import de.biomia.general.cosmetics.items.CosmeticItem.Commonness;
import de.biomia.general.cosmetics.particles.ParticleListener;
import de.biomia.tools.Base64;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Cosmetic {

	private static final HashMap<Group, CosmeticGroup> groups = new HashMap<>();
	private static final HashMap<Integer, ? super CosmeticItem> items = new HashMap<>();
	private static final HashMap<BiomiaPlayer, CosmeticInventory> inventorys = new HashMap<>();
	private static final HashMap<BiomiaPlayer, HashMap<Integer, Integer>> limitedItems = new HashMap<>();
	private static final HashMap<Integer, GadgetListener> gadgetListener = new HashMap<>();
	private static final HashMap<Integer, ParticleListener> particleListener = new HashMap<>();
	private static final HashMap<Commonness, ArrayList<CosmeticItem>> commonnessItems = new HashMap<>();
	private static Inventory inv;
	public static final int gadgetSlot = 4;

	public static HashMap<Group, CosmeticGroup> getGroups() {
		return groups;
	}

	public static HashMap<Integer, ? super CosmeticItem> getItems() {
		return items;
	}

	public static Inventory getMainInventory() {
		if (inv == null)
			initMainInventory();
		return inv;
	}

	private static void initMainInventory() {
		inv = Bukkit.createInventory(null, 9, "\u00A75Cosmetics");

		int i = 0;
		for (Group g : Group.values()) {
			inv.setItem(i, groups.get(g).getIcon());
			i += 2;
		}
	}

	public static void openMainInventory(BiomiaPlayer bp) {
		if (inv == null)
			initMainInventory();
		bp.getPlayer().openInventory(inv);
	}

	public static boolean openGroupInventory(BiomiaPlayer bp, String itemName) {
		for (Group g : groups.keySet()) {
			if (groups.get(g).getIcon().getItemMeta().getDisplayName().equals(itemName)) {
				openGroupInventory(bp, groups.get(g));
				return true;
			}
		}
		return false;
	}

	private static void openGroupInventory(BiomiaPlayer bp, CosmeticGroup group) {
		CosmeticInventory inv = getInventory(bp);
		if (inv == null) {
            ArrayList<CosmeticItem> groupItems = new ArrayList<>();
			for (int id : limitedItems.get(bp).keySet()) {
				groupItems.add((CosmeticItem) items.get(id));
			}
			inv = new CosmeticInventory(groupItems, bp);
			inventorys.put(bp, inv);
		}
		inv.openInventory(group);
	}

	private static CosmeticInventory getInventory(BiomiaPlayer bp) {
		return inventorys.get(bp);
	}

	public static void initGroup(CosmeticGroup group) {
		groups.put(group.getGroup(), group);
	}

    public static void addItem(CosmeticItem item) {
		items.put(item.getID(), item);
	}

	public enum Group {
		HEADS, SUITS, GADGETS, PETS, PARTICLES
    }

	public static void load(BiomiaPlayer bp) {

		HashMap<Integer, Integer> hm = new HashMap<>();

		if (bp.getPlayer().hasPermission("biomia.cosmetics.*")) {
			for (int id : items.keySet()) {
				hm.put(id, -1);
			}
			limitedItems.put(bp, hm);
			return;
		}

		Connection con = MySQL.Connect(MySQL.Databases.cosmetics_db);

		try {
			PreparedStatement ps = Objects.requireNonNull(con)
					.prepareStatement("Select * from Cosmetics where BiomiaPlayer = " + bp.getBiomiaPlayerID());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int time = rs.getInt("Time");
				int id = rs.getInt("ID");
				hm.put(id, time);
				}
			limitedItems.put(bp, hm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static boolean hasItem(BiomiaPlayer bp, int itemID) {
		return limitedItems.get(bp).containsKey(itemID);
	}

	public static int getLimit(BiomiaPlayer bp, int id) {
		if (hasItem(bp, id))
			return limitedItems.get(bp).get(id);
		return 0;
	}

	public static void setLimit(BiomiaPlayer bp, int id, int limit) {
		if (limit == 0) {
			limitedItems.get(bp).remove(id);
			MySQL.executeUpdate(
					"DELETE From `Cosmetics` WHERE `BiomiaPlayer`= " + bp.getBiomiaPlayerID() + " AND ID = " + id, MySQL.Databases.cosmetics_db);
			inventorys.get(bp).removeItem(id);
		} else if (!hasItem(bp, id)) {
			limitedItems.get(bp).put(id, limit);
			MySQL.executeUpdate("INSERT INTO `Cosmetics` (`BiomiaPlayer`, `ID`, `Time`) VALUES ("
					+ bp.getBiomiaPlayerID() + ", " + id + ", " + limit + ")", MySQL.Databases.cosmetics_db);
			inventorys.get(bp).addItem(id);
		} else {
			limitedItems.get(bp).put(id, limit);
			MySQL.executeUpdate("UPDATE `Cosmetics` SET `Time`= " + limit + " WHERE `BiomiaPlayer`= "
					+ bp.getBiomiaPlayerID() + " AND ID = " + id, MySQL.Databases.cosmetics_db);
		}
	}

    public static void addItemToDatabase(CosmeticItem item) {

		MySQL.executeUpdate("INSERT INTO `CosmeticItems`(`CosmeticGroup`, `Name`) VALUES ('" + item.getGroup().name()
				+ "', '" + item.getName() + "')", MySQL.Databases.cosmetics_db);
		int id = MySQL.executeQuerygetint("Select ID from CosmeticItems where Name = '" + item.getName() + "'", "ID", MySQL.Databases.cosmetics_db);
		item.setNewID(id);

		Connection con = MySQL.Connect(MySQL.Databases.cosmetics_db);
		PreparedStatement ps;
		switch (item.getGroup()) {
		case GADGETS:

			CosmeticGadgetItem gi = (CosmeticGadgetItem) item;
			try {
				ps = Objects.requireNonNull(con).prepareStatement("INSERT INTO `" + item.getGroup()
						+ "`(`ID`, `Name`, `Item`, `Commonness`, `GadgetItem`) VALUES (?,?,?,?,?)");
				ps.setInt(1, id);
				ps.setString(2, gi.getName());
				ps.setString(3, Base64.toBase64(gi.getItem()));
				ps.setString(4, gi.getCommonness().name());
				ps.setString(5, Base64.toBase64(gi.getGadgetItem()));
				ps.executeUpdate();
				ps.cancel();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case PETS:
			CosmeticPetItem pi = (CosmeticPetItem) item;
			try {
				ps = Objects.requireNonNull(con).prepareStatement("INSERT INTO `" + item.getGroup()
						+ "`(`ID`, `Name`, `Item`, `Commonness`, `Type`) VALUES (?,?,?,?,?)");
				ps.setInt(1, id);
				ps.setString(2, pi.getName());
				ps.setString(3, Base64.toBase64(pi.getItem()));
				ps.setString(4, pi.getCommonness().name());
				ps.setString(5, pi.getEntityType().name());
				ps.executeUpdate();
				ps.cancel();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case PARTICLES:
			CosmeticParticleItem pai = (CosmeticParticleItem) item;
			try {
				ps = Objects.requireNonNull(con).prepareStatement(
						"INSERT INTO `" + item.getGroup() + "`(`ID`, `Name`, `Item`, `Commonness`) VALUES (?,?,?,?)");
				ps.setInt(1, id);
				ps.setString(2, pai.getName());
				ps.setString(3, Base64.toBase64(pai.getItem()));
				ps.setString(4, pai.getCommonness().name());
				ps.executeUpdate();
				ps.cancel();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case SUITS:
			CosmeticSuitItem si = (CosmeticSuitItem) item;
			try {
				ps = Objects.requireNonNull(con).prepareStatement("INSERT INTO `" + item.getGroup()
						+ "`(`ID`, `Name`, `Item`, `Commonness`, `Helmet`, `Chestplate`, `Leggins`, `Boots`) VALUES (?,?,?,?,?,?,?,?)");
				ps.setInt(1, id);
				ps.setString(2, si.getName());
				ps.setString(3, Base64.toBase64(si.getItem()));
				ps.setString(4, si.getCommonness().name());
				ps.setString(5, Base64.toBase64(si.getHelmet()));
				ps.setString(6, Base64.toBase64(si.getChestplate()));
				ps.setString(7, Base64.toBase64(si.getLeggings()));
				ps.setString(8, Base64.toBase64(si.getBoots()));
				ps.executeUpdate();
				ps.cancel();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case HEADS:
			CosmeticHeadItem hi = (CosmeticHeadItem) item;
			try {
				ps = Objects.requireNonNull(con).prepareStatement("INSERT INTO `" + item.getGroup()
						+ "`(`ID`, `Name`, `Item`, `Commonness`, `Head`) VALUES (?,?,?,?,?)");
				ps.setInt(1, id);
				ps.setString(2, hi.getName());
				ps.setString(3, Base64.toBase64(hi.getItem()));
				ps.setString(4, hi.getCommonness().name());
				ps.setString(5, Base64.toBase64(hi.getHead()));
				ps.executeUpdate();
				ps.cancel();
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

	public static void addParticleListener(int id, ParticleListener pl) {
		particleListener.put(id, pl);
	}

	public static GadgetListener getGadgetListener(int id) {
		return gadgetListener.get(id);
	}

	public static ParticleListener getParticleListener(int id) {
		return particleListener.get(id);
	}

	public static ArrayList<CosmeticItem> getItemsOfCommonness(Commonness commonness) {
		ArrayList<CosmeticItem> items = commonnessItems.get(commonness);
		if (items == null) {
			items = new ArrayList<>();
			for (int itemID : Cosmetic.items.keySet()) {
				CosmeticItem cItem = (CosmeticItem) Cosmetic.items.get(itemID);
				if (cItem.getCommonness() == commonness)
					items.add(cItem);
			}
			commonnessItems.put(commonness, items);
		}
		return items;
	}
}
