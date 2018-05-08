package de.biomia.spigot.messages;

import de.biomia.universal.Messages;

public class BedWarsMessages {

    public static final String bedwars = String.format("%sBED%sWARS", Messages.COLOR_MAIN, Messages.COLOR_SUB);
    public static final String blocksMustBeBeds = String.format("%sDu musst auf dem Fuß des Bettes stehen und auf das Kopfkissen schauen!", Messages.COLOR_MAIN);
    public static final String invailedSide = String.format("%sDiese Seite ist nicht verfügbar!", Messages.COLOR_MAIN);
    public static final String shopInventory = String.format("%sShop", Messages.COLOR_MAIN);
    public static final String notEnoughItemsToPay = String.format("%sDu hast nicht genug $n", Messages.COLOR_MAIN);
    public static final String cantDestroyThisBlock = String.format("%sDu darfst diesen Block nicht zerstören!", Messages.COLOR_MAIN);
    public static final String cantPlaceBlock = String.format("%sDu darfst hier keinen Block setzen!", Messages.COLOR_MAIN);
    public static final String bedWasDestroyed = "%s>>%sDas Bett von %sTeam %s%s wurde zerstört.";
    public static final String notInATeam = String.format("%sDu bist in keinem Team!", Messages.COLOR_MAIN);
    public static final String shopVillagerName ="Shop";
    public static final String thirtySecondShopName = "%s%s Sekunden " + shopVillagerName;

}
