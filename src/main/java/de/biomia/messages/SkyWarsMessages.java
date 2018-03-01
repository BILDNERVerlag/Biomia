package de.biomia.messages;

import de.biomia.server.minigames.skywars.var.Variables;

public class SkyWarsMessages {

    public static final String skywars = "\u00A75Sky\u00A72Wars";
    // %t = CountDown
    public static final String restartCountDown = "\u00A7cRestart in \u00A76%t \u00A7cSekunden!";
    public static final String lobbyCountDown = Variables.prefix + "\u00A75Start in \u00A72%t \u00A75Sekunden!";
    // %mt = Max Teams, %ts = Team Size
    public static final String mapSize = "\u00A75%mt \u00A77x \u00A72%ts";
    // %k = Kit Name
    public static final String demoInventory = "\u00A7cDemo Inventory: %kit";
    public static final String kitPurchased = "\u00A7aGl\u00fcckwunsch, du hast das Kit %kit erworben!";
    public static final String errorWhilePurchasing = "\u00A7cEntschuldigung, es ist ein Fehler aufgetreten! Dir wurde KEIN Geld abgezogen!";
    public static final String notEnoughCoins = "\u00A7cDu hast nicht genug BC's um das Kit %k zu Kaufen!";
    public static final String alreadyPurchased = "\u00A7cDu hast das Kit %k bereits gekauft!";
    public static final String setupInventory = "\u00A7c%k";
    public static final String missingCoins = "\u00A7cDir fehlen noch %c BC's!";
    public static final String notEnoughPlayerToStart = Variables.prefix + "\u00A7cZu wenig Spieler! Warte auf weitere...";
    public static final String compassMessages = "\u00A75Der Spieler \u00A72%p \u00A75ist \u00A72%d \u00A75weit entfernt!";
    public static final String killedBy = "\u00A78Der Spieler %t \u00A78wurde von %k \u00A78get\u00F6tet!";
    public static final String fillSecondLine = "\u00A7cBitte gib eine Zahl in Zeile 2 ein!";
    public static final String teamFull = "Das Team ist voll!";
    public static final String alreadyInTeam = "Du bist bereits in diesem Team!";
    public static final String chatMessageAll = "\u00A78[\u00A77@all\u00A78] %p\u00A77: \u00A7f%msg";
    public static final String chatMessageTeam = "\u00A78[\u00A77Team\u00A78] %p\u00A77: \u00A7f%msg";
    public static final String chatMessageDead = "\u00A78[\u00A77Tot\u00A78] \u00A77\u00A7o%p\u00A7r\u00A77: \u00A7f%msg";
    public static final String chatMessageLobby = "%p\u00A77: \u00A7f%msg";
    public static final String teamInventoryName = "\u00A75Team \u00A72w\u00fchlen";
    // %t = TeamName
    public static final String teamJoinerSet = "\u00A7aTeam Joiner f\u00fcr %t erfolgreich gesetzt!";
    public static final String joinedTeam = "%p ist dem Team beigetreten!";
    public static final String noFittingTeamParty = "\u00A7cEs konnt kein passendes Team gefunden werden, da die Partygr\u00F6\u00DFe die maximale Teamgr\u00F6\u00DFe \u00fcbersteigt!";
    public static final String noFittingTeamPlayer = "\u00A7cEs konnt kein passendes Team gefunden werden, da kein Team genug Platz hat!";
    public static final String youChoseKit = "\u00A75Du hast das Kit \u00A72%k \u00A75ausgew\u00fchlt!";
    public static final String kitAlreadyChosen = "\u00A7cDu hast das Kit bereits ausgew\u00fchlt!";
    public static final String kitNotBought = "\u00A75Du hast das Kit \u00A72nicht \u00A75gekauft!";
    public static final String kickedForPremium = "\u00A7cDu wurdest gekickt um einen Premium Spieler Platz zu machen!";
    public static final String joinedTheGame = " \u00A78ist dem Spiel beigetreten!";
    public static final String nowLookingAtKit = "\u00A75Du schaust nun das Kit \u00A72%k \u00A75an!";
    public static final String playerKilledByPlayer = "\u00A78Der Spieler %p1 \u00A78wurde von %p2 \u00A78get\u00F6tet!";
    public static final String playerDied = "\u00A78Der Spieler %p \u00A78ist gestorben!";
    public static final String chestAddModeON = "\u00A7cDu bist jetzt im \u00A74KISTENHINZUF\u00fcGEMODUS!!!";
    public static final String chestAddModeOFF = "\u00A7cDu bist jetzt nicht mehr im \u00A74KISTENHINZUF\u00fcGEMODUS!!!";
    public static String kills = "\u00A78Kills:\u00A7a ";
    public static String deaths = "\u00A78Tode:\u00A7a ";
    public static String playedGames = "\u00A78Gespielt:\u00A7a ";
    public static String wunGames = "\u00A78Gewonnen:\u00A7a ";
    public static String kd = "\u00A78K/D:\u00A7a ";
    public static String rank = "\u00A75%p \u00A72#%rank";

    public static String addPrefix(String nachricht) {
        return Variables.prefix + nachricht;
    }
}
