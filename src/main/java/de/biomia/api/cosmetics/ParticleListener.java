package de.biomia.api.cosmetics;

import de.biomia.api.BiomiaPlayer;
import org.bukkit.scheduler.BukkitTask;

public interface ParticleListener {
		
	BukkitTask start(BiomiaPlayer bp, CosmeticParticleItem item);

}
