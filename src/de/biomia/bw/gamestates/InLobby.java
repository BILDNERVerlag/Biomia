package de.biomia.bw.gamestates;

import de.biomia.bw.listeners.CountDown;
import de.biomia.bw.lobby.JoinTeam;
import de.biomia.bw.messages.Messages;
import de.biomia.bw.var.Scoreboards;
import de.biomia.bw.var.Variables;
import de.biomiaAPI.main.Main;

public class InLobby {

    public static void start() {
        Scoreboards.initLobbySB();
        Variables.countDown = new CountDown();
        Main.getBukkitTimoapi().getThisServer().setExtra(Messages.mapSize.replaceAll("%mt", Variables.teams + "").replaceAll("%ts", Variables.playerPerTeam + ""));
        Main.getBukkitTimoapi().getThisServer().setState(GameState.LOBBY.name());
    }

    public static void end() {
        Variables.countDown.getBukkitTask().cancel();
        JoinTeam.setAllToTeams();
        InGame.start();
    }
}