package de.biomia.bw.gamestates;

import de.biomia.bw.listeners.SpawnItems;
import de.biomia.bw.main.BedWarsMain;
import de.biomia.bw.var.Scoreboards;
import de.biomia.bw.var.Teleport;
import de.biomia.bw.var.Variables;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.achievements.statEvents.bedwars.BedWarsEndEvent;
import de.biomiaAPI.achievements.statEvents.bedwars.BedWarsStartEvent;
import de.biomiaAPI.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class InGame {


    private static int duration;
    private static BukkitTask clock;

    public static void start() {

        clock = new BukkitRunnable() {
            @Override
            public void run() {
                duration++;
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);


        Main.getBukkitTimoapi().getThisServer().setState(GameState.INGAME.name());
        BedWarsMain.gameState = GameState.INGAME;

        HashMap<BiomiaPlayer, String> biomiaPlayerTeams = new HashMap<>();

        for (Player p : Bukkit.getOnlinePlayers()) {

            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

            biomiaPlayerTeams.put(bp, Biomia.TeamManager().getTeam(p).getTeamname());
            Bukkit.getPluginManager().callEvent(new BedWarsStartEvent(biomiaPlayerTeams));

            for (Player p2 : Bukkit.getOnlinePlayers()) {
                p.showPlayer(p2);
            }

            bp.setBuild(true);
            bp.setDamageEntitys(true);
            bp.setGetDamage(true);
            p.setGameMode(GameMode.SURVIVAL);
            Variables.livingPlayer.add(p);
            Scoreboards.setInGameScoreboard(p);
            p.getInventory().clear();
            p.setFlying(false);
            p.setAllowFlight(false);
        }

        Scoreboards.initSpectatorSB();
        Teleport.teleportTeamsToMap();
        SpawnItems.startSpawning();
    }

    public static void end() {

        clock.cancel();
        ArrayList<BiomiaPlayer> biomiaPlayersWinner = new ArrayList<>();

        BedWarsMain.gameState = GameState.END;
        SpawnItems.stopSpawning();
        for (Player p : Variables.livingPlayer) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
            biomiaPlayersWinner.add(bp);
        }
        Bukkit.getPluginManager().callEvent(new BedWarsEndEvent(biomiaPlayersWinner, duration, Biomia.TeamManager().getTeam(Variables.livingPlayer.get(0)).getTeamname()));
        End.start();
    }
}
