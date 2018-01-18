package de.biomiaAPI.cosmetics;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.biomiaAPI.Biomia;
import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.QuestEvents.TakeItemEvent;
import de.biomiaAPI.cosmetics.Cosmetic.Group;
import de.biomiaAPI.main.Main;

public class CosmeticGadgetItem extends CosmeticItem implements Listener {

	private GadgetListener gadgetListener;
	private final ItemStack gadgetItem;

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {

		if (e.hasItem() && e.getItem().hasItemMeta()
				&& e.getItem().getItemMeta().getDisplayName().equals(gadgetItem.getItemMeta().getDisplayName())
				&& e.getItem().getType().equals(gadgetItem.getType()))
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
				try {
					gadgetListener.execute(Biomia.getBiomiaPlayer(e.getPlayer()), this);
				} catch (Exception ex) {
					e.getPlayer().sendMessage("\u00A7cListener wurde nicht gefunden!");
					ex.printStackTrace();
				}
	}

	@Override
	public void use(BiomiaPlayer bp) {
		ItemStack is = gadgetItem.clone();
		int limit = Cosmetic.getLimit(bp, getID());
		if (limit != -1 && is.getType() != Material.FISHING_ROD)
			is.setAmount(Cosmetic.getLimit(bp, getID()));
		bp.getPlayer().getInventory().setItem(Cosmetic.gadgetSlot, is);
	}

	@Override
    public void remove(BiomiaPlayer bp) {
		bp.getPlayer().getInventory().setItem(Cosmetic.gadgetSlot, null);
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

	public void removeOne(BiomiaPlayer bp, boolean removeItem) {
		int limit = Cosmetic.getLimit(bp, getID());

        switch (limit) {
            case -1:
                // 'Infinity'
                break;
            case 0:
                Cosmetic.setLimit(bp, getID(), 0);
                remove(bp);
                break;
            default:
                Cosmetic.setLimit(bp, getID(), --limit);
                if (removeItem)
                    removeItemFromInventory(bp);
                break;
        }
	}

	private void removeItemFromInventory(BiomiaPlayer bp) {
		new TakeItemEvent(getGadgetItem().getType(), getGadgetItem().getItemMeta().getDisplayName(), 1)
				.executeEvent(bp.getQuestPlayer());
	}
}