package de.biomia.api.cosmetics.GadgetItems;

import de.biomia.api.BiomiaPlayer;
import de.biomia.api.cosmetics.CosmeticGadgetItem;
import de.biomia.api.cosmetics.GadgetListener;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

class Booster implements GadgetListener {

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
