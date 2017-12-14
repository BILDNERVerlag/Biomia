package de.biomiaAPI.cosmetics;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.Cosmetic.Group;

public class CosmeticParticleItem extends CosmeticItem {

	public static HashMap<BiomiaPlayer, BukkitTask> actives = new HashMap<>();
	private ParticleListener particleListener = Cosmetic.getParticleListener(getID());

	@Override
	public void use(BiomiaPlayer bp) {
		bp.getPlayer().sendMessage(getName() + " §8wurde §aAktiviert§8!");
		if (CosmeticParticleItem.actives.containsKey(bp)) {
			CosmeticParticleItem.actives.get(bp).cancel();
		}
		CosmeticParticleItem.actives.put(bp, particleListener.start(bp));
	}

	@Override
	public void remove(BiomiaPlayer bp) {
		bp.getPlayer().sendMessage(getName() + " §8wurde §cDeaktiviert§8!");
		if (CosmeticParticleItem.actives.containsKey(bp)) {
			CosmeticParticleItem.actives.get(bp).cancel();
			CosmeticParticleItem.actives.remove(bp);
		}
	}

	public CosmeticParticleItem(int id, String name, ItemStack is, Commonness c) {
		super(id, name, is, c, Group.PARTICLES);
	}
}