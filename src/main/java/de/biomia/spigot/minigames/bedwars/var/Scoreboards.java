package de.biomia.spigot.minigames.bedwars.var;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.bedwars.BedWars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Scoreboards {

    // VersusLobbyListener
    public static final Scoreboard lobbySB = Bukkit.getScoreboardManager().getNewScoreboard();
    // Spectator
    public static final Scoreboard spectatorSB = Bukkit.getScoreboardManager().getNewScoreboard();

    public static void initLobbySB() {

        Objective o = lobbySB.registerNewObjective("ccc", "ddd");

        o.setDisplayName(BedWarsMessages.bedwars);
        o.setDisplaySlot(DisplaySlot.SIDEBAR);

        o.getScore(" ").setScore(7);
        o.getScore("\u00A75Map:").setScore(6);
        o.getScore("\u00A7c").setScore(5);
        o.getScore("\u00A7a").setScore(4);
        o.getScore("\u00A75Teams:").setScore(3);
        o.getScore("\u00A7f").setScore(2);
        o.getScore("\u00A71").setScore(1);

        Team map = lobbySB.registerNewTeam("map");
        Team teams = lobbySB.registerNewTeam("teams");

        map.addEntry("\u00A7c");
        teams.addEntry("\u00A7f");

        map.setPrefix("\u00A72" + Variables.name);
        teams.setPrefix("\u00A75" + Variables.teams + " \u00A77x " + "\u00A72" + Variables.playerPerTeam);

        for (GameTeam t : BedWars.getBedWars().getTeams()) {
            lobbySB.registerNewTeam(t.getTeamname()).setPrefix(t.getColorcode());
        }
        lobbySB.registerNewTeam("xnoteam").setPrefix("\u00A77");

    }

    public static void setLobbyScoreboard(Player p) {
        p.setScoreboard(lobbySB);
    }

    // InGame
    public static void setInGameScoreboard(Player p) {
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective o = sb.registerNewObjective("aaa", "bbb");

        o.setDisplayName(BedWarsMessages.bedwars);
        o.setDisplaySlot(DisplaySlot.SIDEBAR);

        o.getScore(" ").setScore(3);
        o.getScore("\u00A75Team:").setScore(2);
        o.getScore("\u00A7c").setScore(1);

        Team team = sb.registerNewTeam("team");
        team.addEntry("\u00A7c");

        GameTeam gameTeam = BedWars.getBedWars().getTeam(Biomia.getBiomiaPlayer(p));

        for (GameTeam t : BedWars.getBedWars().getTeams()) {
            sb.registerNewTeam(t.getTeamname()).setPrefix(t.getColorcode());
        }

        for (Player pl : Bukkit.getOnlinePlayers()) {
            sb.getTeam(BedWars.getBedWars().getTeam(Biomia.getBiomiaPlayer(pl)).getTeamname()).addEntry(pl.getName());
        }

        team.setPrefix(gameTeam.getColorcode() + gameTeam.getColor().translate());

        p.setScoreboard(sb);
    }

    public static void initSpectatorSB() {

        for (GameTeam t : BedWars.getBedWars().getTeams()) {
            spectatorSB.registerNewTeam(t.getTeamname()).setPrefix(t.getColorcode());
        }
        spectatorSB.registerNewTeam("spectator").setPrefix("\u00A77\u00A7o");

        for (Player pl : Bukkit.getOnlinePlayers()) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(pl);
            GameTeam team = BedWars.getBedWars().getTeam(bp);
            if (team != null && team.lives(bp)) {
                spectatorSB.getTeam(BedWars.getBedWars().getTeam(bp).getTeamname()).addEntry(pl.getName());
            } else {
                spectatorSB.getTeam("spectator").addEntry(pl.getName());
            }
        }
    }

    public static void setSpectatorSB(Player p) {
        p.setScoreboard(spectatorSB);
    }

}
