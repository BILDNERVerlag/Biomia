package de.biomia.spigot.general.cosmetics.gadgets;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.general.cosmetics.items.CosmeticGadgetItem;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;

class TNTCannon implements GadgetListener {

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
        }.runTaskTimer(Main.getPlugin(), 0, 20);
        item.removeOne(bp, true);
    }
}
