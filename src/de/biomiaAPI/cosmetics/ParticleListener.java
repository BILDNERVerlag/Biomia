package de.biomiaAPI.cosmetics;

import org.bukkit.scheduler.BukkitTask;

import de.biomiaAPI.BiomiaPlayer;

public interface ParticleListener {
		
	public BukkitTask start(BiomiaPlayer bp, CosmeticParticleItem item);

}
