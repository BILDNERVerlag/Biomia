package de.biomiaAPI.cosmetics.GadgetItems;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.CosmeticGadgetItem;
import de.biomiaAPI.cosmetics.GadgetListener;

public class Firework implements GadgetListener {

	@Override
	public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {
		Location l = bp.getPlayer().getLocation();
		org.bukkit.entity.Firework fw = (org.bukkit.entity.Firework) l.getWorld().spawnEntity(l.add(0, 2, 0), EntityType.FIREWORK);
		fw.addPassenger(bp.getPlayer());
	}

}
