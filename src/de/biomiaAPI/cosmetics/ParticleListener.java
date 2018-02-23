package de.biomiaAPI.cosmetics;

import de.biomiaAPI.BiomiaPlayer;
import org.bukkit.scheduler.BukkitTask;

public interface ParticleListener {
		
	BukkitTask start(BiomiaPlayer bp, CosmeticParticleItem item);

}
