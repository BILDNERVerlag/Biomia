package de.biomia.minigames.versus.bw.listeners;

import de.biomia.minigames.versus.bw.messages.ItemNames;
import de.biomia.minigames.versus.bw.var.ItemType;
import de.biomia.minigames.versus.bw.var.Variables;
import de.biomia.general.configs.BedWarsVersusConfig;
import de.biomia.minigames.versus.vs.game.bw.BedWars;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class SpawnItems {

    private BukkitTask items = null;

    public void startSpawning(BedWars bedWars) {

        World world = bedWars.getInstance().getWorld();

        ItemStack gold = ItemCreator.itemCreate(Material.GOLD_INGOT, ItemNames.gold);
        ItemStack iron = ItemCreator.itemCreate(Material.IRON_INGOT, ItemNames.iron);
        ItemStack bronze = ItemCreator.itemCreate(Material.CLAY_BRICK, ItemNames.bronze);

        HashMap<ItemType, ArrayList<Location>> spawner = BedWarsVersusConfig.getSpawner(bedWars.getInstance().getMapID(), world);

        items = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i % Variables.bronzeSpawnDelay == 0) {
                    spawner.get(ItemType.BRONZE).forEach(each -> world.dropItem(each, bronze));
                }

                if (i % Variables.ironSpawnDelay == 0) {
                    spawner.get(ItemType.IRON).forEach(each -> world.dropItem(each, iron));
                }

                if (i % Variables.goldSpawnDelay == 0) {
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