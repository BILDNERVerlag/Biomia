package de.biomia.spigot.general.cosmetics.gadgets;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.general.cosmetics.items.CosmeticGadgetItem;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

class Firework implements GadgetListener {

    @Override
    public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {
        if (!bp.getPlayer().isInsideVehicle()) {
            Location l = bp.getPlayer().getLocation();
            org.bukkit.entity.Firework fw = (org.bukkit.entity.Firework) l.getWorld().spawnEntity(l.add(0, 2, 0),
                    EntityType.FIREWORK);
            fw.addPassenger(bp.getPlayer());
            item.removeOne(bp, true);
        }
    }

}
