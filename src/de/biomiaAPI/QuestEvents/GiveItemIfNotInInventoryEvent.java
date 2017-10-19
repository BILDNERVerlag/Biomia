package de.biomiaAPI.QuestEvents;

import org.bukkit.inventory.ItemStack;

import de.biomiaAPI.QuestConditions.ItemConditions;
import de.biomiaAPI.Quests.QuestPlayer;

public class GiveItemIfNotInInventoryEvent implements Event {

	QuestPlayer qp;

	ItemStack stack;

	public GiveItemIfNotInInventoryEvent(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public void executeEvent(QuestPlayer qp) {
		this.qp = qp;
		giveItem();
	}

	public void giveItem() {
		if (!ItemConditions.hasItemInInventory(qp, stack.getType(), stack.getAmount(), stack.getItemMeta().getDisplayName())) {
			qp.getPlayer().getInventory().addItem(stack);
		}
	}

}
