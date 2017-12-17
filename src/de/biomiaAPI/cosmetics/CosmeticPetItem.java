package de.biomiaAPI.cosmetics;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.Cosmetic.Group;
import de.biomiaAPI.main.Main;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.PathEntity;

public class CosmeticPetItem extends CosmeticItem {

	private EntityType type;
	private static HashMap<BiomiaPlayer, Entity> pets = new HashMap<>();

	public static boolean isOwner(BiomiaPlayer bp, Entity pet) {
		return pets.containsKey(bp) ? (pets.get(bp) == pet) : false;
	}

	public static boolean isPet(Entity e) {
		return pets.containsValue(e);
	}

	public CosmeticPetItem(int id, String name, ItemStack is, Commonness c, EntityType type) {
		super(id, name, is, c, Group.PETS);
		this.type = type;
	}

	@Override
	public void remove(BiomiaPlayer bp) {
		if (pets.containsKey(bp)) {
			Entity e = pets.get(bp);
			e.remove();
			pets.remove(bp);
		}
	}

	@Override
	public void use(BiomiaPlayer bp) {
		remove(bp);
		Player p = bp.getPlayer();
		Entity entity = (Entity) p.getWorld().spawnEntity(p.getLocation(), type);
		entity.setCustomName("§8" + p.getName() + "'s Haustier");
		entity.setCustomNameVisible(true);
		pets.put(bp, entity);
		entity.addPassenger(p);

		new BukkitRunnable() {
			@Override
			public void run() {
				if (!entity.isDead() && entity != null) {
					if (!bp.getPlayer().isInsideVehicle())
						followPlayer((Creature) entity, p);
				} else {
					if (pets.containsKey(bp))
						pets.remove(bp);
					cancel();
				}
			}
		}.runTaskTimer(Main.plugin, 10, 10);
	}

	private void followPlayer(Creature creature, Player player) {
		Location location = player.getLocation();
		switch (new Random().nextInt(6)) {
		case 0:
			location.add(1.5, 0, 1.5);
			break;
		case 1:
			location.add(0, 0, 1.5);
			break;
		case 2:
			location.add(1.5, 0, 0);
			break;
		case 3:
			location.subtract(1.5, 0, 1.5);
			break;
		case 4:
			location.subtract(0, 0, 1.5);
			break;
		case 5:
			location.subtract(1.5, 0, 0);
			break;
		}

		if (!location.getWorld().equals(creature.getWorld()) || location.distanceSquared(creature.getLocation()) > 80) {
			if (!player.isOnGround()) {
				return;
			}
			creature.teleport(location);
		} else if (location.distanceSquared(creature.getLocation()) > 20) {
			net.minecraft.server.v1_12_R1.Entity pet = ((CraftEntity) creature).getHandle();
			((EntityInsentient) pet).getNavigation().a(2);
			Object objPet = ((CraftEntity) creature).getHandle();
			PathEntity path;
			path = ((EntityInsentient) objPet).getNavigation().a(location.getX() + 1, location.getY(),
					location.getZ() + 1);
			if (path != null) {
				((EntityInsentient) objPet).getNavigation().a(path, 1.0D);
				((EntityInsentient) objPet).getNavigation().a(2.0D);
			}
		}
	}

	public EntityType getEntityType() {
		return type;
	}

}
