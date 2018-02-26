package de.biomia.demoserver.main;

import de.biomia.demoserver.config.Bauten;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.api.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ScrollingInventory implements Listener {
	private Inventory inv;
	public ArrayList<ItemStack> items = new ArrayList<>();
	private ItemStack next;
	private ItemStack back;
	private int side = 0;
	private int items_per_side;

	ScrollingInventory(String name, int reihen) {
		inv = Bukkit.createInventory(null, reihen * 9 + 9, name);
		this.items_per_side = reihen * 9;
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
	}

	public void addItem(ItemStack is) {
		items.add(is);
	}

	public void removeItem(ItemStack is) {
		items.remove(is);
	}

	private void clearItems() {
		for (ItemStack is : inv.getContents()) {
			inv.remove(is);
		}
	}

	public void clearAllItems() {
		items.clear();
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (e.getInventory().equals(inv))
			HandlerList.unregisterAll(this);
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().equals(inv)) {
			e.setCancelled(true);
			if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()) {
				for (Bauten b : WeltenlaborMain.bauten)
					if (b.getName().equals(e.getCurrentItem().getItemMeta().getDisplayName())) {
						e.getWhoClicked().teleport(b.getLoc());
						return;
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
		clearItems();
		side = i;
		if (items.size() - side - items_per_side > side * items_per_side)
			setNext();
		if (side > 0)
			setBack();
		int from = side * items_per_side;
		int to = (side + 1) * items_per_side;
		int a = 0;
		for (ItemStack is : items.subList(from, (to > items.size() ? items.size() : to))) {
			inv.setItem(a, is);
			a++;
		}

	}

	public void openCopy(Player p) {
		ScrollingInventory sinv = new ScrollingInventory(inv.getName(), items_per_side / 9);

		items.forEach(sinv::addItem);

		sinv.openInventorry(p);

	}

	private void setNext() {
		if (next == null)
			next = ItemCreator.itemCreate(Material.BLAZE_ROD, "00A7aN\u00e4chste Seite");
		inv.setItem(inv.getSize() - 2, next);
	}

	private void setBack() {
		if (back == null)
			back = ItemCreator.itemCreate(Material.STICK, "00A7aLetzte Seite");
		inv.setItem(inv.getSize() - 8, back);
	}

	public void openInventorry(Player p) {
		displaySide(0);
		p.openInventory(inv);
	}
}
