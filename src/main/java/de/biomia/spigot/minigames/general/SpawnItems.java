package de.biomia.spigot.minigames.general;

import de.biomia.spigot.Main;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.minigames.general.shop.ItemType;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Bukkit;
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

    private final ItemStack gold = ItemCreator.itemCreate(Material.GOLD_INGOT, BedWarsItemNames.gold);
    private final ItemStack iron = ItemCreator.itemCreate(Material.IRON_INGOT, BedWarsItemNames.iron);
    private final ItemStack bronze = ItemCreator.itemCreate(Material.CLAY_BRICK, BedWarsItemNames.bronze);

    private BukkitTask items = null;

    private final World world;

    private final HashMap<ItemType, ArrayList<Location>> locations;

    public SpawnItems(HashMap<ItemType, ArrayList<Location>> locations, World world) {
        this.locations = locations;
        this.world = world;
    }

    public void startSpawning() {

        items = new BukkitRunnable() {

            int i = 0;

            @Override
            public void run() {
                if (i % bronzeSpawnDelay == 0)
                    locations.get(ItemType.BRONZE).forEach(each -> world.dropItem(each, bronze));
                if (i % ironSpawnDelay == 0)
                    locations.get(ItemType.IRON).forEach(each -> world.dropItem(each, iron));
                if (i % goldSpawnDelay == 0)
                    locations.get(ItemType.GOLD).forEach(each -> world.dropItem(each, gold));
                i++;
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }

    public void stopSpawning() {
        items.cancel();
    }
}
