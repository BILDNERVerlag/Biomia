package de.biomia.spigot.minigames.general.shop;

import de.biomia.spigot.messages.BedWarsItemNames;
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
            default:
            case BRONZE:
                return BedWarsItemNames.bronze;
            case IRON:
                return BedWarsItemNames.iron;
            case GOLD:
                return BedWarsItemNames.gold;
        }
    }

}
