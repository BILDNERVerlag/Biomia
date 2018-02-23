package de.biomiaAPI.QuestEvents;

import de.biomiaAPI.QuestConditions.ItemConditions;
import de.biomiaAPI.Quests.QuestPlayer;
import org.bukkit.inventory.ItemStack;

public class GiveItemIfNotInInventoryEvent implements Event {

	private QuestPlayer qp;

	private final ItemStack stack;

	public GiveItemIfNotInInventoryEvent(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public void executeEvent(QuestPlayer qp) {
		this.qp = qp;
		giveItem();
	}

	private void giveItem() {
		if (!ItemConditions.hasItemInInventory(qp, stack.getType(), stack.getAmount(), stack.getItemMeta().getDisplayName())) {
			qp.getPlayer().getInventory().addItem(stack);
		}
	}

}
