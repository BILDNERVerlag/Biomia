package de.biomia.sw.messages;

import de.biomia.sw.var.Variables;

public class Messages {

    public static final String skywars = "§5Sky§2Wars";
    // %t = CountDown
    public static final String restartCountDown = "§cRestart in §6%t §cSekunden!";
    public static final String lobbyCountDown = Variables.prefix + "§5Start in §2%t §5Sekunden!";
    // %mt = Max Teams, %ts = Team Size
    public static final String mapSize = "§5%mt §7x §2%ts";
    // %k = Kit Name
    public static final String demoInventory = "§cDemo Inventory: %kit";
    public static final String kitPurchased = "§aGlückwunsch, du hast das Kit %kit erworben!";
    public static final String errorWhilePurchasing = "§cEntschuldige, es ist ein Fehler aufgetreten! Dir wurde KEIN Geld abgezogen!";
    public static final String notEnoughCoins = "§cDu hast nicht genug BC's um das Kit %k zu Kaufen!";
    public static final String alreadyPurchased = "§cDu hast das Kit %k bereits gekauft!";
    public static final String setupInventory = "§c%k";
    public static final String missingCoins = "§cDir fehlen noch %c BC's!";
    public static final String notEnoughPlayerToStart = Variables.prefix + "§cZu wenig Spieler! Warte auf weitere...";
    public static final String compassMessages = "§5Der Spieler §2%p §5ist §2%d §5weit entfernt!";
    public static final String killedBy = "§8Der Spieler %t §8wurde von %k §8getötet!";
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
    public static final String youChoseKit = "§5Du hast das Kit §2%k §5ausgewählt!";
    public static final String kitAlreadyChosen = "§cDu hast das Kit bereits ausgewählt!";
    public static final String kitNotBought = "§5Du hast das Kit §2nicht §5gekauft!";
    public static final String kickedForPremium = "§cDu wurdest gekickt um einen Premium Spieler Platz zu machen!";
    public static final String joinedTheGame = " §8ist dem Spiel beigetreten!";
    public static final String nowLookingAtKit = "§5Du schaust nun das Kit §2%k §5an!";
    public static final String playerKilledByPlayer = "§8Der Spieler %p1 §8wurde von %p2 §8getötet!";
    public static final String playerDied = "§8Der Spieler %p §8ist gestorben!";
    public static final String chestAddModeON = "§cDu bist jetzt im §4KISTENHINZUFÜGEMODUS!!!";
    public static final String chestAddModeOFF = "§cDu bist jetzt nicht mehr im §4KISTENHINZUFÜGEMODUS!!!";
    public static String kills = "§8Kills:§a ";
    public static String deaths = "§8Tode:§a ";
    public static String playedGames = "§8Gespielt:§a ";
    public static String wunGames = "§8Gewonnen:§a ";
    public static String kd = "§8K/D:§a ";
    public static String rank = "§5%p §2#%rank";

    public static String addPrefix(String nachricht) {
        return Variables.prefix + nachricht;
    }
}
