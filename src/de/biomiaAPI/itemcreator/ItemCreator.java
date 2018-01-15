package de.biomiaAPI.itemcreator;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemCreator {

	public static ItemStack itemCreate(Material material, String name, short data) {

		ItemStack itemStack = new ItemStack(material, 1, data);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(name);
		itemStack.setItemMeta(itemMeta);

		return itemStack;

	}

	private static ItemStack itemCreate(Material material, short data) {
        return new ItemStack(material, 1, data);
	}

	public static ItemStack itemCreate(Material material, String name) {

		ItemStack itemStack = new ItemStack(material);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(name);
		itemStack.setItemMeta(itemMeta);

		return itemStack;

	}

	public static ItemStack itemCreate(Material material) {

        return new ItemStack(material);

	}

	public static ItemStack setAmount(ItemStack itemStack, int menge) {
		itemStack.setAmount(menge);
		return itemStack;
	}

	public static ItemStack setUnbreakable(ItemStack itemStack) {

		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setUnbreakable(true);
		itemStack.setItemMeta(itemMeta);

		return itemStack;

	}



	@SuppressWarnings("deprecation")
	public static ItemStack headWithSkin(String name) {
		ItemStack s = itemCreate(Material.SKULL_ITEM, (short) SkullType.PLAYER.ordinal());
		SkullMeta meta = (SkullMeta) s.getItemMeta();
		meta.setOwner(name);
		meta.setDisplayName(name);
		s.setItemMeta(meta);
		return s;
	}

	public static ItemStack headWithSkin(String name, String displayName) {
		ItemStack s = headWithSkin(name);
		SkullMeta meta = (SkullMeta) s.getItemMeta();
		meta.setDisplayName(displayName);
		s.setItemMeta(meta);
		return s;
	}

}
