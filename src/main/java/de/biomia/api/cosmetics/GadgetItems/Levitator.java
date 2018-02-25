package de.biomia.api.cosmetics.GadgetItems;

import de.biomia.api.BiomiaPlayer;
import de.biomia.api.cosmetics.CosmeticGadgetItem;
import de.biomia.api.cosmetics.GadgetListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

class Levitator implements GadgetListener{

	@Override
	public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {
		Player p = bp.getPlayer();
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 15 * 20, 0, false, false));
		for (Entity e : p.getNearbyEntities(50, 100, 50)) {
			if(e instanceof Player){
				((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 15 * 20, 0, false, false));
			}
		}
		item.removeOne(bp, true);
	}
	
}
