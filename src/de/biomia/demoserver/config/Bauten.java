package de.biomia.demoserver.config;

import de.biomia.demoserver.main.WeltenlaborMain;
import de.biomiaAPI.itemcreator.ItemCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
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

		WeltenlaborMain.bauten.add(this);

		ItemStack stack = ItemCreator.itemCreate(getMaterial(), getName());
		ItemMeta meta = stack.getItemMeta();
		meta.setLore(Collections.singletonList("S. " + getSeite()));
		stack.setItemMeta(meta);
		WeltenlaborMain.si.addItem(stack);
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public int getSeite() {
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
