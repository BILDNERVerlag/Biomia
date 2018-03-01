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

    public Bauten(String name, int seite, Location loc, Material material) {
        setName(name);
        setSeite(seite);
        setLoc(loc);
        setMaterial(material);

        Weltenlabor.bauten.add(this);

        ItemStack stack = ItemCreator.itemCreate(getMaterial(), getName());
        ItemMeta meta = stack.getItemMeta();
        meta.setLore(Collections.singletonList("S. " + getSeite()));
        stack.setItemMeta(meta);
        Weltenlabor.si.addItem(stack);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    private int getSeite() {
        return seite;
    }

    private void setSeite(int seite) {
        this.seite = seite;
    }

    public Location getLoc() {
        return loc;
    }

    private void setLoc(Location loc) {
        this.loc = loc;
    }

    public Material getMaterial() {
        return material;
    }

    private void setMaterial(Material material) {
        this.material = material;
    }

}
