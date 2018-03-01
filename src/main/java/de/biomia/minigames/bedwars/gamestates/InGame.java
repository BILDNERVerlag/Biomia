package de.biomia.minigames.bedwars.gamestates;

import de.biomia.minigames.bedwars.listeners.SpawnItems;
import de.biomia.minigames.bedwars.BedWars;
import de.biomia.minigames.bedwars.var.Scoreboards;
import de.biomia.minigames.bedwars.var.Teleport;
import de.biomia.minigames.bedwars.var.Variables;
import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.achievements.statEvents.bedwars.BedWarsEndEvent;
import de.biomia.api.achievements.statEvents.bedwars.BedWarsStartEvent;
import de.biomia.Main;
import de.biomia.minigames.GameState;
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
        BedWars.gameState = GameState.INGAME;

        HashMap<BiomiaPlayer, String> biomiaPlayerTeams = new HashMap<>();

        for (Player p : Bukkit.getOnlinePlayers()) {

            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

            biomiaPlayerTeams.put(bp, Biomia.getTeamManager().getTeam(p).getTeamname());
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

        BedWars.gameState = GameState.END;
        SpawnItems.stopSpawning();
        for (Player p : Variables.livingPlayer) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
            biomiaPlayersWinner.add(bp);
        }
        Bukkit.getPluginManager().callEvent(new BedWarsEndEvent(biomiaPlayersWinner, duration, Biomia.getTeamManager().getTeam(Variables.livingPlayer.get(0)).getTeamname()));
        End.start();
    }
}
