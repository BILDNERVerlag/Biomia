package de.biomia.server.minigames.bedwars.listeners;

import de.biomia.Main;
import de.biomia.messages.BedWarsItemNames;
import de.biomia.server.minigames.bedwars.var.Variables;
import de.biomia.server.minigames.general.ItemType;
import de.biomia.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SpawnItems {

    private static BukkitTask items = null;

    public static void startSpawning() {
        items = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i % Variables.bronzeSpawnDelay == 0) {
                    Variables.spawner.get(ItemType.BRONZE).forEach(each -> Bukkit.getWorld(Variables.name).dropItem(each,
                            ItemCreator.itemCreate(Material.CLAY_BRICK, BedWarsItemNames.bronze)));
                }
                if (i % Variables.ironSpawnDelay == 0) {
                    Variables.spawner.get(ItemType.IRON).forEach(each -> Bukkit.getWorld(Variables.name).dropItem(each,
                            ItemCreator.itemCreate(Material.IRON_INGOT, BedWarsItemNames.iron)));
                }
                if (i % Variables.goldSpawnDelay == 0) {
                    Variables.spawner.get(ItemType.GOLD).forEach(each -> Bukkit.getWorld(Variables.name).dropItem(each,
                            ItemCreator.itemCreate(Material.GOLD_INGOT, BedWarsItemNames.gold)));
                }
                i++;
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }

    public static void stopSpawning() {
        items.cancel();
    }
}
