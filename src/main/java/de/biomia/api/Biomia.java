package de.biomia.api;

import de.biomia.api.Teams.TeamManager;
import de.biomia.Main;
import de.biomia.quests.general.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Biomia {

    private static final HashMap<Player, BiomiaPlayer> biomiaPlayers = new HashMap<>();
    private static TeamManager teamManager;
    private static QuestManager questManager;

    // METHODS
    public static void stopWithDelay() {
        new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers())
                    p.kickPlayer(null);
                Bukkit.shutdown();
            }
        }.runTaskLater(Main.plugin, 10 * 20);
    }

    public static void removeBiomiaPlayer(Player p) {
        biomiaPlayers.remove(p);
    }

    // GETTERS
    public static QuestPlayer getQuestPlayer(Player p) {
        return getBiomiaPlayer(p).getQuestPlayer();
    }

    public static BiomiaPlayer getBiomiaPlayer(Player p) {
        return biomiaPlayers.computeIfAbsent(p, biomiaplayer -> new BiomiaPlayer(p));
    }

    public static TeamManager getTeamManager() {
        return teamManager != null ? teamManager : new TeamManager();
    }

    public static QuestManager getQuestManager() {
        return questManager != null ? questManager : new QuestManager();
    }

}
