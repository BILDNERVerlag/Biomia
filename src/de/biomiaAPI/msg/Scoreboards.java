package de.biomiaAPI.msg;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.biomiaAPI.main.Main;
import de.biomiaAPI.pex.Rank;

public class Scoreboards {

	public static void addToEachOther(Player p) {

		String rank = Rank.getRank(p);

		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (p != pl) {
				searchforteam: for (Team t : pl.getScoreboard().getTeams()) {
					if (t.getName().contains(rank)) {
						if (t.getPrefix().isEmpty()) {
							t.setPrefix(Rank.getPrefix(p));
						}
						t.addEntry(p.getDisplayName());
						break searchforteam;
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
			searchforteam: for (Team t : sb.getTeams()) {
				if (t.getName().contains(Rank.getRank(pl))) {
					if (t.getPrefix().isEmpty()) {
						t.setPrefix(Rank.getPrefix(pl));
					}
					t.addEntry(pl.getDisplayName());
					break searchforteam;
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
