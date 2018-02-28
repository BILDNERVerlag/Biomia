package de.biomia.versus.sw.messages;

import de.biomia.api.messages.SpecialCharacters;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemNames {

    public static final String playerTracker = "\u00A75Spieler Tracker";
    public static final String purchaseKit = "\u00A7aKaufen";
    public static final String selectKit = "\u00A7aAusr\u00fcsten";
    public static final String showKit = "\u00A7aAnschauen";

    public static final ArrayList<String> notPurchasedKitLore = new ArrayList<>(Arrays.asList("", "\u00A7cNicht Gekauft " + SpecialCharacters.BALLOT_X, "Preis: %c BC's"));
    public static final ArrayList<String> purchasedKitLore = new ArrayList<>(Arrays.asList("", "\u00A7aGekauft " + SpecialCharacters.CHECK_MARK, "Preis: %c BC's"));

    public static final String oneHitSnowball = "\u00A72One-Hit-Schneeball";
    public static final String gummibogen = "\u00A72Gummibogen";
    public static final String gummipfeil = "\u00A72Gummipfeil";

    public static final String normaleTruheHinzu = "\u00A7cNormale Truhe hinzugef\u00fcgt!";
    public static final String bessereTruheHinzu = "\u00A7cBessere Truhe hinzugef\u00fcgt!";
    public static final String truheBereitsHinzu = "\u00A7cDiese Truhe existiert bereits!";

    public static final String kitItemName = "\u00A7dKits";
    public static final String startItem = "\u00A7bStart";
    public static final String teamJoinerSetter = "\u00A7cTeamJoinerSetter";
    public static final String teamWaehlerItem = "\u00A7cTeam W\u00fchler";
}
