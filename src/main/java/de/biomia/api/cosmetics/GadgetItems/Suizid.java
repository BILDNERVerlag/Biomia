package de.biomia.api.cosmetics.GadgetItems;

import de.biomia.api.BiomiaPlayer;
import de.biomia.api.cosmetics.CosmeticGadgetItem;
import de.biomia.api.cosmetics.GadgetListener;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;

class Suizid implements GadgetListener {

	@Override
	public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {

		for (int i = 0; i < 20; i++) {
			TNTPrimed tnt = (TNTPrimed) bp.getPlayer().getWorld().spawnEntity(bp.getPlayer().getLocation(),
					EntityType.PRIMED_TNT);
			tnt.setFuseTicks(20);
			tnt.setYield(0);
			tnt.setGlowing(true);
			bp.getPlayer().addPassenger(tnt);
			item.removeOne(bp, true);
		}
	}

}
