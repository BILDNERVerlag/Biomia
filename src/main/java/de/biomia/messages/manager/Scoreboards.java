package de.biomia.messages.manager;

import de.biomia.Main;
import de.biomia.tools.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Scoreboards {

    //TODO remake

    private static void addToEachOther(Player p) {

        String rank = RankManager.getRank(p);

        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (p != pl) {
                for (Team t : pl.getScoreboard().getTeams()) {
                    if (t.getName().contains(rank)) {
                        if (t.getPrefix().isEmpty()) {
                            t.setPrefix(RankManager.getPrefix(p));
                        }
                        t.addEntry(p.getDisplayName());
                        break;
                    }
                }
            }
        }
    }

    public static void setTabList(Player p) {

        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();

        initScoreboard(sb);

        addToEachOther(p);

        for (Player pl : Bukkit.getOnlinePlayers()) {
            for (Team t : sb.getTeams()) {
                if (t.getName().contains(RankManager.getRank(pl))) {
                    if (t.getPrefix().isEmpty()) {
                        t.setPrefix(RankManager.getPrefix(pl));
                    }
                    t.addEntry(pl.getDisplayName());
                    break;
                }
            }
        }

        p.setScoreboard(sb);
    }

    private static void initScoreboard(Scoreboard sb) {

        int i = 0;
        for (String s : Main.RANK_NAMES_PREFIXES.keySet()) {

            if (i < 10)
                sb.registerNewTeam("0" + i + s);
            else
                sb.registerNewTeam(i + s);

            i++;
        }

    }

}
