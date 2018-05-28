package de.biomia.spigot.general.cosmetics.items;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.universal.MySQL;
import de.biomia.spigot.events.cosmetics.CosmeticUsedEvent;
import de.biomia.spigot.general.cosmetics.Cosmetic;
import de.biomia.spigot.general.cosmetics.Cosmetic.Group;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public abstract class CosmeticItem {

    private final String name;
    private final ItemStack item;
    private final Commonness commonness;
    private final Group group;
    private int id;

    CosmeticItem(int id, String name, ItemStack is, Commonness c, Group group) {
        this.id = id;
        this.name = name;
        this.item = is;
        this.commonness = c;
        this.group = group;

        Cosmetic.addItem(this);
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

    public enum Commonness {
        VERY_COMMON, COMMON, RARE, VERY_RARE;

        public String deutsch() {
            switch (this) {
                case VERY_COMMON:
                    return "Sehr H00e4ufig";
                case COMMON:
                    return "H00e4ufig";
                case RARE:
                    return "Selten";
                case VERY_RARE:
                    return "Sehr Selten";
            }
            return null;
        }

    }
}
