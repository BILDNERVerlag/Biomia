package de.biomiaAPI.cosmetics;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.Cosmetic.Group;

public class CosmeticHeadItem extends CosmeticItem {

	private ItemStack head;

	public CosmeticHeadItem(int id, String name, ItemStack is, Commonness c, ItemStack head) {
		super(id, name, is, c, Group.HEADS);
		this.head = head;
	}

	@Override
	public void use(BiomiaPlayer bp) {
		Player p = bp.getPlayer();
		p.getInventory().setHelmet(head);
	}
	
	public ItemStack getHead() {
		return head;
	}
	
}
