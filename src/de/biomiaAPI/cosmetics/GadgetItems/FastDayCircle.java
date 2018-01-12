package de.biomiaAPI.cosmetics.GadgetItems;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.CosmeticGadgetItem;
import de.biomiaAPI.cosmetics.GadgetListener;
import de.biomiaAPI.main.Main;

class FastDayCircle implements GadgetListener {

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
