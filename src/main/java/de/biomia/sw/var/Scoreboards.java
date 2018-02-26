package de.biomia.sw.var;

import de.biomia.sw.messages.Messages;
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

        o.setDisplayName("00A76Sky00A7fWars");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);

        o.getScore(" ").setScore(7);
        o.getScore("00A75Map:").setScore(6);
        o.getScore("00A7c").setScore(5);
        o.getScore("00A7a").setScore(4);
        o.getScore("00A75Teams:").setScore(3);
        o.getScore("00A7f").setScore(2);
        o.getScore("00A71").setScore(1);

        Team map = lobbySB.registerNewTeam("map");
        Team teams = lobbySB.registerNewTeam("teams");

        map.addEntry("00A7c");
        teams.addEntry("00A7f");

        map.setPrefix("00A72" + Variables.name);
        teams.setPrefix("00A75" + Variables.teams + " 00A77x " + "00A72" + Variables.playerPerTeam);

        for (de.biomia.api.Teams.Team t : Biomia.TeamManager().getTeams()) {
            lobbySB.registerNewTeam("0" + t.getTeamname()).setPrefix(t.getColorcode());
        }
        lobbySB.registerNewTeam("noteam").setPrefix("00A77");

    }

    public static void setLobbyScoreboard(Player p) {
        p.setScoreboard(lobbySB);
    }

    // InGame
    public static void setInGameScoreboard(Player p) {
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective o = sb.registerNewObjective("aaa", "bbb");

        o.setDisplayName(Messages.skywars);
        o.setDisplaySlot(DisplaySlot.SIDEBAR);

        o.getScore(" ").setScore(7);
        o.getScore("00A75Kit:").setScore(6);
        o.getScore("00A7c").setScore(5);
        o.getScore("00A7a").setScore(4);
        o.getScore("00A75Team:").setScore(3);
        o.getScore("00A7f").setScore(2);
        o.getScore("00A71").setScore(1);

        Team kit = sb.registerNewTeam("kitname");
        Team team = sb.registerNewTeam("team");

        kit.addEntry("00A7c");
        team.addEntry("00A7f");

        String name = Biomia.TeamManager().translate(Biomia.TeamManager().getTeam(p).getTeamname());

        for (de.biomia.api.Teams.Team t : Biomia.TeamManager().getTeams()) {
            sb.registerNewTeam(t.getTeamname()).setPrefix(t.getColorcode());
        }

        for (Player pl : Bukkit.getOnlinePlayers()) {
            sb.getTeam(Biomia.TeamManager().getTeam(pl).getTeamname()).addEntry(pl.getName());
        }

        if (Variables.selectedKit.get(p) != null)
            kit.setPrefix("00A7c" + Variables.selectedKit.get(p).getName());
        else
            kit.setPrefix("00A7cKein Kit");

        team.setPrefix(Biomia.TeamManager().getTeam(p).getColorcode() + name);

        p.setScoreboard(sb);
    }

    public static void initSpectatorSB() {

        for (de.biomia.api.Teams.Team t : Biomia.TeamManager().getTeams()) {
            spectatorSB.registerNewTeam(t.getTeamname()).setPrefix(t.getColorcode());
        }

        spectatorSB.registerNewTeam("spectator").setPrefix("00A7700A7o");

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
