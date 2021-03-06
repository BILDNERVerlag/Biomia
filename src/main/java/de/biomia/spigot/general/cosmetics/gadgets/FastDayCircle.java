package de.biomia.spigot.general.cosmetics.gadgets;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.general.cosmetics.items.CosmeticGadgetItem;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

class FastDayCircle implements GadgetListener {

    private boolean isRunning = false;

    @Override
    public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {

        if (isRunning) {
            bp.getPlayer().sendMessage("§cDer Tag-Beschleuniger läuft bereits!");
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
        }.runTaskTimer(Main.getPlugin(), 0, 1);
    }
}
