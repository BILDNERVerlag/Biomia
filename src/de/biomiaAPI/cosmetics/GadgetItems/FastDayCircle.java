package de.biomiaAPI.cosmetics.GadgetItems;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.CosmeticGadgetItem;
import de.biomiaAPI.cosmetics.GadgetListener;
import de.biomiaAPI.main.Main;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

class FastDayCircle implements GadgetListener {

    private boolean isRunning = false;

    @Override
    public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {

        if (isRunning) {
            bp.getPlayer().sendMessage("\u00A7cDer Tag-Beschleuniger l\u00e4uft bereits!");
            return;
        }
        isRunning = true;
        World w = bp.getPlayer().getWorld();
        item.removeOne(bp, true);

        new BukkitRunnable() {
            long time = w.getTime();
            @Override
            public void run() {
                w.setTime(time += 60);
                if (time == 6000) {
                    cancel();
                    isRunning = false;
                } else if (time == 24000) {
                    time = 0;
                }
            }
        }.runTaskTimer(Main.plugin, 0, 1);
    }
}
