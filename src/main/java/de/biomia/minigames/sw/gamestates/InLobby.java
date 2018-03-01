package de.biomia.minigames.sw.gamestates;

import de.biomia.minigames.GameState;
import de.biomia.minigames.sw.listeners.CountDown;
import de.biomia.minigames.sw.lobby.JoinTeam;
import de.biomia.minigames.sw.messages.Messages;
import de.biomia.minigames.sw.var.Scoreboards;
import de.biomia.minigames.sw.var.Variables;
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
