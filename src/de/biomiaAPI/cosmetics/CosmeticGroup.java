package de.biomiaAPI.cosmetics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import de.biomiaAPI.cosmetics.Cosmetic.Group;
import de.biomiaAPI.cosmetics.CosmeticItem.Commonness;
import de.biomiaAPI.mysql.MySQL;
import de.biomiaAPI.tools.ItemBase64;

public class CosmeticGroup {

	private Group group;
	private ArrayList<? super CosmeticItem> items = new ArrayList<>();

	public CosmeticGroup(Group group) {
		this.group = group;
		loadGroup();
	}

	public <T extends CosmeticItem> void addItem(T item) {
		items.add(item);
	}

	public void loadGroup() {
		Connection con = MySQL.Connect();

		try {

			PreparedStatement ps = con.prepareStatement("Select * from ?");
			ps.setString(1, group.name());
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

					if (boots != null) {
						suit.setHelmet(ItemBase64.fromBase64(helmet));
					}
					if (chestplate != null) {
						suit.setChestplate(ItemBase64.fromBase64(chestplate));
					}
					if (leggins != null) {
						suit.setLeggins(ItemBase64.fromBase64(leggins));
					}
					if (boots != null) {
						suit.setBoots(ItemBase64.fromBase64(boots));
					}
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
}
