package de.biomiaAPI.cosmetics.GadgetItems;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.CosmeticGadgetItem;
import de.biomiaAPI.cosmetics.GadgetListener;

public class Switcher implements GadgetListener {

	@Override
	public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {
		FishHook hook = (FishHook) bp.getPlayer().getWorld().spawnEntity(bp.getPlayer().getLocation(),
				EntityType.FISHING_HOOK);
		FishHook hook2 = bp.getPlayer().launchProjectile(hook.getClass());
		hook.remove();
		hook2.setShooter(bp.getPlayer());
		hook2.setCustomName("Switcher");
		item.removeOne(bp, false);
	}

}
