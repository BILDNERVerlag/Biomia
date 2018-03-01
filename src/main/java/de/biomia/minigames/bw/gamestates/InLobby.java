package de.biomia.minigames.bw.gamestates;

import de.biomia.minigames.bw.listeners.CountDown;
import de.biomia.minigames.bw.lobby.JoinTeam;
import de.biomia.minigames.bw.messages.Messages;
import de.biomia.minigames.bw.var.Scoreboards;
import de.biomia.minigames.bw.var.Variables;
import de.biomia.api.main.Main;
import de.biomia.minigames.GameState;

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