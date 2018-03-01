package de.biomia.minigames.skywars.gamestates;

import de.biomia.minigames.GameState;
import de.biomia.minigames.skywars.listeners.CountDown;
import de.biomia.minigames.skywars.lobby.JoinTeam;
import de.biomia.general.messages.SkyWarsMessages;
import de.biomia.minigames.skywars.var.Scoreboards;
import de.biomia.minigames.skywars.var.Variables;
import de.biomia.Main;

public class InLobby {

    public static void start() {
        Scoreboards.initLobbySB();
        Variables.countDown = new CountDown();
        Variables.countDown.startCountDown();
        Main.getBukkitTimoapi().getThisServer().setExtra(SkyWarsMessages.mapSize.replaceAll("%mt", Variables.teams + "").replaceAll("%ts", Variables.playerPerTeam + ""));
        Main.getBukkitTimoapi().getThisServer().setState(GameState.LOBBY.name());
    }

    public static void end() {
        Variables.countDown.getBukkitTask().cancel();
        JoinTeam.setAllToTeams();
        InGame.start();
    }
}
