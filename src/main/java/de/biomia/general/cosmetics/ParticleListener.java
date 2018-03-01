package de.biomia.general.cosmetics;

import de.biomia.BiomiaPlayer;
import org.bukkit.scheduler.BukkitTask;

public interface ParticleListener {
		
	BukkitTask start(BiomiaPlayer bp, CosmeticParticleItem item);

}
