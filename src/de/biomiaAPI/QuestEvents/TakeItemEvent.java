package de.biomiaAPI.QuestEvents;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.biomiaAPI.Quests.QuestPlayer;

public class TakeItemEvent implements Event {

	QuestPlayer qp = null;
	Material material = null;
	String name = null;
	int menge = 0;

	public TakeItemEvent(Material material, String name, int menge) {
		this.material = material;
		this.menge = menge;
		this.name = name;
	}

	public TakeItemEvent(Material material, int menge) {
		this.material = material;
		this.menge = menge;
	}

	@Override
	public void executeEvent(QuestPlayer qp) {
		this.qp = qp;
		takeItem();
	}

	public void takeItem() {

		int i = 0;

		for (ItemStack is : qp.getPlayer().getInventory().getContents()) {
			if (is != null)
				if (is.getType() == material)
					if (name != null) {
						if (is.getItemMeta().getDisplayName().equals(name))
							if (is.getAmount() >= menge || is.getAmount() >= menge - i) {
								is.setAmount(is.getAmount() - (menge - i));
								return;
							} else {
								i += is.getAmount();
								is.setAmount(0);
							}
					} else if (is.getAmount() >= menge || is.getAmount() >= menge - i) {
						is.setAmount(is.getAmount() - (menge - i));
						return;
					} else {
						i += is.getAmount();
						is.setAmount(0);
					}
		}

	}
}
