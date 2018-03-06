package de.biomia.spigot.minigames.skywars.gamestates;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import de.biomia.spigot.Main;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.minigames.skywars.listeners.CountDown;
import de.biomia.spigot.minigames.skywars.lobby.JoinTeam;
import de.biomia.spigot.minigames.skywars.var.Scoreboards;
import de.biomia.spigot.minigames.skywars.var.Variables;
import org.bukkit.scheduler.BukkitRunnable;

public class InLobby {

    public static void start() {
        Scoreboards.initLobbySB();
        Variables.countDown = new CountDown();

        new BukkitRunnable() {
            @Override
            public void run() {
                TimoCloudAPI.getBukkitInstance().getThisServer().setExtra(BedWarsMessages.mapSize.replace("%s", Variables.teams + "").replace("%s", Variables.playerPerTeam + ""));
            }
        }.runTaskLater(Main.getPlugin(), 20);
    }

    public static void end() {
        Variables.countDown.getBukkitTask().cancel();
        JoinTeam.setAllToTeams();
        InGame.start();
    }
}
