package de.biomia.spigot.messages;

import de.biomia.universal.Messages;

public class MinigamesMessages {
    @SuppressWarnings("MalformedFormatString") /* $t = CountDown */
    public static final String restartCountDown = String.format("%sRestart in %s$t %sSekunden!", Messages.COLOR_AUX, Messages.COLOR_SUB, Messages.COLOR_AUX);
    public static final String lobbyCountDown = Messages.PREFIX + "§7Start in §b%t §7Sekunden!";
    public static final String mapSize = "§b%s §7x §c%s";
    public static final String notEnoughPlayerToStart = Messages.PREFIX + "§cZu wenig Spieler! Warte auf weitere...";
    public static final String teamFull = Messages.PREFIX + "§cDas Team ist voll!";
    public static final String alreadyInTeam = Messages.PREFIX + "§cDu bist bereits in diesem Team!";
    public static final String chatMessageAll = "§8[§7@all§8] %p§7: §f%msg";
    public static final String chatMessageTeam = "§8[§7Team§8] %p§7: §f%msg";
    public static final String chatMessageDead = "§8[§7Tot§8] §7§o%p§r§7: §f%msg";
    public static final String chatMessageLobby = "%p§7: §f%msg";
    // %t = TeamName
    public static final String joinedTeam = "%p ist Team %t beigetreten!";
    public static final String gameAlreadyStarted = "§cDas Spiel hat bereits begonnen!";
    public static final String noFittingTeamParty = "§cEs konnt kein passendes Team gefunden werden, da die Partygröße die maximale Teamgröße übersteigt!";
    public static final String noFittingTeamPlayer = "§cEs konnt kein passendes Team gefunden werden, da kein Team genug Platz hat!";
    public static final String serverFull = "§cDer Server ist bereits voll!";
    public static final String kickedForPremium = "§cDu wurdest gekickt um einen Premium Spieler Platz zu machen!";
    public static final String joinedTheGame = "§7 ist dem Spiel beigetreten.";
    public static final String leftTheGame = "§7 hat das Spiel verlassen.";
    public static final String explainMessages =
            "§7§m-------------------------------------------------§r\n" +
                    "§7Nur Teammitglieder können deine Chatnachrichten sehen.\n" +
                    "§7Tippe §b@a §7oder §b@all §7vor eine Nachricht, um an alle zu senden.\n" +
                    "§7§m-------------------------------------------------§r";
    public static final String playerKilledByPlayer = "§7>>Der Spieler %s §7wurde von %s §7getötet!";
    public static final String playerDied = "§7Der Spieler %p §7ist gestorben.";
    public static final String playerDiedFinally = "§7Der Spieler %p §7ist endgültig gestorben!";
    public static final String destroyOwnBed = "§7Du kannst das eigene Bett nicht zerstören.\n Deine Mitspieler wären enttäuscht :(";

    public static final String rank = "§c%s §b#%s";
    public static final String winns = "§8Gewonnen: §b%s";
    public static final String kd = "§8K/D: §b%s";
    public static final String played = "§8Gespielt: §b%s";


}
