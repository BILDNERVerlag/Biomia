package de.biomia.spigot.general.cosmetics.particles;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.general.cosmetics.items.CosmeticParticleItem;
import org.bukkit.scheduler.BukkitTask;

public interface ParticleListener {

    BukkitTask start(BiomiaPlayer bp, CosmeticParticleItem item);

}
