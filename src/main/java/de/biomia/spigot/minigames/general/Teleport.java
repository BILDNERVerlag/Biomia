package de.biomia.spigot.minigames.general;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.messages.manager.ActionBar;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameStateManager;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.bedwars.BedWarsTeam;
import de.biomia.spigot.minigames.skywars.SkyWars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Iterator;

public class Teleport {

    private static final HashMap<BiomiaPlayer, Location> starts = new HashMap<>();

    public static void teleportAllToWarteLobby(Location warteLobbySpawn) {

        Iterator<? extends Player> players = Bukkit.getOnlinePlayers().iterator();

        new BukkitRunnable() {

            @Override
            public void run() {
                if (players.hasNext()) {
                    players.next().teleport(warteLobbySpawn);
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 1);
    }

    public static void teleportPlayerToMap(GameMode mode) {
        Iterator<BiomiaPlayer> players = mode.getInstance().getPlayers().iterator();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (players.hasNext()) {
                    BiomiaPlayer bp = players.next();
                    bp.getPlayer().setFallDistance(0);
                    bp.getPlayer().teleport(bp.getTeam().getHome());
                    bp.sendMessage(MinigamesMessages.explainMessages);
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 1);
    }

    public static void teleportBackHome(BiomiaPlayer bp) {
        starts.put(bp, bp.getPlayer().getLocation());
    }

    public static Location getStartLocation(BiomiaPlayer bp) {
        return starts.get(bp);
    }

    public static void removeFromStartLocs(BiomiaPlayer bp) {
        starts.remove(bp);
    }

    public static void sendCountDown(GameMode mode) {
        new BukkitRunnable() {
            int waitForStartCountdown = 5;

            @Override
            public void run() {
                for (BiomiaPlayer p : mode.getInstance().getPlayers()) {
                    if (waitForStartCountdown == 0) {
                        ActionBar.sendActionBar("\u00A78Los!", p.getPlayer());
                        p.setBuild(true);
                        p.setDamageEntitys(true);
                        p.setGetDamage(true);
                        mode.getStateManager().setActualGameState(GameStateManager.GameState.INGAME);
                        cancel();
                    } else {
                        ActionBar.sendActionBar("\u00A78" + waitForStartCountdown, p.getPlayer());
                    }
                }
                waitForStartCountdown--;
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }
}