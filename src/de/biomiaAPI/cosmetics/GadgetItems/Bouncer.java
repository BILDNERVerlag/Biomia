package de.biomiaAPI.cosmetics.GadgetItems;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.CosmeticGadgetItem;
import de.biomiaAPI.cosmetics.GadgetListener;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

class Bouncer implements GadgetListener {

	@Override
	public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {
		Player p = bp.getPlayer();
		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 30, 30, false, false));
		item.removeOne(bp, true);
	}
}
