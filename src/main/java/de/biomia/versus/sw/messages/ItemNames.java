package de.biomia.versus.sw.messages;

import de.biomia.api.msg.SpecialCharacters;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemNames {

    public static final String playerTracker = "00A75Spieler Tracker";
    public static final String purchaseKit = "00A7aKaufen";
    public static final String selectKit = "00A7aAusr\u00fcsten";
    public static final String showKit = "00A7aAnschauen";

    public static final ArrayList<String> notPurchasedKitLore = new ArrayList<>(Arrays.asList("", "00A7cNicht Gekauft " + SpecialCharacters.BALLOT_X, "Preis: %c BC's"));
    public static final ArrayList<String> purchasedKitLore = new ArrayList<>(Arrays.asList("", "00A7aGekauft " + SpecialCharacters.CHECK_MARK, "Preis: %c BC's"));

    public static final String oneHitSnowball = "00A72One-Hit-Schneeball";
    public static final String gummibogen = "00A72Gummibogen";
    public static final String gummipfeil = "00A72Gummipfeil";

    public static final String normaleTruheHinzu = "00A7cNormale Truhe hinzugef\u00fcgt!";
    public static final String bessereTruheHinzu = "00A7cBessere Truhe hinzugef\u00fcgt!";
    public static final String truheBereitsHinzu = "00A7cDiese Truhe existiert bereits!";

    public static final String kitItemName = "00A7dKits";
    public static final String startItem = "00A7bStart";
    public static final String teamJoinerSetter = "00A7cTeamJoinerSetter";
    public static final String teamWaehlerItem = "00A7cTeam W\u00fchler";
}
