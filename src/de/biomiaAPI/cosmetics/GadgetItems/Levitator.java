package de.biomiaAPI.cosmetics.GadgetItems;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.CosmeticGadgetItem;
import de.biomiaAPI.cosmetics.GadgetListener;

public class Levitator implements GadgetListener{

	@Override
	public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {
		Player p = bp.getPlayer();
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 15 * 20, 0, false, false));
		for (Entity e : p.getNearbyEntities(50, 100, 50)) {
			if(e instanceof Player){
				((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 15 * 20, 0, false, false));
			}
		}
		
	}
	
}
