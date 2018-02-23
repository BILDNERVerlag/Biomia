package de.biomia.sw.gamestates;

import de.biomia.sw.kits.Kits;
import de.biomia.sw.main.SkyWarsMain;
import de.biomia.sw.var.Scoreboards;
import de.biomia.sw.var.Teleport;
import de.biomia.sw.var.Variables;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.achievements.statEvents.skywars.SkyWarsEndEvent;
import de.biomiaAPI.achievements.statEvents.skywars.SkyWarsStartEvent;
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


        Bukkit.getPluginManager();

        SkyWarsMain.gameState = GameState.WAITINGFORSTART;
        Main.getBukkitTimoapi().getThisServer().setState(GameState.INGAME.name());


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
        SkyWarsMain.gameState = GameState.END;

        ArrayList<BiomiaPlayer> biomiaPlayers = new ArrayList<>();
        for (Player p : Variables.livingPlayer) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
            biomiaPlayers.add(bp);
        }
        Bukkit.getPluginManager().callEvent(new SkyWarsEndEvent(biomiaPlayers, duration, Biomia.TeamManager().getTeam(Variables.livingPlayer.get(0)).getTeamname()));
        End.start();
    }
}