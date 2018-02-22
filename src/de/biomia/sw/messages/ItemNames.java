package de.biomia.sw.messages;

import de.biomiaAPI.msg.SpecialCharacters;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemNames {

    public static final String playerTracker = "§5Spieler Tracker";
    public static final String purchaseKit = "§aKaufen";
    public static final String selectKit = "§aAusrüsten";
    public static final String showKit = "§aAnschauen";
    public static final String purchaseKitWithoutColors = purchaseKit.substring(2, purchaseKit.length());
    public static final String selectKitWithoutColors = selectKit.substring(2, selectKit.length());
    public static final String showKitWithoutColors = showKit.substring(2, showKit.length());

    // %c = price to split the lines put: '$'
    public static final ArrayList<String> notPurchasedKitLore = new ArrayList<>(Arrays.asList("", "§cNicht Gekauft " + SpecialCharacters.BALLOT_X, "Preis: %c BC's"));
    public static final ArrayList<String> purchasedKitLore = new ArrayList<>(Arrays.asList("", "§aGekauft " + SpecialCharacters.CHECK_MARK, "Preis: %c BC's"));

    public static final String oneHitSnowball = "§2One-Hit-Schneeball";
    public static final String gummibogen = "§2Gummibogen";
    public static final String gummipfeil = "§2Gummipfeil";

    public static final String normaleTruheHinzu = "§cNormale Truhe hinzugefügt!";
    public static final String bessereTruheHinzu = "§cBessere Truhe hinzugefügt!";
    public static final String truheBereitsHinzu = "§cDiese Truhe existiert bereits!";

    public static final String kitItemName = "§cKits";
    public static final String startItem = "§bStart";
    public static final String teamJoinerSetter = "§cTeamJoinerSetter";
    public static final String teamWaehlerItem = "§cTeam Wähler";
}
