package de.biomiaAPI.QuestEvents;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.biomiaAPI.Quests.QuestPlayer;
import de.biomiaAPI.itemcreator.ItemCreator;

public class GiveItemEvent implements Event {

	QuestPlayer qp;
	Material material;
	String name;
	int menge;

	ItemStack stack;

	public GiveItemEvent(Material material, String name, int menge) {
		this.material = material;
		this.menge = menge;
		this.name = name;
	}

	public GiveItemEvent(Material material, int menge) {
		this.material = material;
		this.menge = menge;
	}

	public GiveItemEvent(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public void executeEvent(QuestPlayer qp) {
		this.qp = qp;
		giveItem();
	}

	public void giveItem() {
		if (stack == null) {
			if (name != null) {
				qp.getPlayer().getInventory()
						.addItem(ItemCreator.setAmount(ItemCreator.itemCreate(material, name), menge));
			} else {
				qp.getPlayer().getInventory().addItem(ItemCreator.setAmount(ItemCreator.itemCreate(material), menge));
			}
		} else {
			qp.getPlayer().getInventory().addItem(stack);
		}
	}

}
