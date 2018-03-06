package de.biomia.spigot.minigames.bedwars.gamestates;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import de.biomia.spigot.Main;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.minigames.bedwars.listeners.CountDown;
import de.biomia.spigot.minigames.bedwars.lobby.JoinTeam;
import de.biomia.spigot.minigames.bedwars.var.Scoreboards;
import de.biomia.spigot.minigames.bedwars.var.Variables;
import org.bukkit.scheduler.BukkitRunnable;

public class InLobby {

    public static void start() {
        Scoreboards.initLobbySB();
        Variables.countDown = new CountDown();
        new BukkitRunnable() {
            @Override
            public void run() {
                TimoCloudAPI.getBukkitInstance().getThisServer().setExtra(String.format(BedWarsMessages.mapSize, Variables.teams + "", Variables.playerPerTeam + ""));
            }
        }.runTaskLater(Main.getPlugin(), 20);

    }

    public static void end() {
        Variables.countDown.getBukkitTask().cancel();
        JoinTeam.setAllToTeams();
        InGame.start();
    }
}