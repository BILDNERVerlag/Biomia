package de.biomia.server.minigames.bedwars.gamestates;

import de.biomia.Main;
import de.biomia.messages.BedWarsMessages;
import de.biomia.server.minigames.bedwars.listeners.CountDown;
import de.biomia.server.minigames.bedwars.lobby.JoinTeam;
import de.biomia.server.minigames.bedwars.var.Scoreboards;
import de.biomia.server.minigames.bedwars.var.Variables;
import de.biomia.server.minigames.general.GameState;

public class InLobby {

    public static void start() {
        Scoreboards.initLobbySB();
        Variables.countDown = new CountDown();
        Main.getBukkitTimoapi().getThisServer().setExtra(BedWarsMessages.mapSize.replaceAll("%mt", Variables.teams + "").replaceAll("%ts", Variables.playerPerTeam + ""));
        Main.getBukkitTimoapi().getThisServer().setState(GameState.LOBBY.name());
    }

    public static void end() {
        Variables.countDown.getBukkitTask().cancel();
        JoinTeam.setAllToTeams();
        InGame.start();
    }
}