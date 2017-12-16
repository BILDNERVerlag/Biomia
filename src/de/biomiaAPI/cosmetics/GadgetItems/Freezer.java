package de.biomiaAPI.cosmetics.GadgetItems;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.CosmeticGadgetItem;
import de.biomiaAPI.cosmetics.GadgetListener;

public class Freezer implements GadgetListener{
	
	@Override
	public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {
		Snowball e = (Snowball) bp.getPlayer().getWorld().spawnEntity(bp.getPlayer().getLocation(), EntityType.SNOWBALL);
		bp.getPlayer().launchProjectile(e.getClass()).setCustomName("Freezer");
		e.remove();
	}
	

}
