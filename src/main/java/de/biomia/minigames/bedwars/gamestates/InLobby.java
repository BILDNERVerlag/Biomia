package de.biomia.minigames.bedwars.gamestates;

import de.biomia.minigames.bedwars.listeners.CountDown;
import de.biomia.minigames.bedwars.lobby.JoinTeam;
import de.biomia.minigames.bedwars.messages.Messages;
import de.biomia.minigames.bedwars.var.Scoreboards;
import de.biomia.minigames.bedwars.var.Variables;
import de.biomia.Main;
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