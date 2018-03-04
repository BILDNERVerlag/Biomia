package de.biomia.spigot.messages.manager;

import de.biomia.spigot.tools.RankManager;
import de.biomia.universal.UniversalBiomia;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Scoreboards {

    public static void setTabList(Player p) {

        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        int i = 0;
        for (String s : UniversalBiomia.RANK_NAMES_PREFIXES.keySet()) {

            Team t;

            if (i < 10)
                t = sb.registerNewTeam("0" + i + s);
            else
                t = sb.registerNewTeam(i + s);
            t.setPrefix(UniversalBiomia.RANK_NAMES_PREFIXES.get(s));

            i++;
        }
        String rank = RankManager.getRank(p);

        for (Player pl : Bukkit.getOnlinePlayers()) {
            for (Team t : sb.getTeams()) {
                if (t.getName().contains(RankManager.getRank(pl))) {
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
