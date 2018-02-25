package de.biomia.api.cosmetics.GadgetItems;

import de.biomia.api.BiomiaPlayer;
import de.biomia.api.cosmetics.CosmeticGadgetItem;
import de.biomia.api.cosmetics.GadgetListener;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;

class PaintballGun implements GadgetListener{
	
	@Override
	public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {
		Snowball e = (Snowball) bp.getPlayer().getWorld().spawnEntity(bp.getPlayer().getLocation(), EntityType.SNOWBALL);
		bp.getPlayer().launchProjectile(e.getClass()).setCustomName("Paintball");
		e.remove();
		item.removeOne(bp, true);
	}
}
