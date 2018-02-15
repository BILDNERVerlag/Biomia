package de.biomiaAPI.QuestConditions;

import de.biomiaAPI.Quests.QuestPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class ItemConditions {

	public static boolean hasItemInInventory(QuestPlayer qp, Material material, int menge) {
		int i = 0;

		for (ItemStack is : qp.getPlayer().getInventory().getContents()) {
			if (is != null)
				if (is.getType() == material)
					i += is.getAmount();
		}

		return (i >= menge);
	}

	public static boolean hasItemInInventory(QuestPlayer qp, Material material, int menge, String name) {
		int i = 0;

		for (ItemStack is : qp.getPlayer().getInventory().getContents()) {
			if (is != null)
				try {
					if (is.getType() == material && is.getItemMeta() != null
							&& is.getItemMeta().getDisplayName().equals(name))
						i += is.getAmount();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
		}

		return (i >= menge);
	}

	public static boolean hasItemInInventory(QuestPlayer qp, Material material, int menge, byte data) {

		int i = 0;

		MaterialData dat = new MaterialData(material, data);
		for (ItemStack is : qp.getPlayer().getInventory().getContents()) {
			if (is != null)
				if (is.getType() == material && is.getData() == dat)
					i += is.getAmount();
		}

		return (i >= menge);
	}

	public static boolean hasItemInInventory(QuestPlayer qp, Material material, int menge, String name, byte data) {

		int i = 0;

		MaterialData dat = new MaterialData(material, data);
		for (ItemStack is : qp.getPlayer().getInventory().getContents()) {
			if (is != null)
				if (is.getType() == material && is.getData() == dat && is.getItemMeta() != null
						&& is.getItemMeta().getDisplayName().equals(name))
					i += is.getAmount();
		}

		return (i >= menge);
	}

	public static boolean hasItemOnArmor(QuestPlayer qp, Material material, int menge, String name, byte data) {

		int i = 0;

		MaterialData dat = new MaterialData(material, data);
		for (ItemStack is : qp.getPlayer().getInventory().getArmorContents()) {
			if (is != null)
				if (is.getType() == material && is.getData() == dat && is.getItemMeta() != null
						&& is.getItemMeta().getDisplayName().equals(name))
					i += is.getAmount();
		}

        return i >= menge;
    }

	public static boolean hasItemOnArmor(QuestPlayer qp, Material material, int menge, String name) {

		int i = 0;

		for (ItemStack is : qp.getPlayer().getInventory().getArmorContents()) {
			if (is != null)
				if (is.getType() == material && is.getItemMeta() != null
						&& is.getItemMeta().getDisplayName().equals(name))
					i += is.getAmount();
		}

        return i >= menge;
    }

	public static boolean hasItemOnArmor(QuestPlayer qp, Material material, int menge) {

		int i = 0;

		for (ItemStack is : qp.getPlayer().getInventory().getArmorContents()) {
			if (is != null)
				if (is.getType() == material)
					i += is.getAmount();
		}

        return i >= menge;
    }

	public static boolean inFullArmor(QuestPlayer qp) {
        return qp.getPlayer().getInventory().getBoots() != null && qp.getPlayer().getInventory().getHelmet() != null && qp.getPlayer().getInventory().getChestplate() != null && qp.getPlayer().getInventory().getLeggings() != null;
	}

	public static boolean inFullArmorOfMaterial(QuestPlayer qp, String m) {

		if (qp.getPlayer().getInventory().getBoots() != null && qp.getPlayer().getInventory().getHelmet() != null
				&& qp.getPlayer().getInventory().getChestplate() != null
				&& qp.getPlayer().getInventory().getLeggings() != null) {
			{
				Material bootsMaterial, helmetMaterial, chestplateMaterial, leggingsMaterial;
				bootsMaterial = helmetMaterial = chestplateMaterial = leggingsMaterial = null;

				
				switch (m.toLowerCase()) {
				case "leather":
				case "leder":
					bootsMaterial = Material.LEATHER_BOOTS;
					helmetMaterial = Material.LEATHER_HELMET;
					chestplateMaterial = Material.LEATHER_CHESTPLATE;
					leggingsMaterial = Material.LEATHER_LEGGINGS;
					break;
				case "iron":
				case "eisen":
					bootsMaterial = Material.IRON_BOOTS;
					helmetMaterial = Material.IRON_HELMET;
					chestplateMaterial = Material.IRON_CHESTPLATE;
					leggingsMaterial = Material.IRON_LEGGINGS;
					break;
				case "gold":
					bootsMaterial = Material.GOLD_BOOTS;
					helmetMaterial = Material.GOLD_HELMET;
					chestplateMaterial = Material.GOLD_CHESTPLATE;
					leggingsMaterial = Material.GOLD_LEGGINGS;
					break;
				case "diamond":
				case "diamant":
					bootsMaterial = Material.DIAMOND_BOOTS;
					helmetMaterial = Material.DIAMOND_HELMET;
					chestplateMaterial = Material.DIAMOND_CHESTPLATE;
					leggingsMaterial = Material.DIAMOND_LEGGINGS;
					break;
				default:
					break;
				}

                return qp.getPlayer().getInventory().getBoots().getType() == bootsMaterial && qp.getPlayer().getInventory().getHelmet().getType() == helmetMaterial && qp.getPlayer().getInventory().getChestplate().getType() == chestplateMaterial && qp.getPlayer().getInventory().getLeggings().getType() == leggingsMaterial;
			}
		}

		return false;
	}

}
