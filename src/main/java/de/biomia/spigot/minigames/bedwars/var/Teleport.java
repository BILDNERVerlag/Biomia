package de.biomia.spigot.minigames.bedwars.var;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.bedwars.BedWars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
        Iterator<BiomiaPlayer> players = BedWars.getBedWars().getInstance().getPlayers().iterator();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (players.hasNext()) {
                    BiomiaPlayer bp = players.next();
                    bp.getPlayer().setFallDistance(0);
                    bp.getPlayer().teleport(mode.getTeam(bp).getHome());
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
}