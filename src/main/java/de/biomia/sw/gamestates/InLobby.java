package de.biomia.sw.gamestates;

import de.biomia.sw.listeners.CountDown;
import de.biomia.sw.lobby.JoinTeam;
import de.biomia.sw.messages.Messages;
import de.biomia.sw.var.Scoreboards;
import de.biomia.sw.var.Variables;
import de.biomia.api.main.Main;

public class InLobby {

    public static void start() {
        Scoreboards.initLobbySB();
        Variables.countDown = new CountDown();
        Variables.countDown.startCountDown();
        Main.getBukkitTimoapi().getThisServer().setExtra(Messages.mapSize.replaceAll("%mt", Variables.teams + "").replaceAll("%ts", Variables.playerPerTeam + ""));
        Main.getBukkitTimoapi().getThisServer().setState(GameState.LOBBY.name());
    }

    public static void end() {
        Variables.countDown.getBukkitTask().cancel();
        JoinTeam.setAllToTeams();
        InGame.start();
    }
}
