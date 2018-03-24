package de.biomia.spigot.messages;

import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class SkyWarsItemNames {

    public static final ItemStack kitItem = ItemCreator.itemCreate(Material.CHEST, SkyWarsItemNames.kitItemName);

    public static final String playerTracker = "\u00A75Spieler Tracker";
    public static final String purchaseKit = "\u00A7aKaufen";
    public static final String selectKit = "\u00A7aAusr\u00fcsten";
    public static final String showKit = "\u00A7aAnschauen";

    // %c = price to split the lines put: '$'
    public static final ArrayList<String> notPurchasedKitLore = new ArrayList<>(Arrays.asList("", "\u00A7cNicht Gekauft " + SpecialChars.BALLOT_X, "Preis: %c BC's"));
    public static final ArrayList<String> purchasedKitLore = new ArrayList<>(Arrays.asList("", "\u00A7aGekauft " + SpecialChars.CHECK_MARK, "Preis: %c BC's"));

    public static final String oneHitSnowball = "\u00A72One-Hit-Schneeball";
    public static final String gummibogen = "\u00A72Gummibogen";
    public static final String gummipfeil = "\u00A72Gummipfeil";

    public static final String kitItemName = "\u00A7cKits";
}
