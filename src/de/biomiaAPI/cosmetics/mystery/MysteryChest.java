package de.biomiaAPI.cosmetics.mystery;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.Cosmetic;
import de.biomiaAPI.cosmetics.CosmeticGadgetItem;
import de.biomiaAPI.cosmetics.CosmeticItem;
import de.biomiaAPI.cosmetics.CosmeticItem.Commonness;
import de.biomiaAPI.cosmetics.CosmeticParticleItem;

public class MysteryChest {

	public static void open(BiomiaPlayer bp) {

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

		ArrayList<CosmeticItem> items = Cosmetic.getItemsOfCommonnes(c);

		while (items.size() == 0) {
			random = new Random().nextInt(100) + 1;
			
			if (random > 40)
				c = Commonness.VERY_COMMON;
			else if (random > 10)
				c = Commonness.COMMON;
			else if (random > 2)
				c = Commonness.RARE;
			else
				c = Commonness.VERY_RARE;
			items = Cosmetic.getItemsOfCommonnes(c);
		}

		int i = new Random().nextInt(items.size());
		CosmeticItem item = (CosmeticItem) items.get(i);

		Inventory inv = Bukkit.createInventory(null, 27, "�4Mysterische Box �8- �4Gewinn");
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
		inv.setItem(13, itemStack);
		bp.getPlayer().openInventory(inv);
	}
}