package de.biomia.spigot.minigames.general.chests;

import de.biomia.spigot.configs.SkyWarsConfig;
import de.biomia.spigot.events.game.skywars.SkyWarsOpenChestEvent;
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

    private HashMap<Block, ItemStack[]> goodFillableChests;
    private HashMap<Block, ItemStack[]> normalFillableChests;
    private final ArrayList<Location> openedChests = new ArrayList<>();

    public Chests(GameMode skyWars) {
        HashMap<SkyWarsOpenChestEvent.ChestType, ArrayList<Block>> chestsFromConfig = ((SkyWarsConfig) skyWars.getConfig()).loadChestsFromConfig(skyWars.getInstance());

        fillNormalChests(chestsFromConfig.get(SkyWarsOpenChestEvent.ChestType.NormalChest));
        fillGoodChests(chestsFromConfig.get(SkyWarsOpenChestEvent.ChestType.GoodChest));
    }

    public boolean fillChest(Location locations) {
        if (openedChests.contains(locations)) {
            return false;
        }

        Chest c = getChestByLoc(locations);
        ItemStack[] inv = normalFillableChests.get(locations.getBlock());
        inv = inv != null ? inv : goodFillableChests.get(locations.getBlock());
        if (c != null && inv != null) {
            c.getInventory().setContents(inv);
            openedChests.add(locations);
            return true;
        }
        return false;
    }

    public boolean isNormalChest(Location loc) {
        return normalFillableChests.get(loc.getBlock()) != null;
    }

    private Chest getChestByLoc(Location loc) {
        Block b = loc.getBlock();
        if (b.getType().equals(Material.CHEST)) {
            return (Chest) b.getState();
        }
        return null;
    }

    private void fillNormalChests(ArrayList<Block> locations) {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (ItemStack entry : normalItems.keySet())
            for (int i = 0; i < normalItems.get(entry); i++)
                items.add(entry);
        normalFillableChests = fillChests(locations, items);
    }

    private void fillGoodChests(ArrayList<Block> locations) {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (ItemStack entry : goodItems.keySet())
            for (int i = 0; i < goodItems.get(entry); i++)
                items.add(entry);
        goodFillableChests = fillChests(locations, items);
    }

    private HashMap<Block, ItemStack[]> fillChests(ArrayList<Block> blocks, ArrayList<ItemStack> items) {

        HashMap<Block, ItemStack[]> filledChests = new HashMap<>();
        blocks.forEach(b -> {
            if (b != null) {
                int slotsToFill = Items.random(7, 9);
                int filledSlots = 0;
                ItemStack[] stacks = new ItemStack[27];
                while (filledSlots < slotsToFill) {
                    int i = new Random().nextInt(27);
                    if (stacks[i] == null) {
                        if (items.size() > 0) {
                            int randomIndex = new Random().nextInt(items.size());
                            stacks[i] = items.get(randomIndex).clone();
                            filledSlots++;
                        }
                    }
                }
                filledChests.put(b, stacks);
            }
        });

        return filledChests;

    }

}
