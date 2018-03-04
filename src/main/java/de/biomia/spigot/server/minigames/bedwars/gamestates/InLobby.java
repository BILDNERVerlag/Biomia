package de.biomia.spigot.server.minigames.bedwars.gamestates;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.server.minigames.bedwars.listeners.CountDown;
import de.biomia.spigot.server.minigames.bedwars.lobby.JoinTeam;
import de.biomia.spigot.server.minigames.bedwars.var.Scoreboards;
import de.biomia.spigot.server.minigames.bedwars.var.Variables;
import de.biomia.spigot.server.minigames.general.GameState;

public class InLobby {

    public static void start() {
        Scoreboards.initLobbySB();
        Variables.countDown = new CountDown();
        TimoCloudAPI.getBukkitInstance().getThisServer().setExtra(String.format(BedWarsMessages.mapSize, Variables.teams, Variables.playerPerTeam));
    }

    public static void end() {
        Variables.countDown.getBukkitTask().cancel();
        JoinTeam.setAllToTeams();
        InGame.start();
    }
}