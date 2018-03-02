package de.biomia.server.minigames.versus.sw.chests;

import de.biomia.data.configs.SkyWarsVersusConfig;
import de.biomia.server.minigames.versus.sw.var.Variables;
import de.biomia.server.minigames.versus.vs.game.sw.SkyWars;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Chests {

    private HashMap<Location, ItemStack[]> goodFillableChests;
    private HashMap<Location, ItemStack[]> normalFillableChests;

    public Chests(SkyWars skyWars) {
        fillNormalChests(SkyWarsVersusConfig.loadNormalChestsFromConfig(skyWars.getInstance().getMapID(), skyWars.getInstance().getWorld()));
        fillGoodChests(SkyWarsVersusConfig.loadGoodChestsFromConfig(skyWars.getInstance().getMapID(), skyWars.getInstance().getWorld()));
    }

    public Chest fillChest(Location locations) {
        Chest c = getChestByLoc(locations);
        ItemStack[] inv = normalFillableChests.get(locations);
        inv = inv != null ? inv : goodFillableChests.get(locations);
        if (c != null && inv != null) {
            c.getInventory().setContents(inv);
            return c;
        }
        return null;
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
        for (ItemStack entry : Variables.normalItems.keySet())
            for (int i = 0; i < Variables.normalItems.get(entry); i++)
                items.add(entry);
        normalFillableChests = fillChests(locations, items);
    }

    private void fillGoodChests(ArrayList<Location> locations) {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (ItemStack entry : Variables.goodItems.keySet())
            for (int i = 0; i < Variables.goodItems.get(entry); i++)
                items.add(entry);
        goodFillableChests = fillChests(locations, items);
    }

    private HashMap<Location, ItemStack[]> fillChests(ArrayList<Location> locations, ArrayList<ItemStack> items) {

        HashMap<Location, ItemStack[]> filledChests = new HashMap<>();
        locations.forEach(loc -> {

            if (loc != null) {
                int fullSlots = Items.random(Variables.minimaleBelegteSlots, Variables.maximalBelegteSlots);
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
