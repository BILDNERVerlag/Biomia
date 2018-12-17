package de.biomia.spigot.messages;

import de.biomia.universal.Messages;

public class MinigamesMessages {
    /* $t = CountDown */
    public static final String restartCountDown = "Restart in %s Sekunden!";
    public static final String lobbyCountDown = "Start in %s Sekunden!";
    public static final String mapSize = "§b%s §7x §c%s";
    public static final String notEnoughPlayerToStart = Messages.format("Zu wenig Spieler! Warte auf weitere...");
    public static final String teamFull = Messages.format("Das Team ist voll!");
    public static final String alreadyInTeam = Messages.format("Du bist bereits in diesem Team!");
    public static final String chatMessageAll = "§8[§7@all§8] %p§7: §f%msg";
    public static final String chatMessageTeam = "§8[§7Team§8] %p§7: §f%msg";
    public static final String chatMessageDead = "§8[§7Tot§8] §7§o%p§r§7: §f%msg";
    public static final String chatMessageLobby = "%p§7: §f%msg";
    // %t = TeamName
    public static final String joinedTeam = "%s ist Team %s beigetreten!";
    public static final String gameAlreadyStarted = Messages.format("Das Spiel hat bereits begonnen!");
    public static final String noFittingTeamParty = Messages.format("Es konnte kein passendes Team gefunden werden, da die Partygröße die maximale Teamgröße übersteigt!");
    public static final String noFittingTeamPlayer = Messages.format("Es konnte kein passendes Team gefunden werden, da kein Team genug Platz hat!");
    public static final String serverFull = Messages.format("Der Server ist bereits voll!");
    public static final String kickedForPremium = Messages.format("Du wurdest gekickt um einen Premium Spieler Platz zu machen!");
    public static final String joinedTheGame = " ist dem Spiel beigetreten.";
    public static final String leftTheGame = " hat das Spiel verlassen.";
    public static final String explainMessages =
            "§7§m-------------------------------------------------§r\n" +
                    "§7Nur Teammitglieder können deine Chatnachrichten sehen.\n" +
                    "§7Tippe §b@a §7oder §b@all §7vor eine Nachricht, um an alle zu senden.\n" +
                    "§7§m-------------------------------------------------§r";
    public static final String playerKilledByPlayer = ">>Der Spieler %s wurde von %s getötet!";
    public static final String playerDied = "Der Spieler %s ist gestorben.";
    public static final String playerDiedFinally = "Der Spieler %s ist endgültig gestorben!";
    public static final String destroyOwnBed = Messages.format("Du kannst das eigene Bett nicht zerstören.\n Deine Mitspieler wären enttäuscht :(");
    public static final String rank = "§c%s §b#%s";
    public static final String winns = "§8Gewonnen: §b%s";
    public static final String kd = "§8K/D: §b%s";
    public static final String played = "§8Gespielt: §b%s";


}
