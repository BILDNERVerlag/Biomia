package de.biomia.spigot.server.minigames.general;

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
                return "\u00A7cBronze";
            case IRON:
                return "\u00A77Eisen";
            case GOLD:
                return "\u00A76Gold";
            default:
                return null;
        }
    }

}
