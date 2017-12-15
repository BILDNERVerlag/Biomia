package de.biomiaAPI.cosmetics.GadgetItems;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.GadgetListener;

public class PaintballGun implements GadgetListener{
	
	@Override
	public void execute(BiomiaPlayer bp) {
		Snowball e = (Snowball) bp.getPlayer().getWorld().spawnEntity(bp.getPlayer().getLocation(), EntityType.SNOWBALL);
		bp.getPlayer().launchProjectile(e.getClass()).setCustomName("Paintball");
		e.remove();
	}
}
