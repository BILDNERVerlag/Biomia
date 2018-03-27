package de.biomia.spigot;

import de.biomia.spigot.server.quests.general.QuestManager;
import de.biomia.spigot.server.quests.general.QuestPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Biomia {

    private static final HashMap<Player, BiomiaPlayer> biomiaPlayers = new HashMap<>();
    private static final HashMap<Integer, OfflineBiomiaPlayer> offlineBiomiaPlayers = new HashMap<>();
    private static QuestManager questManager;
    private static BiomiaServer serverInstance;


    public static void removeBiomiaPlayer(Player p) {
        biomiaPlayers.remove(p);
    }

    // GETTERS
    public static QuestPlayer getQuestPlayer(Player p) {
        return getBiomiaPlayer(p).getQuestPlayer();
    }

    public static BiomiaPlayer getBiomiaPlayer(Player p) {
        if (p == null) return null;
        return biomiaPlayers.computeIfAbsent(p, biomiaplayer -> new BiomiaPlayer(p));
    }

    public static OfflineBiomiaPlayer getOfflineBiomiaPlayer(int biomiaID) {
        String name = OfflineBiomiaPlayer.getName(biomiaID);
        if (name != null)
            return getOfflineBiomiaPlayer(name);
        else
            return offlineBiomiaPlayers.computeIfAbsent(biomiaID, biomiaplayer -> new OfflineBiomiaPlayer(biomiaID));
    }

    public static OfflineBiomiaPlayer getOfflineBiomiaPlayer(String name) {
        Player p = org.bukkit.Bukkit.getPlayer(name);
        if (p == null) {
            int biomiaID = OfflineBiomiaPlayer.getBiomiaPlayerID(name);
            return offlineBiomiaPlayers.computeIfAbsent(biomiaID, biomiaplayer -> new OfflineBiomiaPlayer(biomiaID, name));
        } else {
            return getBiomiaPlayer(p);
        }
    }

    public static QuestManager getQuestManager() {
        return questManager != null ? questManager : (questManager = new QuestManager());
    }

    public static void setServerInstance(BiomiaServer serverInstance) {
        Biomia.serverInstance = serverInstance;
    }

    public static BiomiaServer getServerInstance() {
        return serverInstance;
    }
}
