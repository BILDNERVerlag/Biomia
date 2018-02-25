package de.biomia.api.cosmetics.GadgetItems;

import de.biomia.api.BiomiaPlayer;
import de.biomia.api.cosmetics.CosmeticGadgetItem;
import de.biomia.api.cosmetics.GadgetListener;
import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

class Switcher implements GadgetListener, Listener {

	@Override
	public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {
		item.removeOne(bp, false);
		// Nothing
	}

	@EventHandler
	public void onLaunch(ProjectileLaunchEvent e) {
		if (e.getEntity() instanceof FishHook) {
			if (((Player) e.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().getDisplayName()
					.contains("Tauscher")) {
				Player p = (Player) e.getEntity().getShooter();

				if (p.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD) {
					e.getEntity().setCustomName("Switcher");
				}
			}
		}
	}
}
