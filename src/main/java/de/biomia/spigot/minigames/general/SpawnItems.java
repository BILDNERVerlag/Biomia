package de.biomia.spigot.minigames.general;

import de.biomia.spigot.Main;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.minigames.bedwars.var.Variables;
import de.biomia.spigot.minigames.general.shop.ItemType;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SpawnItems {

    private final int bronzeSpawnDelay = 1;
    private final int ironSpawnDelay = 10;
    private final int goldSpawnDelay = 30;

    private final ItemStack gold = ItemCreator.itemCreate(Material.GOLD_INGOT, BedWarsItemNames.gold);
    private final ItemStack iron = ItemCreator.itemCreate(Material.IRON_INGOT, BedWarsItemNames.iron);
    private final ItemStack bronze = ItemCreator.itemCreate(Material.CLAY_BRICK, BedWarsItemNames.bronze);

    private BukkitTask items = null;

    private World world;

    public SpawnItems(World world) {
        this.world = world;
    }

    public void startSpawning() {
        items = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i % bronzeSpawnDelay == 0)
                    Variables.spawner.get(ItemType.BRONZE).forEach(each -> world.dropItem(each, bronze));
                if (i % ironSpawnDelay == 0)
                    Variables.spawner.get(ItemType.IRON).forEach(each -> world.dropItem(each, iron));
                if (i % goldSpawnDelay == 0)
                    Variables.spawner.get(ItemType.GOLD).forEach(each -> world.dropItem(each, gold));
                i++;
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }

    public void stopSpawning() {
        items.cancel();
    }
}
