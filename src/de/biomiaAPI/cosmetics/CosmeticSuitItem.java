package de.biomiaAPI.cosmetics;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.Cosmetic.Group;

public class CosmeticSuitItem extends CosmeticItem {

	private ItemStack helmet = null;
	private ItemStack chestplate = null;
	private ItemStack leggins = null;
	private ItemStack boots = null;

	public CosmeticSuitItem(int id, String name, ItemStack is, Commonness c) {
		super(id, name, is, c, Group.SUITS);
	}

	public ItemStack getHelmet() {
		return helmet;
	}

	public void setHelmet(ItemStack helmet) {
		this.helmet = helmet;
	}

	public ItemStack getChestplate() {
		return chestplate;
	}

	public void setChestplate(ItemStack chestplate) {
		this.chestplate = chestplate;
	}

	public ItemStack getLeggings() {
		return leggins;
	}

	public void setLeggins(ItemStack leggins) {
		this.leggins = leggins;
	}

	public ItemStack getBoots() {
		return boots;
	}

	public void setBoots(ItemStack boots) {
		this.boots = boots;
	}

	@Override
	public void use(BiomiaPlayer bp) {
		Player p = bp.getPlayer();
		p.getInventory().setHelmet(getHelmet());
		p.getInventory().setChestplate(getChestplate());
		p.getInventory().setLeggings(getLeggings());
		p.getInventory().setBoots(getBoots());
	}
}
