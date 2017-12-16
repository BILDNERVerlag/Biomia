package de.biomiaAPI.cosmetics.GadgetItems;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.CosmeticGadgetItem;
import de.biomiaAPI.cosmetics.GadgetListener;
import de.biomiaAPI.main.Main;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.PathEntity;

public class Witch implements GadgetListener {

	@Override
	public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {

		ArrayList<Bat> bats = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			Bat bat = (Bat) bp.getPlayer().getWorld().spawnEntity(bp.getPlayer().getLocation(), EntityType.BAT);
			bats.add(bat);
			new BukkitRunnable() {
				int i = 0;

				@Override
				public void run() {
					if (i == 120) {
						for (Bat b : bats)
							b.remove();
						cancel();
						return;
					}
					followPlayer(bat, bp.getPlayer());
					i++;
				}
			}.runTaskTimer(Main.plugin, 0, 10);
		}

	}

	private void followPlayer(Entity creature, Player player) {
		Location location = player.getLocation();
		switch (new Random().nextInt(6)) {
		case 0:
			location.add(1.5, 3, 1.5);
			break;
		case 1:
			location.add(0, 3, 1.5);
			break;
		case 2:
			location.add(1.5, 3, 0);
			break;
		case 3:
			location.subtract(1.5, -3, 1.5);
			break;
		case 4:
			location.subtract(0, -3, 1.5);
			break;
		case 5:
			location.subtract(1.5, -3, 0);
			break;
		}
		if (!location.getWorld().equals(creature.getWorld()) || location.distanceSquared(creature.getLocation()) > 80) {
			creature.teleport(location);
		}
		net.minecraft.server.v1_12_R1.Entity pet = ((CraftEntity) creature).getHandle();
		((EntityInsentient) pet).getNavigation().a(2);
		Object objPet = ((CraftEntity) creature).getHandle();
		PathEntity path;
		path = ((EntityInsentient) objPet).getNavigation().a(location.getX() + 1, location.getY(), location.getZ() + 1);
		if (path != null) {
			((EntityInsentient) objPet).getNavigation().a(path, 1.0D);
			((EntityInsentient) objPet).getNavigation().a(2.0D);
		}
	}

}
