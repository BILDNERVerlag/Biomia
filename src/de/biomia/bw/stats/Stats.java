package de.biomia.bw.stats;

import de.biomiaAPI.Biomia;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

class Stats {

    public String name;
    public int kills = 0;
    public int wins = 0;
    public int deaths = 0;
    public int played_games = 0;
    public int rank = -1;

    public Stats(UUID uuid) {
        UUID uuid1 = uuid;
        //TODO Sinn..?
    }

    public static void getStats() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Biomia.TeamManager().isPlayerInAnyTeam(p);
        }
    }
}
