package de.biomia.spigot.server.demoserver.teleporter;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.server.demoserver.Weltenlabor;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class Bauten {

    private final String name;
    private final int seite;
    private final Location loc;
    private final Material material;
    private final ItemStack item;

    public Bauten(String name, int seite, Location loc, Material material) {
        this.name = name;
        this.seite = seite;
        this.loc = loc;
        this.material = material;
        ((Weltenlabor) Biomia.getServerInstance()).getBauten().add(this);

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
