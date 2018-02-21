package de.biomiaAPI.cosmetics;

import de.biomiaAPI.achievements.Stats;
import de.biomiaAPI.achievements.statEvents.cosmetics.CosmeticUsedEvent;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.cosmetics.Cosmetic.Group;
import de.biomiaAPI.mysql.MySQL;

public abstract class CosmeticItem {

    private int id;
    private final String name;
    private final ItemStack item;
    private final Commonness commonness;
    private final Group group;

    CosmeticItem(int id, String name, ItemStack is, Commonness c, Group group) {
        this.id = id;
        this.name = name;
        this.item = is;
        this.commonness = c;
        this.group = group;

        Cosmetic.addItem(this);
    }

    public enum Commonness {
        VERY_COMMON, COMMON, RARE, VERY_RARE;

		public String deutsch() {
			switch (this) {
			case VERY_COMMON:
				return "Sehr H\u00e4ufig";
			case COMMON:
				return "H\u00e4ufig";
			case RARE:
				return "Selten";
			case VERY_RARE:
				return "Sehr Selten";
			}
			return null;
		}

    }

    public void remove(BiomiaPlayer bp) {
    }

    public void use(BiomiaPlayer bp) {
        Bukkit.getServer().getPluginManager().callEvent(new CosmeticUsedEvent(bp, this));
    }

    public void add(BiomiaPlayer bp) {
        MySQL.executeUpdate(
                "INSERT INTO `Cosmetics`(`BiomiaPlayer`, `ID`) VALUES (" + bp.getBiomiaPlayerID() + ", " + id + " )", MySQL.Databases.cosmetics_db);
    }

    public void add(BiomiaPlayer bp, int timeinseconds) {
        MySQL.executeUpdate("INSERT INTO `Cosmetics`(`BiomiaPlayer`, `ID`, `Time`) VALUES (" + bp.getBiomiaPlayerID()
                + ", " + id + ", " + timeinseconds + ")", MySQL.Databases.cosmetics_db);
    }

    public int getID() {
        return id;
    }

    public Group getGroup() {
        return group;
    }

    public ItemStack getItem() {
        return item;
    }

    public Commonness getCommonness() {
        return commonness;
    }

    public String getName() {
        return name;
    }

    public void setNewID(int id) {
        this.id = id;
    }
}
