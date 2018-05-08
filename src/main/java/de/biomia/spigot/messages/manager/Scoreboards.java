package de.biomia.spigot.messages.manager;

import de.biomia.spigot.Biomia;
import de.biomia.universal.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Scoreboards {

    private static final Scoreboard main = Bukkit.getScoreboardManager().getNewScoreboard();

    static {
        initScoreboard(main);
    }

    public static Scoreboard setTabList(Player p, boolean send, boolean ownScoreboard) {

        if (ownScoreboard) {
            Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
            initScoreboard(sb);
            Ranks bpRank = Biomia.getBiomiaPlayer(p).getRank();

            for (Player pl : Bukkit.getOnlinePlayers()) {
                Scoreboard asb = pl.getScoreboard();
                Ranks ranks = Biomia.getBiomiaPlayer(pl).getRank();
                for (Team t : asb.getTeams())
                    if (t.getName().contains(bpRank.name())) {
                        t.addEntry(p.getName());
                        break;
                    }
                for (Team t : sb.getTeams())
                    if (t.getName().contains(ranks.name())) {
                        t.addEntry(pl.getName());
                        break;
                    }
            }

            if (send)
                p.setScoreboard(sb);

            return sb;
        } else {

            Ranks bpRank = Biomia.getBiomiaPlayer(p).getRank();

            for (Team t : main.getTeams())
                if (t.getName().contains(bpRank.name())) {
                    t.addEntry(p.getName());
                    break;
                }

            if (send) {
                p.setScoreboard(main);
            }
            return main;
        }
    }

    private static void initScoreboard(Scoreboard sb) {
        for (Ranks r : Ranks.values()) {
            Team t;
            if (r.getLevel() < 10)
                t = sb.registerNewTeam("0" + r.getLevel() + r.name());
            else
                t = sb.registerNewTeam(r.getLevel() + r.name());
            t.setPrefix(r.getPrefix());
        }
    }

    public static Scoreboard getMainScoreboard() {
        return main;
    }
}
