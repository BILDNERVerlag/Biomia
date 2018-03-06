package de.biomia.spigot.minigames.skywars.chests;

import de.biomia.spigot.messages.SkyWarsItemNames;
import de.biomia.spigot.minigames.skywars.var.Variables;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Random;

public class Items {

    public static void init() {

        HashMap<ItemStack, Integer> n = Variables.normalItems;

        ItemStack stack = ItemCreator.itemCreate(Material.DIAMOND_CHESTPLATE);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        n.put(stack, 1);

        n.put(ItemCreator.itemCreate(Material.ENDER_PEARL), 1);

        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.DIAMOND), random(3, 6)), 1);

        stack = ItemCreator.itemCreate(Material.DIAMOND_LEGGINGS);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        n.put(stack, 1);

        stack = ItemCreator.itemCreate(Material.DIAMOND_BOOTS);
        stack.addEnchantment(Enchantment.PROTECTION_FALL, 1);
        n.put(stack, 1);

        stack = ItemCreator.itemCreate(Material.DIAMOND_HELMET);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        n.put(stack, 2);

        stack = ItemCreator.itemCreate(Material.GOLD_SWORD);
        stack.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        n.put(stack, 2);

        n.put(ItemCreator.itemCreate(Material.GOLDEN_APPLE), 2);

        stack = ItemCreator.itemCreate(Material.IRON_CHESTPLATE);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        n.put(stack, 3);

        stack = ItemCreator.itemCreate(Material.IRON_PICKAXE);
        stack.addEnchantment(Enchantment.DURABILITY, 2);
        n.put(stack, 3);

        n.put(ItemCreator.itemCreate(Material.CHAINMAIL_HELMET), 4);
        n.put(ItemCreator.itemCreate(Material.DIAMOND_BOOTS), 2);
        n.put(ItemCreator.itemCreate(Material.LAVA_BUCKET), 4);

        stack = ItemCreator.itemCreate(Material.IRON_LEGGINGS);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        n.put(stack, 4);

        stack = ItemCreator.itemCreate(Material.IRON_HELMET);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        n.put(stack, 5);

        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.IRON_INGOT), random(3, 6)), 5);
        n.put(ItemCreator.itemCreate(Material.IRON_AXE), 5);
        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.WHEAT), random(3, 6)), 5);
        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.FLINT), random(1, 2)), 5);

        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.LEATHER), random(1, 4)), 6);
        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.WATER_BUCKET), 1), 6);
        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.CAKE), 1), 6);

        stack = ItemCreator.itemCreate(Material.LEATHER_BOOTS);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        n.put(stack, 5);

        stack = ItemCreator.itemCreate(Material.CHAINMAIL_LEGGINGS);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        n.put(stack, 6);

        stack = ItemCreator.itemCreate(Material.GOLD_LEGGINGS);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        n.put(stack, 6);

        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.EXP_BOTTLE), random(5, 8)), 3);
        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.EXP_BOTTLE), random(9, 12)), 3);

        stack = ItemCreator.itemCreate(Material.CHAINMAIL_BOOTS);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        n.put(stack, 7);

        stack = ItemCreator.itemCreate(Material.LEATHER_HELMET);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        n.put(stack, 7);

        stack = ItemCreator.itemCreate(Material.LEATHER_CHESTPLATE);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        n.put(stack, 7);

        stack = ItemCreator.itemCreate(Material.GOLD_BOOTS);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        n.put(stack, 6);

        stack = ItemCreator.itemCreate(Material.GOLD_CHESTPLATE);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        n.put(stack, 5);

        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.COMPASS, SkyWarsItemNames.playerTracker), 1), 5);

        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.TNT), random(3, 8)), 3);

        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.WEB), random(3, 8)), 3);

        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.BREAD), 6), 6);
        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.STICK), random(4, 6)), 7);
        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.COOKED_CHICKEN), 4), 7);
        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.COOKED_BEEF), 8), 7);
        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.BAKED_POTATO), random(5, 7)), 7);

        stack = ItemCreator.itemCreate(Material.WOOD_SWORD);
        stack.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        n.put(stack, 8);

        n.put(ItemCreator.itemCreate(Material.GOLD_HELMET), 6);
        n.put(ItemCreator.itemCreate(Material.LEATHER_BOOTS), 7);
        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.GLASS), 32), 10);
        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.BRICK), 32), 10);
        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.WOOD), 32), 4);
        n.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.STONE), 32), 10);

        HashMap<ItemStack, Integer> g = Variables.goodItems;

        g.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.TNT), random(6, 8)), 4);
        g.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.WOOD), 32), 4);
        g.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.WEB), random(7, 12)), 3);
        g.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.STONE), 16), 4);

        g.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.EGG), random(6, 15)), 2);

        g.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.EXP_BOTTLE), random(6, 15)), 2);

        stack = ItemCreator.itemCreate(Material.DIAMOND_HELMET);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        g.put(stack, 2);

        g.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.SNOW_BALL), random(6, 15)), 2);

        g.put(ItemCreator.itemCreate(Material.GOLDEN_APPLE), 2);
        g.put(ItemCreator.itemCreate(Material.LAVA_BUCKET), 2);
        g.put(ItemCreator.itemCreate(Material.WATER_BUCKET), 2);

        stack = ItemCreator.itemCreate(Material.DIAMOND_BOOTS);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        g.put(stack, 1);
        stack = ItemCreator.itemCreate(Material.DIAMOND_BOOTS);
        stack.addEnchantment(Enchantment.PROTECTION_FALL, 1);
        g.put(stack, 1);

        stack = ItemCreator.itemCreate(Material.DIAMOND_CHESTPLATE);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        stack.addEnchantment(Enchantment.DURABILITY, 1);
        g.put(stack, 1);

        stack = ItemCreator.itemCreate(Material.DIAMOND_LEGGINGS);
        stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        stack.addEnchantment(Enchantment.DURABILITY, 1);
        g.put(stack, 1);

        g.put(ItemCreator.setAmount(ItemCreator.itemCreate(Material.ARROW), random(2, 3)), 1);
    }

    private static int random(int arg1, int arg2) {

        int randomZahl = 0;

        Random random = new Random();

        while (randomZahl < arg1) {
            randomZahl = random.nextInt(arg2);
        }

        return randomZahl;

    }

}
