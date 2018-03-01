package de.biomia.minigames.versus.sw.var;

import de.biomia.minigames.versus.sw.messages.ItemNames;
import de.biomia.api.itemcreator.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Variables {

    // Chest System
    public static final int minimaleBelegteSlots = 7;
    public static final int maximalBelegteSlots = 9;
    public static final HashMap<ItemStack, Integer> normalItems = new HashMap<>();
    public static final HashMap<ItemStack, Integer> goodItems = new HashMap<>();
    // Items
    public static final ItemStack kitItem = ItemCreator.itemCreate(Material.CHEST, ItemNames.kitItemName);
    //TODO Rewards
    public static int winReward = 300;
    public static int playReward = 60;
    public static int killReward = 100;

}