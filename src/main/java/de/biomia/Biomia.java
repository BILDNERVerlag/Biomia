package de.biomia;

import de.biomia.server.minigames.general.teams.TeamManager;
import de.biomia.server.quests.general.QuestManager;
import de.biomia.server.quests.general.QuestPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Biomia {

    private static final HashMap<Player, BiomiaPlayer> biomiaPlayers = new HashMap<>();
    private static final HashMap<Integer, OfflineBiomiaPlayer> offlineBiomiaPlayers = new HashMap<>();
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

    public static OfflineBiomiaPlayer getOfflineBiomiaPlayer(int biomiaID) {
        return offlineBiomiaPlayers.computeIfAbsent(biomiaID, biomiaplayer -> new OfflineBiomiaPlayer(biomiaID));
    }

    public static OfflineBiomiaPlayer getOfflineBiomiaPlayer(String name) {
        int biomiaPlayerID = OfflineBiomiaPlayer.getBiomiaPlayerID(name);
        return offlineBiomiaPlayers.computeIfAbsent(biomiaPlayerID, biomiaplayer -> new OfflineBiomiaPlayer(biomiaPlayerID, name));
    }

    public static OfflineBiomiaPlayer getOfflineBiomiaPlayer(UUID uuid) {
        int biomiaPlayerID = OfflineBiomiaPlayer.getBiomiaPlayerID(uuid);
        return offlineBiomiaPlayers.computeIfAbsent(biomiaPlayerID, biomiaplayer -> new OfflineBiomiaPlayer(biomiaPlayerID, uuid));
    }

    public static TeamManager getTeamManager() {
        return teamManager != null ? teamManager : (teamManager = new TeamManager());
    }

    public static QuestManager getQuestManager() {
        return questManager != null ? questManager : (questManager = new QuestManager());
    }

}
