package de.biomia.general.cosmetics.gadgets;

import de.biomia.BiomiaPlayer;
import de.biomia.Main;
import de.biomia.general.cosmetics.items.CosmeticGadgetItem;
import net.minecraft.server.v1_12_R1.AttributeInstance;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.PathEntity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

class CrazyCat implements GadgetListener {

    @Override
    public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {

        Player p = bp.getPlayer();
        Ocelot entity = (Ocelot) p.getWorld().spawnEntity(p.getLocation(), EntityType.OCELOT);
        entity.addPassenger(p);

        AttributeInstance attributes = ((CraftLivingEntity) entity).getHandle()
                .getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        attributes.setValue(5);

        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i == 20) {
                    entity.eject();
                    entity.remove();
                    cancel();
                } else {
                    weird(entity);
                    i++;
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 2);
        item.removeOne(bp, true);
    }

    private void weird(Entity creature) {
        Location location = creature.getLocation();
        switch (new Random().nextInt(6)) {
        case 0:
            location.add(30, 1, 30);
            break;
        case 1:
            location.add(0, 1, 30);
            break;
        case 2:
            location.add(30, 1, 0);
            break;
        case 3:
            location.subtract(30, 1, 30);
            break;
        case 4:
            location.subtract(0, 1, 30);
            break;
        case 5:
            location.subtract(30, 1, 0);
            break;

        }
        net.minecraft.server.v1_12_R1.Entity ocelot = ((CraftEntity) creature).getHandle();
        ((EntityInsentient) ocelot).getNavigation().a(2);
        Object objPet = ((CraftEntity) creature).getHandle();
        PathEntity path = ((EntityInsentient) objPet).getNavigation().a(location.getX() + 1, location.getY(),
                location.getZ() + 1);
        if (path != null) {
            ((EntityInsentient) objPet).getNavigation().a(path, 1.0D);
            ((EntityInsentient) objPet).getNavigation().a(2.0D);
        }
    }

}
