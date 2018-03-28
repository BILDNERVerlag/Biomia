package de.biomia.spigot.messages.manager;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.server.lobby.LobbyScoreboard;
import de.biomia.universal.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Scoreboards {

    public static void setTabList(Player p) {

        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        LobbyScoreboard.initScoreboard(sb);
        String rank = Biomia.getBiomiaPlayer(p).getRank().name();

        for (Player pl : Bukkit.getOnlinePlayers()) {
            for (Team t : sb.getTeams()) {
                if (t.getName().contains(Biomia.getBiomiaPlayer(pl).getRank().name())) {
                    t.addEntry(pl.getDisplayName());
                    break;
                }
            }
            for (Team t : pl.getScoreboard().getTeams()) {
                if (t.getName().contains(rank))
                    t.addEntry(pl.getDisplayName());
            }
        }
        p.setScoreboard(sb);
    }
}
