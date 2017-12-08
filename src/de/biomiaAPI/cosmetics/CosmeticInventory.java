package de.biomiaAPI.cosmetics;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.biomiaAPI.Biomia;
import de.biomiaAPI.cosmetics.Cosmetic.Group;
import de.biomiaAPI.itemcreator.ItemCreator;
import de.biomiaAPI.main.Main;

public class CosmeticInventory implements Listener {

	private ArrayList<CosmeticItem> cosmeticItems = new ArrayList<>();	
	private Inventory inv;
	public ArrayList<ItemStack> items = new ArrayList<>();
	private ItemStack next;
	private ItemStack back;
	private int side = 0;
	private int items_per_side = 18;

	public CosmeticInventory(ArrayList<CosmeticItem> items) {
		this.cosmeticItems = items;
		inv = Bukkit.createInventory(null, 27, "Cosmetics");
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}


	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().equals(inv)) {
			e.setCancelled(true);
			if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {

				for (CosmeticItem item : cosmeticItems) {
					if (item.getItem().equals(e.getCurrentItem())) {
						item.use(Biomia.getBiomiaPlayer((Player) e.getWhoClicked()));
					}
				}

				if (e.getCurrentItem().equals(back)) {
					displaySide(side - 1);
				} else if (e.getCurrentItem().equals(next)) {
					displaySide(side + 1);
				}
			}
		}
	}

	private void displaySide(int i) {
		inv.clear();
		side = i;
		if (items.size() - side - 1 * items_per_side > side * items_per_side)
			setNext();
		if (side > 0)
			setBack();
		int from = side * items_per_side;
		int to = (side + 1) * items_per_side;
		int actualItem = 0;
		for (ItemStack is : items.subList(from, (to > items.size() ? items.size() : to))) {
			inv.setItem(actualItem, is);
			actualItem++;
		}

	}

	private void setNext() {
		if (next == null)
			next = ItemCreator.itemCreate(Material.BLAZE_ROD, "§aNächste Seite");
		inv.setItem(inv.getSize() - 2, next);
	}

	private void setBack() {
		if (back == null)
			back = ItemCreator.itemCreate(Material.STICK, "§aLetzte Seite");
		inv.setItem(inv.getSize() - 8, back);
	}

	public void openInventorry(Player p, Group group) {

		items.clear();
		for (CosmeticItem cosmeticitem : cosmeticItems)
			if (cosmeticitem.getGroup() == group)
				items.add(cosmeticitem.getItem());
		displaySide(0);
		p.openInventory(inv);
	}
}
