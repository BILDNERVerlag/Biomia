package de.biomia.general.cosmetics.GadgetItems;

import de.biomia.BiomiaPlayer;
import de.biomia.general.cosmetics.CosmeticGadgetItem;
import de.biomia.general.cosmetics.GadgetListener;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;

class Freezer implements GadgetListener{
	
	@Override
	public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {
		Snowball e = (Snowball) bp.getPlayer().getWorld().spawnEntity(bp.getPlayer().getLocation(), EntityType.SNOWBALL);
		bp.getPlayer().launchProjectile(e.getClass()).setCustomName("Freezer");
		e.remove();
		item.removeOne(bp, true);
	}
	

}
