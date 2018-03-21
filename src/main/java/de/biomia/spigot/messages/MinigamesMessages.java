package de.biomia.spigot.messages;

import de.biomia.universal.Messages;

public class MinigamesMessages {
    // %t = CountDown
    public static final String restartCountDown = "\u00A7cRestart in \u00A76%t \u00A7cSekunden!";
    public static final String lobbyCountDown = Messages.PREFIX + "\u00A75Start in \u00A72%t \u00A75Sekunden!";
    public static final String mapSize = "\u00A7b%s \u00A77x \u00A7c%s";
    public static final String notEnoughPlayerToStart = Messages.PREFIX + "\u00A7cZu wenig Spieler! Warte auf weitere...";
    public static final String teamFull = Messages.PREFIX + "\u00A7cDas Team ist voll!";
    public static final String alreadyInTeam = Messages.PREFIX + "\u00A7cDu bist bereits in diesem Team!";
    public static final String chatMessageAll = "\u00A78[\u00A77@all\u00A78] %p\u00A77: \u00A7f%msg";
    public static final String chatMessageTeam = "\u00A78[\u00A77Team\u00A78] %p\u00A77: \u00A7f%msg";
    public static final String chatMessageDead = "\u00A78[\u00A77Tot\u00A78] \u00A77\u00A7o%p\u00A7r\u00A77: \u00A7f%msg";
    public static final String chatMessageLobby = "%p\u00A77: \u00A7f%msg";
    // %t = TeamName
    public static final String joinedTeam = "%p ist dem Team beigetreten!";
    public static final String noFittingTeamParty = "\u00A7cEs konnt kein passendes Team gefunden werden, da die Partygr\u00F6\u00DFe die maximale Teamgr\u00F6\u00DFe \u00fcbersteigt!";
    public static final String noFittingTeamPlayer = "\u00A7cEs konnt kein passendes Team gefunden werden, da kein Team genug Platz hat!";
    public static final String kickedForPremium = "\u00A7cDu wurdest gekickt um einen Premium Spieler Platz zu machen!";
    public static final String joinedTheGame = " \u00A78ist dem Spiel beigetreten!";
    public static final String playerKilledByPlayer = "\u00A78Der Spieler %s \u00A78wurde von %s \u00A78get\u00F6tet!";
    public static final String playerDied = "\u00A78Der Spieler %p \u00A78ist gestorben!";
    public static final String playerDiedFinally = "\u00A78Der Spieler %p \u00A78ist endg\u00fcltig gestorben!";
}
