package de.biomia.bw.var;

import de.biomia.bw.messages.Messages;
import de.biomia.api.Biomia;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Scoreboards {

    // Lobby
    public static final Scoreboard lobbySB = Bukkit.getScoreboardManager().getNewScoreboard();
    // Spectator
    public static final Scoreboard spectatorSB = Bukkit.getScoreboardManager().getNewScoreboard();

    public static void initLobbySB() {

        Objective o = lobbySB.registerNewObjective("ccc", "ddd");

        o.setDisplayName(Messages.bedwars);
        o.setDisplaySlot(DisplaySlot.SIDEBAR);

        o.getScore(" ").setScore(7);
        o.getScore("§5Map:").setScore(6);
        o.getScore("§c").setScore(5);
        o.getScore("§a").setScore(4);
        o.getScore("§5Teams:").setScore(3);
        o.getScore("§f").setScore(2);
        o.getScore("§1").setScore(1);

        Team map = lobbySB.registerNewTeam("map");
        Team teams = lobbySB.registerNewTeam("teams");

        map.addEntry("§c");
        teams.addEntry("§f");

        map.setPrefix("§2" + Variables.name);
        teams.setPrefix("§5" + Variables.teams + " §7x " + "§2" + Variables.playerPerTeam);

        for (de.biomia.api.Teams.Team t : Biomia.TeamManager().getTeams()) {
            lobbySB.registerNewTeam("0" + t.getTeamname()).setPrefix(t.getColorcode());
        }
        lobbySB.registerNewTeam("noteam").setPrefix("§7");

    }

    public static void setLobbyScoreboard(Player p) {
        p.setScoreboard(lobbySB);
    }

    // InGame
    public static void setInGameScoreboard(Player p) {
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective o = sb.registerNewObjective("aaa", "bbb");

        o.setDisplayName(Messages.bedwars);
        o.setDisplaySlot(DisplaySlot.SIDEBAR);

        o.getScore(" ").setScore(3);
        o.getScore("§5Team:").setScore(2);
        o.getScore("§c").setScore(1);

        Team team = sb.registerNewTeam("team");
        team.addEntry("§c");

        String name = Biomia.TeamManager().translate(Biomia.TeamManager().getTeam(p).getTeamname());

        for (de.biomia.api.Teams.Team t : Biomia.TeamManager().getTeams()) {
            sb.registerNewTeam(t.getTeamname()).setPrefix(t.getColorcode());
        }

        for (Player pl : Bukkit.getOnlinePlayers()) {
            sb.getTeam(Biomia.TeamManager().getTeam(pl).getTeamname()).addEntry(pl.getName());
        }

        team.setPrefix(Biomia.TeamManager().getTeam(p).getColorcode() + name);

        p.setScoreboard(sb);
    }

    public static void initSpectatorSB() {

        for (de.biomia.api.Teams.Team t : Biomia.TeamManager().getTeams()) {
            spectatorSB.registerNewTeam(t.getTeamname()).setPrefix(t.getColorcode());
        }

        spectatorSB.registerNewTeam("spectator").setPrefix("§7§o");

        for (Player pl : Bukkit.getOnlinePlayers()) {

            if (Variables.livingPlayer.contains(pl)) {
                spectatorSB.getTeam(Biomia.TeamManager().getTeam(pl).getTeamname()).addEntry(pl.getName());
            } else {
                spectatorSB.getTeam("spectator").addEntry(pl.getName());
            }
        }

    }

    public static void setSpectatorSB(Player p) {
        p.setScoreboard(spectatorSB);
    }

}
