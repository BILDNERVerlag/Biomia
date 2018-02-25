package de.biomia.api.itemcreator;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemCreator {

    public static ItemStack itemCreate(Material material, String name, short data) {

        ItemStack itemStack = new ItemStack(material, 1, data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);

        return itemStack;

    }

    private static ItemStack itemCreate(short data) {
        return new ItemStack(Material.SKULL_ITEM, 1, data);
    }

    public static ItemStack itemCreate(Material material, String name) {

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);

        return itemStack;

    }

    public static ItemStack itemCreate(Material material) {

        return new ItemStack(material);

    }

    public static ItemStack setAmount(ItemStack itemStack, int menge) {
        itemStack.setAmount(menge);
        return itemStack;
    }

    public static ItemStack setUnbreakable(ItemStack itemStack) {

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setUnbreakable(true);
        itemStack.setItemMeta(itemMeta);

        return itemStack;

    }

    public static ItemStack headWithSkin(String name) {
        ItemStack s = itemCreate((short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) s.getItemMeta();
        meta.setOwner(name);
        s.setItemMeta(meta);
        return s;
    }

    public static ItemStack headWithSkin(String name, String displayName) {
        ItemStack s = headWithSkin(name);
        SkullMeta meta = (SkullMeta) s.getItemMeta();
        meta.setDisplayName(displayName);
        s.setItemMeta(meta);
        return s;
    }

}
