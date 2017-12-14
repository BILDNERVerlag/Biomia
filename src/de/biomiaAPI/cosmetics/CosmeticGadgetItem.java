package de.biomiaAPI.cosmetics;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.biomiaAPI.Biomia;
import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.Cosmetic.Group;
import de.biomiaAPI.main.Main;

public class CosmeticGadgetItem extends CosmeticItem implements Listener {

	GadgetListener gadgetListener;
	ItemStack gadgetItem;

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.hasItem() && e.getItem().equals(gadgetItem))
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
				try {
					gadgetListener.execute(Biomia.getBiomiaPlayer(e.getPlayer()));
				} catch (Exception ex) {
					e.getPlayer().sendMessage("§cListener not found!");
				}
	}

	public CosmeticGadgetItem(int id, String name, ItemStack is, Commonness c, ItemStack gadgetItem) {
		super(id, name, is, c, Group.GADGETS);
		gadgetListener = Cosmetic.getGadgetListener(id);
		this.gadgetItem = gadgetItem;
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}

	public void setGadgetListener(GadgetListener gadgetListener) {
		this.gadgetListener = gadgetListener;
	}

	public ItemStack getGadgetItem() {
		return gadgetItem;
	}

	public static void removeOne(BiomiaPlayer bp) {
		// TODO
	}

}
