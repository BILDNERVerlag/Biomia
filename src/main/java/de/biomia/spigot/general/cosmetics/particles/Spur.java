package de.biomia.spigot.general.cosmetics.particles;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.general.cosmetics.items.CosmeticParticleItem;
import de.biomia.spigot.tools.Particles;
import net.minecraft.server.v1_12_R1.EnumParticle;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

class Spur implements ParticleListener {

    @Override
    public BukkitTask start(BiomiaPlayer bp, CosmeticParticleItem item) {

        return new BukkitRunnable() {

            int i = 0;

            @Override
            public void run() {

                if (i == 10) {
                    i = 0;
                    if (item.removeTime(bp, 1)) {
                        cancel();
                        return;
                    }
                }

                Location l = bp.getPlayer().getLocation().add(0, 0.1, 0);
                new Particles(EnumParticle.FLAME, l, false, 0, 0, 0, 1, 1).sendAll();
                i++;
            }

        }.runTaskTimer(Main.getPlugin(), 0, 2);
    }

}
