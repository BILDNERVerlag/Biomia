package de.biomiaAPI.cosmetics;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.Cosmetic.Group;

public class CosmeticParticleItem extends CosmeticItem {

	public static HashMap<BiomiaPlayer, BukkitTask> actives = new HashMap<>();

	private ParticleListener particleListener = Cosmetic.getParticleListener(getID());

	public void activate(BiomiaPlayer bp) {
		bp.getPlayer().sendMessage(getName() + " §8wurde §aAktiviert§8!");

		if (actives.containsKey(bp)) {
			actives.get(bp).cancel();
		}
		actives.put(bp, particleListener.start(bp));

	}

	public void deaktivate(BiomiaPlayer bp) {
		bp.getPlayer().sendMessage(getName() + " §8wurde §cDeaktiviert§8!");
		if (actives.containsKey(bp)) {
			actives.get(bp).cancel();
			actives.remove(bp);
		}
	}

	public CosmeticParticleItem(int id, String name, ItemStack is, Commonness c) {
		super(id, name, is, c, Group.PARTICLES);
	}
}