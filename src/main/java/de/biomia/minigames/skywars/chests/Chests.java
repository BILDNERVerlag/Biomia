package de.biomia.minigames.skywars.chests;

import de.biomia.minigames.skywars.var.Variables;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

public class Chests {

    public static Chest getChestByLoc(Location loc) {
        Block b = loc.getBlock();
        if (b.getType().equals(Material.CHEST)) {
            return (Chest) b.getState();
        }
        return null;
    }

    public static HashMap<Chest, ItemStack[]> fillNormalChests() {
        ArrayList<ItemStack> items = new ArrayList<>();
        HashMap<Chest, ItemStack[]> filledChests = new HashMap<>();

        for (Entry<ItemStack, Integer> entry : Variables.normalItems.entrySet()) {
            int wahrscheindlichkeit = entry.getValue();
            ItemStack value = entry.getKey();

            int temp = 0;

            while (temp < wahrscheindlichkeit) {
                items.add(value);
                temp++;
            }
        }

        Variables.normalChests.forEach((Chest chest) -> {
            if (!chest.getLocation().getChunk().isLoaded()) {
                chest.getLocation().getChunk().load();
            }

            chest.getInventory().clear();

            int fullSlots = 0;

            while (fullSlots < Variables.minimaleBelegteSlots)
                fullSlots = new Random().nextInt(Variables.maximalBelegteSlots);

            ArrayList<Integer> filledSlots = new ArrayList<>();
            while (filledSlots.size() < fullSlots) {
                int i = new Random().nextInt(26);

                if (!filledSlots.contains(i)) {
                    filledSlots.add(i);
                }

            }

            ItemStack[] stacks = new ItemStack[27];

            filledSlots.forEach(eachSlot -> stacks[eachSlot] = items.get(new Random().nextInt(items.size())).clone());
            filledChests.put(chest, stacks);
        });
        return filledChests;
    }

    public static HashMap<Chest, ItemStack[]> fillGoodChests() {
        ArrayList<ItemStack> items = new ArrayList<>();
        HashMap<Chest, ItemStack[]> filledChests = new HashMap<>();

        for (Entry<ItemStack, Integer> entry : Variables.goodItems.entrySet()) {
            int wahrscheindlichkeit = entry.getValue();
            ItemStack value = entry.getKey();

            int temp = 0;

            while (temp < wahrscheindlichkeit) {
                items.add(value);
                temp++;
            }
        }

        Variables.goodChests.forEach(chest -> {
            if (!chest.getLocation().getChunk().isLoaded()) {
                chest.getLocation().getChunk().load();
            }

            chest.getInventory().clear();

            int fullSlots = 0;

            while (fullSlots < Variables.minimaleBelegteSlots) {
                fullSlots = new Random().nextInt(Variables.maximalBelegteSlots);
            }

            ArrayList<Integer> filledSlots = new ArrayList<>();
            while (filledSlots.size() < fullSlots) {
                int i = new Random().nextInt(26);

                if (!filledSlots.contains(i)) {
                    filledSlots.add(i);
                }

            }

            ItemStack[] stacks = new ItemStack[27];

            filledSlots.forEach(eachSlot -> stacks[eachSlot] = items.get(new Random().nextInt(items.size())).clone());
            filledChests.put(chest, stacks);
        });
        return filledChests;
    }

}
