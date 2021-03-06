package de.biomia.spigot.general.cosmetics.gadgets;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.general.cosmetics.items.CosmeticGadgetItem;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;

class Suizid implements GadgetListener {

    @Override
    public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {

        for (int i = 0; i < 20; i++) {
            TNTPrimed tnt = (TNTPrimed) bp.getPlayer().getWorld().spawnEntity(bp.getPlayer().getLocation(),
                    EntityType.PRIMED_TNT);
            tnt.setFuseTicks(20);
            tnt.setYield(0);
            tnt.setGlowing(true);
            bp.getPlayer().addPassenger(tnt);
            item.removeOne(bp, true);
        }
    }

}
