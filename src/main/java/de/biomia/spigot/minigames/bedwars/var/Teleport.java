package de.biomia.spigot.minigames.bedwars.var;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.Main;
import de.biomia.spigot.minigames.general.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;

public class Teleport {

    private static final HashMap<Player, Location> starts = new HashMap<>();

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

    public static void teleportPlayerToMap(HashMap<Team, Location> spawns) {

        Iterator<? extends Player> players = Bukkit.getOnlinePlayers().iterator();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (players.hasNext()) {
                    Player p = players.next();
                    p.setFallDistance(0);
                    p.teleport(spawns.get(Biomia.getTeamManager().getTeam(p)));
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 1);
    }

    public static void teleportBackHome(Player p) {
        starts.put(p, p.getLocation());
    }

    public static Location getStartLocation(Player p) {
        return starts.get(p);
    }

    public static void removeFromStartLocs(Player p) {
        starts.remove(p);
    }
}