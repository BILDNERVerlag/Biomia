package de.biomia.general.cosmetics.particles;

import de.biomia.BiomiaPlayer;
import de.biomia.general.cosmetics.items.CosmeticParticleItem;
import org.bukkit.scheduler.BukkitTask;

public interface ParticleListener {

    BukkitTask start(BiomiaPlayer bp, CosmeticParticleItem item);

}
