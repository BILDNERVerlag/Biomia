package de.biomia.server.demoserver.teleporter;

import de.biomia.server.demoserver.Weltenlabor;
import de.biomia.tools.ItemCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class Bauten {

    private String name;
    private int seite;
    private Location loc;
    private Material material;
    private ItemStack item;

    public Bauten(String name, int seite, Location loc, Material material) {
        this.name = name;
        this.seite = seite;
        this.loc = loc;
        this.material = material;
        Weltenlabor.bauten.add(this);

        ItemStack stack = ItemCreator.itemCreate(getMaterial(), getName());
        ItemMeta meta = stack.getItemMeta();
        meta.setLore(Collections.singletonList("S. " + getSeite()));
        stack.setItemMeta(meta);

        this.item = stack;
    }

    public String getName() {
        return name;
    }

    private int getSeite() {
        return seite;
    }

    public Location getLoc() {
        return loc;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemStack getItem() {
        return item;
    }
}
