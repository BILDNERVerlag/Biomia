package de.biomiaAPI.cosmetics.GadgetItems;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.CosmeticGadgetItem;
import de.biomiaAPI.cosmetics.GadgetListener;
import de.biomiaAPI.main.Main;

public class TNTCanon implements GadgetListener {

	@Override
	public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {
		Player p = bp.getPlayer();
		new BukkitRunnable() {
			int i = 1;
			boolean onHead = false;
			TNTPrimed tnt;

			@Override
			public void run() {

				if (i == 20) {
					cancel();
					return;
				}

				if (onHead) {
					p.eject();
					tnt.setVelocity(p.getVelocity().multiply(3D).setY(2));
				} else {
					tnt = (TNTPrimed) p.getWorld().spawnEntity(p.getLocation().add(0, 1, 0), EntityType.PRIMED_TNT);
					tnt.setYield(0);
					p.addPassenger(tnt);
				}

				onHead = !onHead;
				i++;
			}
		}.runTaskTimer(Main.plugin, 0, 20);
	}
}
