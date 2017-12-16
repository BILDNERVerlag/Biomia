package de.biomiaAPI.cosmetics.GadgetItems;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.CosmeticGadgetItem;
import de.biomiaAPI.cosmetics.GadgetListener;
import de.biomiaAPI.main.Main;

public class FastDayCircle implements GadgetListener {

	@Override
	public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {

		World w = bp.getPlayer().getWorld();

		new BukkitRunnable() {
			@Override
			public void run() {
				long time = w.getTime();
				bp.getPlayer().getWorld().setTime(time + 60);
				if (time == 6000) {
					cancel();
					return;
				} else if (time == 24000) {
					time = 0;
				}
			}
		}.runTaskTimer(Main.plugin, 0, 1);
	}
}
