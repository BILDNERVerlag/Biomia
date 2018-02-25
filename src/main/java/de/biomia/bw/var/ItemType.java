package de.biomia.bw.var;

import org.bukkit.Material;

public enum ItemType {

    BRONZE, IRON, GOLD;

    public static Material toMaterial(ItemType type) {

        switch (type) {
            case BRONZE:
                return Material.CLAY_BRICK;
            case IRON:
                return Material.IRON_INGOT;
            case GOLD:
                return Material.GOLD_INGOT;
            default:
                return null;
        }
    }

    public static String getName(ItemType type) {
        switch (type) {
            case BRONZE:
                return "§cBronze";
            case IRON:
                return "§7Eisen";
            case GOLD:
                return "§6Gold";
            default:
                return null;
        }
    }

}
