package de.biomia.spigot.messages;

import de.biomia.universal.Messages;

public class MinigamesMessages {
    @SuppressWarnings("MalformedFormatString") /* $t = CountDown */
    public static final String restartCountDown = String.format("%sRestart in %s$t %sSekunden!", Messages.COLOR_AUX, Messages.COLOR_SUB, Messages.COLOR_AUX);
    public static final String lobbyCountDown = Messages.PREFIX + "\u00A77Start in \u00A7b%t \u00A77Sekunden!";
    public static final String mapSize = "\u00A7b%s \u00A77x \u00A7c%s";
    public static final String notEnoughPlayerToStart = Messages.PREFIX + "\u00A7cZu wenig Spieler! Warte auf weitere...";
    public static final String teamFull = Messages.PREFIX + "\u00A7cDas Team ist voll!";
    public static final String alreadyInTeam = Messages.PREFIX + "\u00A7cDu bist bereits in diesem Team!";
    public static final String chatMessageAll = "\u00A78[\u00A77@all\u00A78] %p\u00A77: \u00A7f%msg";
    public static final String chatMessageTeam = "\u00A78[\u00A77Team\u00A78] %p\u00A77: \u00A7f%msg";
    public static final String chatMessageDead = "\u00A78[\u00A77Tot\u00A78] \u00A77\u00A7o%p\u00A7r\u00A77: \u00A7f%msg";
    public static final String chatMessageLobby = "%p\u00A77: \u00A7f%msg";
    // %t = TeamName
    public static final String joinedTeam = "%p ist Team %t beigetreten!";
    public static final String gameAlreadyStarted = "\u00A7cDas Spiel hat bereits begonnen!";
    public static final String noFittingTeamParty = "\u00A7cEs konnt kein passendes Team gefunden werden, da die Partygr00f600dfe die maximale Teamgr00f600dfe 00fcbersteigt!";
    public static final String noFittingTeamPlayer = "\u00A7cEs konnt kein passendes Team gefunden werden, da kein Team genug Platz hat!";
    public static final String serverFull = "\u00A7cDer Server ist bereits voll!";
    public static final String kickedForPremium = "\u00A7cDu wurdest gekickt um einen Premium Spieler Platz zu machen!";
    public static final String joinedTheGame = "\u00A77 ist dem Spiel beigetreten.";
    public static final String leftTheGame = "\u00A77 hat das Spiel verlassen.";
    public static final String explainMessages =
            "\u00A77\u00A7m-------------------------------------------------\u00A7r\n" +
                    "\u00A77Nur Teammitglieder k00f6nnen deine Chatnachrichten sehen.\n" +
                    "\u00A77Tippe \u00A7b@a \u00A77oder \u00A7b@all \u00A77vor eine Nachricht, um an alle zu senden.\n" +
                    "\u00A77\u00A7m-------------------------------------------------\u00A7r";
    public static final String playerKilledByPlayer = "\u00A77>>Der Spieler %s \u00A77wurde von %s \u00A77get00f6tet!";
    public static final String playerDied = "\u00A77Der Spieler %p \u00A77ist gestorben.";
    public static final String playerDiedFinally = "\u00A77Der Spieler %p \u00A77ist endg00fcltig gestorben!";
    public static final String destroyOwnBed = "\u00A77Du kannst das eigene Bett nicht zerst00f6ren.\n Deine Mitspieler w00e4ren entt00e4uscht :(";

    public static final String rank = "\u00A7c%s \u00A7b#%s";
    public static final String winns = "\u00A78Gewonnen: \u00A7b%s";
    public static final String kd = "\u00A78K/D: \u00A7b%s";
    public static final String played = "\u00A78Gespielt: \u00A7b%s";


}
