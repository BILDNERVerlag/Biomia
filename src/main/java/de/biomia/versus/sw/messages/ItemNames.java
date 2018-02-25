package de.biomia.versus.sw.messages;

import de.biomia.api.msg.SpecialCharacters;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemNames {

    public static final String playerTracker = "§5Spieler Tracker";
    public static final String purchaseKit = "§aKaufen";
    public static final String selectKit = "§aAusrüsten";
    public static final String showKit = "§aAnschauen";

    public static final ArrayList<String> notPurchasedKitLore = new ArrayList<>(Arrays.asList("", "§cNicht Gekauft " + SpecialCharacters.BALLOT_X, "Preis: %c BC's"));
    public static final ArrayList<String> purchasedKitLore = new ArrayList<>(Arrays.asList("", "§aGekauft " + SpecialCharacters.CHECK_MARK, "Preis: %c BC's"));

    public static final String oneHitSnowball = "§2One-Hit-Schneeball";
    public static final String gummibogen = "§2Gummibogen";
    public static final String gummipfeil = "§2Gummipfeil";

    public static final String normaleTruheHinzu = "§cNormale Truhe hinzugefügt!";
    public static final String bessereTruheHinzu = "§cBessere Truhe hinzugefügt!";
    public static final String truheBereitsHinzu = "§cDiese Truhe existiert bereits!";

    public static final String kitItemName = "§dKits";
    public static final String startItem = "§bStart";
    public static final String teamJoinerSetter = "§cTeamJoinerSetter";
    public static final String teamWaehlerItem = "§cTeam Wähler";
}
