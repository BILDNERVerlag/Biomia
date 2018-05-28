package de.biomia.spigot.messages;

import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class SkyWarsItemNames {

    public static final ItemStack kitItem = ItemCreator.itemCreate(Material.CHEST, SkyWarsItemNames.kitItemName);

    public static final String playerTracker = "§5Spieler Tracker";
    public static final String purchaseKit = "§aKaufen";
    public static final String selectKit = "§aAusrüsten";
    public static final String showKit = "§aAnschauen";

    // %c = price to split the lines put: '$'
    public static final ArrayList<String> notPurchasedKitLore = new ArrayList<>(Arrays.asList("", "§cNicht Gekauft " + SpecialChars.BALLOT_X, "Preis: %c BC's"));
    public static final ArrayList<String> purchasedKitLore = new ArrayList<>(Arrays.asList("", "§aGekauft " + SpecialChars.CHECK_MARK, "Preis: %c BC's"));

    public static final String oneHitSnowball = "§2One-Hit-Schneeball";
    public static final String gummibogen = "§2Gummibogen";
    public static final String gummipfeil = "§2Gummipfeil";

    public static final String kitItemName = "§cKits";
}
