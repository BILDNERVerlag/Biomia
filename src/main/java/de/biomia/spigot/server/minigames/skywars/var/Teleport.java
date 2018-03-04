package de.biomia.spigot.server.minigames.skywars.var;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.Main;
import de.biomia.spigot.messages.manager.ActionBar;
import de.biomia.spigot.server.minigames.general.GameState;
import de.biomia.spigot.server.minigames.skywars.SkyWars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class Teleport {

    public static void teleportAllToWarteLobby() {

        Iterator<? extends Player> players = Bukkit.getOnlinePlayers().iterator();

        new BukkitRunnable() {

            @Override
            public void run() {

                if (players.hasNext()) {
                    players.next().teleport(Variables.warteLobbySpawn);
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 1);
    }

    public static void teleportTeamsToMap() {

        Iterator<? extends Player> players = Bukkit.getOnlinePlayers().iterator();

        new BukkitRunnable() {

            @Override
            public void run() {
                if (players.hasNext()) {
                    Player p = players.next();
                    p.teleport(Variables.teamSpawns.get(Biomia.getTeamManager().getTeam(p)));
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 1);
    }

    public static void sendCountDown() {
        new BukkitRunnable() {
            int waitForStartCountdown = 5;

            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (waitForStartCountdown == 0) {
                        ActionBar.sendActionBar("\u00A78Los!", p);
                        Biomia.getBiomiaPlayer(p).setBuild(true);
                        Biomia.getBiomiaPlayer(p).setDamageEntitys(true);
                        Biomia.getBiomiaPlayer(p).setGetDamage(true);
                        SkyWars.gameState = GameState.INGAME;
                        cancel();
                    } else {
                        ActionBar.sendActionBar("\u00A78" + waitForStartCountdown, p);
                    }
                }
                waitForStartCountdown--;
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }

}
