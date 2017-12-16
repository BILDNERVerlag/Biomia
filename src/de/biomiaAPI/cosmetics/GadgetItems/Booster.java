package de.biomiaAPI.cosmetics.GadgetItems;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.CosmeticGadgetItem;
import de.biomiaAPI.cosmetics.GadgetListener;

public class Booster implements GadgetListener {

	@Override
	public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {
		Player p = bp.getPlayer();

		if (p.isOnGround()) {
			p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, 1, 0);
			Vector jump = p.getLocation().getDirection().multiply(4D).setY(3);
			p.setVelocity(p.getVelocity().add(jump));
			item.removeOne(bp, true);
		}
	}

}
