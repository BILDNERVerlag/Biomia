package de.biomiaAPI.cosmetics.ParticleItems;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.CosmeticParticleItem;
import de.biomiaAPI.cosmetics.ParticleListener;
import de.biomiaAPI.main.Main;
import de.biomiaAPI.tools.Particles;
import net.minecraft.server.v1_12_R1.EnumParticle;

public class Spur implements ParticleListener {

	@Override
	public BukkitTask start(BiomiaPlayer bp, CosmeticParticleItem item) {

		return new BukkitRunnable() {

			int i = 0;

			@Override
			public void run() {

				if (i == 10) {
					i = 0;
					if (!item.removeTime(bp, 1)) {
						cancel();
						return;
					}
				}

				Location l = bp.getPlayer().getLocation().add(0, 0.1, 0);
				new Particles(EnumParticle.FLAME, l, false, 0, 0, 0, 1, 1).sendAll();
				i++;
			}

		}.runTaskTimer(Main.plugin, 0, 2);
	}

}