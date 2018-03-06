package de.biomia.spigot.minigames.skywars.gamestates;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.events.skywars.SkyWarsEndEvent;
import de.biomia.spigot.events.skywars.SkyWarsStartEvent;
import de.biomia.spigot.minigames.GameState;
import de.biomia.spigot.minigames.skywars.SkyWars;
import de.biomia.spigot.minigames.skywars.kits.Kits;
import de.biomia.spigot.minigames.skywars.var.Scoreboards;
import de.biomia.spigot.minigames.skywars.var.Teleport;
import de.biomia.spigot.minigames.skywars.var.Variables;
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


        Bukkit.getPluginManager();

        SkyWars.gameState = GameState.WAITING_FOR_START;
        TimoCloudAPI.getBukkitInstance().getThisServer().setState(GameState.INGAME.name());


        HashMap<BiomiaPlayer, Integer> biomiaPlayerKits = new HashMap<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            for (Player p2 : Bukkit.getOnlinePlayers()) {
                p.showPlayer(p2);
            }
            p.setGameMode(GameMode.SURVIVAL);
            Variables.livingPlayer.add(p);
            Scoreboards.setInGameScoreboard(p);
            p.getInventory().clear();
            Kits.setKitInventory(p);
            p.setFlying(false);
            p.setAllowFlight(false);
            biomiaPlayerKits.put(Biomia.getBiomiaPlayer(p), Kits.getSelectedKit(p).getID());
        }
        Bukkit.getPluginManager().callEvent(new SkyWarsStartEvent(biomiaPlayerKits));
        Scoreboards.initSpectatorSB();
        Teleport.teleportTeamsToMap();
        Teleport.sendCountDown();
    }

    public static void end() {
        clock.cancel();
        SkyWars.gameState = GameState.END;

        ArrayList<BiomiaPlayer> biomiaPlayers = new ArrayList<>();
        for (Player p : Variables.livingPlayer) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
            biomiaPlayers.add(bp);
        }
        Bukkit.getPluginManager().callEvent(new SkyWarsEndEvent(biomiaPlayers, duration, Biomia.getTeamManager().getTeam(Variables.livingPlayer.get(0)).getTeamname()));
        End.start();
    }
}
