package de.biomia.server.minigames.skywars.gamestates;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import de.biomia.messages.BedWarsMessages;
import de.biomia.server.minigames.skywars.listeners.CountDown;
import de.biomia.server.minigames.skywars.lobby.JoinTeam;
import de.biomia.server.minigames.skywars.var.Scoreboards;
import de.biomia.server.minigames.skywars.var.Variables;

public class InLobby {

    public static void start() {
        Scoreboards.initLobbySB();
        Variables.countDown = new CountDown();
        Variables.countDown.startCountDown();
        TimoCloudAPI.getBukkitInstance().getThisServer().setExtra(String.format(BedWarsMessages.mapSize, Variables.teams, Variables.playerPerTeam));
    }

    public static void end() {
        Variables.countDown.getBukkitTask().cancel();
        JoinTeam.setAllToTeams();
        InGame.start();
    }
}
