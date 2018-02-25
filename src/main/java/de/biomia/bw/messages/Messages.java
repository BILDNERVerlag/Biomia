package de.biomia.bw.messages;

import de.biomia.bw.var.Variables;

public class Messages {

    public static final String bedwars = "§5Bed§2Wars";
    // %t = CountDown
    public static final String restartCountDown = "§cRestart in §6%t §cSekunden!";
    public static final String lobbyCountDown = Variables.prefix + "§5Start in §2%t §5Sekunden!";
    // %mt = Max Teams, %ts = Team Size
    public static final String mapSize = "§5%mt §7x §2%ts";
    public static final String notEnoughPlayerToStart = Variables.prefix + "§cZu wenig Spieler! Warte auf weitere...";
    public static final String fillSecondLine = "§cBitte gib eine Zahl in Zeile 2 ein!";
    public static final String teamFull = "Das Team ist voll!";
    public static final String alreadyInTeam = "Du bist bereits in diesem Team!";
    public static final String chatMessageAll = "§8[§7@all§8] %p§7: §f%msg";
    public static final String chatMessageTeam = "§8[§7Team§8] %p§7: §f%msg";
    public static final String chatMessageDead = "§8[§7Tot§8] §7§o%p§r§7: §f%msg";
    public static final String chatMessageLobby = "%p§7: §f%msg";
    public static final String teamInventoryName = "§5Team §2wählen";
    // %t = TeamName
    public static final String teamJoinerSet = "§aTeam Joiner für %t erfolgreich gesetzt!";
    public static final String joinedTeam = "%p ist dem Team beigetreten!";
    public static final String noFittingTeamParty = "§cEs konnt kein passendes Team gefunden werden, da die Partygröße die maximale Teamgröße übersteigt!";
    public static final String noFittingTeamPlayer = "§cEs konnt kein passendes Team gefunden werden, da kein Team genug Platz hat!";
    public static final String kickedForPremium = "§cDu wurdest gekickt um einen Premium Spieler Platz zu machen!";
    public static final String joinedTheGame = " §8ist dem Spiel beigetreten!";
    public static final String playerKilledByPlayer = "§8Der Spieler %p1 §8wurde von %p2 §8getötet!";
    public static final String playerDied = "§8Der Spieler %p §8ist gestorben!";
    public static final String playerDiedFinally = "§8Der Spieler %p §8ist endgültig gestorben!";
    public static final String blocksMustBeBeds = "§cDu musst auf dem Fuß eines Bettes stehen und auf den Kopf schauen!";
    public static final String invailedSide = "§cDiese Seite ist nicht verfügbar!";
    public static final String shopInventory = "§cShop";
    public static final String notEnoughItemsToPay = "§cDu hast nicht genug %n";
    public static final String cantDestroyThisBlock = "§cDu darfst diesen Block nicht zerstören!";
    public static final String bronzeSpawnAdded = "§aBronze Spawner hinzugefügt!";
    public static final String ironSpawnAdded = "§aEisen Spawner hinzugefügt!";
    public static final String goldSpawnAdded = "§aGold Spawner hinzugefügt!";
    public static final String cantDestroyYourOwnBed = "§cDu kannst dein eigenes Bett nicht zerstören!";

    public static String addPrefix(String nachricht) {
        return Variables.prefix + nachricht;
    }

}
