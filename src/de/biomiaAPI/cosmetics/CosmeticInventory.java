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
	private CosmeticGroup group;
	private ArrayList<ItemStack> items = new ArrayList<>();
	private ItemStack next;
	private ItemStack back;
	private ItemStack remove;
	private int side = 0;
	private int items_per_side = 18;

	@SuppressWarnings("unchecked")
	public CosmeticInventory(ArrayList<? super CosmeticItem> items, CosmeticGroup group) {
		this.group = group;
		this.cosmeticItems = (ArrayList<CosmeticItem>) items;
		inv = Bukkit.createInventory(null, 27, "Cosmetics");
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {

		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();

			if (e.getInventory().equals(inv)) {
				e.setCancelled(true);
				if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {

					for (CosmeticItem item : cosmeticItems) {
						if (item.getItem().getItemMeta().getDisplayName().equals(e.getCurrentItem().getItemMeta().getDisplayName())) {
							item.use(Biomia.getBiomiaPlayer((Player) e.getWhoClicked()));
							p.closeInventory();
						}
					}

					if (e.getCurrentItem().equals(back)) {
						displaySide(side - 1);
					} else if (e.getCurrentItem().equals(next)) {
						displaySide(side + 1);
					} else if (e.getCurrentItem().equals(remove)) {
						group.remove(Biomia.getBiomiaPlayer(p));
					}
				}
			}
		}
	}

	private void displaySide(int i) {
		inv.clear();
		side = i;

		setRemove();

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

	private void setRemove() {
		if (remove == null)
			remove = ItemCreator.itemCreate(Material.BARRIER, "§cEntfernen");
		inv.setItem(inv.getSize() - 5, remove);
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
	
	public void removeItem(int id) {
		for (CosmeticItem cosmeticitem : cosmeticItems) {
			if(cosmeticitem.getID() == id) {
				cosmeticItems.remove(cosmeticitem);
			}
		}
	}
}
