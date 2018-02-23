package de.biomiaAPI.msg;

import de.biomiaAPI.main.Main;
import de.biomiaAPI.pex.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Scoreboards {

	private static void addToEachOther(Player p) {

		String rank = Rank.getRank(p);

		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (p != pl) {
				for (Team t : pl.getScoreboard().getTeams()) {
					if (t.getName().contains(rank)) {
						if (t.getPrefix().isEmpty()) {
							t.setPrefix(Rank.getPrefix(p));
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
                if (t.getName().contains(Rank.getRank(pl))) {
                    if (t.getPrefix().isEmpty()) {
                        t.setPrefix(Rank.getPrefix(pl));
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

		for (String s : Main.group) {

			if (i < 10)
				sb.registerNewTeam("0" + i + s);
			else
				sb.registerNewTeam(i + s);

			i++;
		}

	}

}
