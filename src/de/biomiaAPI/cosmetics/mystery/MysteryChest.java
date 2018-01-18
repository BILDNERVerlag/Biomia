package de.biomiaAPI.cosmetics.mystery;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.Cosmetic;
import de.biomiaAPI.cosmetics.CosmeticGadgetItem;
import de.biomiaAPI.cosmetics.CosmeticItem;
import de.biomiaAPI.cosmetics.CosmeticItem.Commonness;
import de.biomiaAPI.cosmetics.CosmeticParticleItem;
import de.biomiaAPI.itemcreator.ItemCreator;
import de.biomiaAPI.main.Main;

class MysteryChest {

	private static Commonness determineCommonness() {
		// Prozentchancen:
		// VERY_COMMON: 60
		// COMMON: 30
		// RARE: 9
		// VERY_RARE: 1

		int random = new Random().nextInt(100) + 1;
		Commonness c;

		if (random > 40)
			c = Commonness.VERY_COMMON;
		else if (random > 10)
			c = Commonness.COMMON;
		else if (random > 2)
			c = Commonness.RARE;
		else
			c = Commonness.VERY_RARE;

		return c;
	}

	public static void open(BiomiaPlayer bp) {

		Commonness c;
		ArrayList<CosmeticItem> items;

		do {
			c = determineCommonness();
			items = Cosmetic.getItemsOfCommonness(c);
		} while (items.size() == 0);

		int i = new Random().nextInt(items.size());
		CosmeticItem item = items.get(i);

		Inventory inv = Bukkit.createInventory(null, 27, "\u00A74Mysteri\u00f6se Box \u00A78- \u00A74Dein Gewinn:");
		ItemStack itemStack = item.getItem().clone();

		int menge = 0;
		if (item instanceof CosmeticGadgetItem)
			switch (new Random().nextInt(6)) {
			case 0:
				menge = 10;
				break;
			case 1:
				menge = 15;
				break;
			case 2:
				menge = 20;
				break;
			case 3:
				menge = 25;
				break;
			case 4:
				menge = 30;
				break;
			default:
				break;
			}
		else if (item instanceof CosmeticParticleItem)
			switch (new Random().nextInt(6)) {
			case 0:
				menge = 60;
				break;
			case 1:
				menge = 100;
				break;
			case 2:
				menge = 150;
				break;
			case 3:
				menge = 180;
				break;
			case 4:
				menge = 200;
				break;
			case 5:
				menge = 240;
				break;
			default:
				break;
			}
		else
			menge = -1;
		Cosmetic.setLimit(bp, item.getID(), menge);
		if (menge != -1)
			itemStack.setAmount(menge);

		bp.getPlayer().openInventory(inv);

		new BukkitRunnable() {
			int counter = -1;

			@Override
			public void run() {
				counter++;
				ItemStack is = ItemCreator.itemCreate(Material.BLACK_GLAZED_TERRACOTTA);
				ItemStack is2 = ItemCreator.itemCreate(Material.AIR);
				for (int index = counter; index < 27; index++) {
                    inv.setItem(index, is);
				}
				for (int index2 = 0; index2 < counter; index2++) {
					inv.setItem(index2, is2);
				}
				if (counter >= 14) {
					inv.setItem(13, itemStack);
				}
				if (counter >= 27) {
					this.cancel();
				}
			}
		}.runTaskTimer(Main.plugin, 0, 2);
	}
}
