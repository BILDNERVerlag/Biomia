package de.biomia.api.cosmetics.GadgetItems;

import de.biomia.api.BiomiaPlayer;
import de.biomia.api.cosmetics.CosmeticGadgetItem;
import de.biomia.api.cosmetics.GadgetListener;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

class Firework implements GadgetListener {

	@Override
	public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {
		if (!bp.getPlayer().isInsideVehicle()) {
			Location l = bp.getPlayer().getLocation();
			org.bukkit.entity.Firework fw = (org.bukkit.entity.Firework) l.getWorld().spawnEntity(l.add(0, 2, 0),
					EntityType.FIREWORK);
			fw.addPassenger(bp.getPlayer());
			item.removeOne(bp, true);
		}
	}

}
