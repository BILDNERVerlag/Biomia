package de.biomia.sw.var;

import de.biomia.sw.gamestates.GameState;
import de.biomia.sw.main.SkyWarsMain;
import de.biomia.api.Biomia;
import de.biomia.api.main.Main;
import de.biomia.api.msg.ActionBar;
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
                    p.teleport(Variables.teamSpawns.get(Biomia.TeamManager().getTeam(p)));
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
                        ActionBar.sendActionBar("§8Los!", p);
                        Biomia.getBiomiaPlayer(p).setBuild(true);
                        Biomia.getBiomiaPlayer(p).setDamageEntitys(true);
                        Biomia.getBiomiaPlayer(p).setGetDamage(true);
                        SkyWarsMain.gameState = GameState.INGAME;
                        cancel();
                    } else {
                        ActionBar.sendActionBar("§8" + waitForStartCountdown, p);
                    }
                }
                waitForStartCountdown--;
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }

}
