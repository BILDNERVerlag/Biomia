package de.biomiaAPI.cosmetics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.Cosmetic.Group;
import de.biomiaAPI.cosmetics.CosmeticItem.Commonness;
import de.biomiaAPI.mysql.MySQL;
import de.biomiaAPI.tools.ItemBase64;

public class CosmeticGroup {

	private final Group group;
	private final ArrayList<? super CosmeticItem> items = new ArrayList<>();
	private final ItemStack icon;

	public CosmeticGroup(Group group, ItemStack icon) {
		this.group = group;
		this.icon = icon;
		loadGroup();
	}

	public void remove(BiomiaPlayer bp) {
		if (!items.isEmpty())
			((CosmeticItem) items.get(0)).remove(bp);
	}

	private <T extends CosmeticItem> void addItem(T item) {
		items.add(item);
	}

	private void loadGroup() {
		Connection con = MySQL.Connect();

		try {

			PreparedStatement ps = Objects.requireNonNull(con).prepareStatement("Select * from " + group.name());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("ID");
				String name = rs.getString("Name");
				ItemStack is = ItemBase64.fromBase64(rs.getString("Item"));
				Commonness c = Commonness.valueOf(rs.getString("Commonness"));

				switch (group) {
				case GADGETS:
					ItemStack gadget = ItemBase64.fromBase64(rs.getString("GadgetItem"));
					addItem(new CosmeticGadgetItem(id, name, is, c, gadget));
					break;
				case HEADS:
					ItemStack head = ItemBase64.fromBase64(rs.getString("Head"));
					addItem(new CosmeticHeadItem(id, name, is, c, head));
					break;
				case PARTICLES:
					addItem(new CosmeticParticleItem(id, name, is, c));
					break;
				case PETS:
					addItem(new CosmeticPetItem(id, name, is, c, EntityType.valueOf(rs.getString("Type"))));
					break;
				case SUITS:
					CosmeticSuitItem suit = new CosmeticSuitItem(id, name, is, c);
					String helmet = rs.getString("Helmet");
					String chestplate = rs.getString("Chestplate");
					String leggins = rs.getString("Leggins");
					String boots = rs.getString("Boots");
					if (boots != null)
						suit.setHelmet(ItemBase64.fromBase64(helmet));
					if (chestplate != null)
						suit.setChestplate(ItemBase64.fromBase64(chestplate));
					if (leggins != null)
						suit.setLeggins(ItemBase64.fromBase64(leggins));
					if (boots != null)
						suit.setBoots(ItemBase64.fromBase64(boots));
					addItem(suit);
					break;
				default:
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Group getGroup() {
		return group;
	}

	public ItemStack getIcon() {
		return icon;
	}

	public ArrayList<? super CosmeticItem> getItems() {
		return items;
	}
}
