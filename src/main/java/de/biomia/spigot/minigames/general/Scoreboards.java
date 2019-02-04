package de.biomia.spigot.minigames.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.messages.KitPVPMessages;
import de.biomia.spigot.messages.ParrotMessages;
import de.biomia.spigot.messages.SkyWarsMessages;
import de.biomia.spigot.minigames.*;
import de.biomia.spigot.minigames.general.kits.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Scoreboards {

    public static final Scoreboard lobbySB = Bukkit.getScoreboardManager().getNewScoreboard();
    private static Scoreboard spectatorSB;

    public static void initLobbySB(GameMode mode) {

        Objective o = lobbySB.registerNewObjective("ccc", "ddd");
        setDisplayName(mode.getInstance().getType(), o);
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.getScore(" ").setScore(7);
        o.getScore("§cMap:").setScore(6);
        o.getScore("§c").setScore(5);
        o.getScore("§a").setScore(4);
        o.getScore("§cTeams:").setScore(3);
        o.getScore("§f").setScore(2);
        o.getScore("§1").setScore(1);

        Team map = lobbySB.registerNewTeam("map");
        Team teams = lobbySB.registerNewTeam("teams");

        map.addEntry("§c");
        teams.addEntry("§f");

        map.setPrefix("§b" + mode.getInstance().getMapDisplayName());
        teams.setPrefix("§b" + mode.getInstance().getTeamAmount() + " §7x " + "§b" + mode.getInstance().getTeamSize());

        for (GameTeam t : mode.getTeams()) {
            lobbySB.registerNewTeam(t.getColor().name()).setPrefix(t.getColorcode());
        }
        lobbySB.registerNewTeam("xnoteam").setPrefix("§7");
    }

    public static void initSpectatorSB(GameMode mode) {
        if (spectatorSB == null) {
            spectatorSB = Bukkit.getScoreboardManager().getNewScoreboard();
            for (GameTeam t : mode.getTeams())
                spectatorSB.registerNewTeam(t.getColor().name()).setPrefix(t.getColorcode());
            spectatorSB.registerNewTeam("spectator").setPrefix("§7§o");
        }
        for (Player pl : Bukkit.getOnlinePlayers()) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(pl);
            GameTeam team = bp.getTeam();
            if (team != null && team.lives(bp))
                spectatorSB.getTeam(bp.getTeam().getColor().name()).addEntry(pl.getName());
            else spectatorSB.getTeam("spectator").addEntry(pl.getName());
        }
    }

    public static void setLobbyScoreboard(Player p) {
        p.setScoreboard(lobbySB);
    }

    // InGame
    public static void setInGameScoreboard(Player p, GameInstance instance) {
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective o = sb.registerNewObjective("aaa", "bbb");
        setDisplayName(instance.getType(), o);
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        GameTeam gameTeam = Biomia.getBiomiaPlayer(p).getTeam();
        Team team;
        switch (instance.getType()) {
            default:
            case BED_WARS:
            case BED_WARS_VS:
                o.getScore(" ").setScore(3);
                o.getScore("§cTeam:").setScore(2);
                o.getScore("§c").setScore(1);

                team = sb.registerNewTeam("team");
                team.addEntry("§c");
                team.setPrefix(gameTeam.getColorcode() + gameTeam.getColor().translate());
                break;
            case SKY_WARS:
            case SKY_WARS_VS:
                o.getScore(" ").setScore(7);
                o.getScore("§cKit:").setScore(6);
                o.getScore("§c").setScore(5);
                o.getScore("§a").setScore(4);
                o.getScore("§cTeam:").setScore(3);
                o.getScore("§f").setScore(2);
                o.getScore("§1").setScore(1);

                Team kit = sb.registerNewTeam("kitname");
                team = sb.registerNewTeam("team");

                kit.addEntry("§c");
                team.addEntry("§f");

                kit.setPrefix("§b" + KitManager.getManager(Biomia.getBiomiaPlayer(p)).getSelectedKit().getName());
                team.setPrefix(gameTeam.getColorcode() + gameTeam.getColor().translate());
                break;
        }
        for (TeamColor color : TeamColor.values()) {
            sb.registerNewTeam(color.name()).setPrefix(color.getColorcode());
        }
        for (BiomiaPlayer bp : instance.getPlayers()) {
            sb.getTeam(bp.getTeam().getColor().name()).addEntry(bp.getName());
        }
        p.setScoreboard(sb);
    }

    public static void setSpectatorSB(Player p) {
        Scoreboards.spectatorSB.getTeam("spectator").addEntry(p.getName());
        p.setScoreboard(spectatorSB);
    }

    private static void setDisplayName(GameType type, Objective o) {
        switch (type) {
            default:
            case BED_WARS:
            case BED_WARS_VS:
                o.setDisplayName(BedWarsMessages.bedwars);
                break;
            case SKY_WARS:
            case SKY_WARS_VS:
                o.setDisplayName(SkyWarsMessages.skywars);
                break;
            case KIT_PVP_VS:
                o.setDisplayName(KitPVPMessages.kitpvp);
            case PARROT:
                o.setDisplayName(ParrotMessages.parrot);
                break;
        }
    }

}
