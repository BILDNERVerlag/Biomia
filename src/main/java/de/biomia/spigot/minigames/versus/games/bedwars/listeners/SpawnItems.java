package de.biomia.spigot.minigames.versus.games.bedwars.listeners;

import de.biomia.spigot.Main;
import de.biomia.spigot.configs.BedWarsVersusConfig;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.minigames.general.ItemType;
import de.biomia.spigot.minigames.versus.games.bedwars.BedWars;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class SpawnItems {

    private final int bronzeSpawnDelay = 1;
    private final int ironSpawnDelay = 10;
    private final int goldSpawnDelay = 30;

    private BukkitTask items = null;

    public void startSpawning(BedWars bedWars) {

        World world = bedWars.getInstance().getWorld();

        ItemStack gold = ItemCreator.itemCreate(Material.GOLD_INGOT, BedWarsItemNames.gold);
        ItemStack iron = ItemCreator.itemCreate(Material.IRON_INGOT, BedWarsItemNames.iron);
        ItemStack bronze = ItemCreator.itemCreate(Material.CLAY_BRICK, BedWarsItemNames.bronze);

        HashMap<ItemType, ArrayList<Location>> spawner = BedWarsVersusConfig.getSpawner(bedWars.getInstance().getMapID(), world);

        items = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i % bronzeSpawnDelay == 0) {
                    spawner.get(ItemType.BRONZE).forEach(each -> world.dropItem(each, bronze));
                }

                if (i % ironSpawnDelay == 0) {
                    spawner.get(ItemType.IRON).forEach(each -> world.dropItem(each, iron));
                }

                if (i % goldSpawnDelay == 0) {
                    spawner.get(ItemType.GOLD).forEach(each -> world.dropItem(each, gold));
                }
                i++;
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }

    public void stopSpawning() {
        items.cancel();
    }
}
