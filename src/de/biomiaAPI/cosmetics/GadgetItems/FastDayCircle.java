package de.biomiaAPI.cosmetics.GadgetItems;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.CosmeticGadgetItem;
import de.biomiaAPI.cosmetics.GadgetListener;
import de.biomiaAPI.main.Main;

public class FastDayCircle implements GadgetListener {

	private boolean isRunning = false;

	@Override
	public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {

		if (isRunning) {
			bp.getPlayer().sendMessage("�cDer Tag-Beschleuniger l�uft bereits!");
			return;
		}
		isRunning = true;
		World w = bp.getPlayer().getWorld();
		item.removeOne(bp, true);

		new BukkitRunnable() {
			@Override
			public void run() {
				long time = w.getTime();
				w.setTime(time += 60);
				if (time == 6000) {
					cancel();
					isRunning = false;
					return;
				} else if (time == 24000) {
					time = 0;
				}
			}
		}.runTaskTimer(Main.plugin, 0, 1);
	}
}
