package de.biomia.bw.var;

import de.biomia.api.Biomia;
import de.biomia.api.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;

public class Teleport {

    private static final HashMap<Player, Location> starts = new HashMap<>();

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

        if (Variables.teamSpawns.size() < Variables.teams) {
            Bukkit.broadcastMessage("\u00A7cZu wenig Spawn Locations!");
            Biomia.stopWithDelay();
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (players.hasNext()) {
                    Player p = players.next();
                    p.setFallDistance(0);
                    p.teleport(Variables.teamSpawns.get(Biomia.getTeamManager().getTeam(p)));
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