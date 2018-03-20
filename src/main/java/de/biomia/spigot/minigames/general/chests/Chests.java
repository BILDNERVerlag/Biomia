package de.biomia.spigot.minigames.general.chests;

import de.biomia.spigot.configs.SkyWarsConfig;
import de.biomia.spigot.events.skywars.SkyWarsOpenChestEvent;
import de.biomia.spigot.minigames.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Chests {

    public static final HashMap<ItemStack, Integer> normalItems = new HashMap<>();
    public static final HashMap<ItemStack, Integer> goodItems = new HashMap<>();

    private HashMap<Location, ItemStack[]> goodFillableChests;
    private HashMap<Location, ItemStack[]> normalFillableChests;
    private final ArrayList<Location> openedChests = new ArrayList<>();

    public Chests(GameMode skyWars) {
        HashMap<SkyWarsOpenChestEvent.ChestType, ArrayList<Location>> chestsFromConfig = ((SkyWarsConfig) skyWars.getConfig()).loadChestsFromConfig(skyWars.getInstance());

        fillNormalChests(chestsFromConfig.get(SkyWarsOpenChestEvent.ChestType.NormalChest));
        fillGoodChests(chestsFromConfig.get(SkyWarsOpenChestEvent.ChestType.GoodChest));
    }

    public boolean fillChest(Location locations) {
        if (openedChests.contains(locations))
            return false;

        Chest c = getChestByLoc(locations);
        ItemStack[] inv = normalFillableChests.get(locations);
        inv = inv != null ? inv : goodFillableChests.get(locations);
        if (c != null && inv != null) {
            c.getInventory().setContents(inv);
            openedChests.add(locations);
            return true;
        }
        return false;
    }

    public boolean isNormalChest(Location loc) {
        return normalFillableChests.get(loc) != null;
    }

    private Chest getChestByLoc(Location loc) {
        Block b = loc.getBlock();
        if (b.getType().equals(Material.CHEST)) {
            return (Chest) b.getState();
        }
        return null;
    }

    private void fillNormalChests(ArrayList<Location> locations) {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (ItemStack entry : normalItems.keySet())
            for (int i = 0; i < normalItems.get(entry); i++)
                items.add(entry);
        normalFillableChests = fillChests(locations, items);
    }

    private void fillGoodChests(ArrayList<Location> locations) {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (ItemStack entry : goodItems.keySet())
            for (int i = 0; i < goodItems.get(entry); i++)
                items.add(entry);
        goodFillableChests = fillChests(locations, items);
    }

    private HashMap<Location, ItemStack[]> fillChests(ArrayList<Location> locations, ArrayList<ItemStack> items) {

        HashMap<Location, ItemStack[]> filledChests = new HashMap<>();
        locations.forEach(loc -> {

            if (loc != null) {
                int fullSlots = Items.random(7, 9);
                ArrayList<Integer> filledSlots = new ArrayList<>();

                while (filledSlots.size() < fullSlots) {
                    int i = new Random().nextInt(26);
                    if (!filledSlots.contains(i))
                        filledSlots.add(i);
                }
                ItemStack[] stacks = new ItemStack[27];
                filledSlots.forEach(eachSlot -> stacks[eachSlot] = items.get(new Random().nextInt(items.size())).clone());
                filledChests.put(loc, stacks);
            }
        });

        return filledChests;

    }

}
