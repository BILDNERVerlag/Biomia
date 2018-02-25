package de.biomia.lobby.scoreboard;

import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.main.Main;
import de.biomia.api.pex.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardClass {

    private static void addToEach(Player p) {
        for (Player pl : Bukkit.getOnlinePlayers()) {

            Scoreboard asb = pl.getScoreboard();

            for (Team t : asb.getTeams()) {
                if (t.getName().contains(Rank.getRank(p))) {
                    t.addEntry(p.getName());
                    break;
                }
            }
        }
    }

    public static void sendScoreboard(Player p) {
        Team freunde, rank, coins;
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();

        initScoreboard(sb);

        for (Player pl : Bukkit.getOnlinePlayers()) {
            for (Team t : sb.getTeams()) {
                if (t.getName().contains(Rank.getRank(pl))) {
                    t.addEntry(pl.getName());
                    break;
                }
            }
        }

        addToEach(p);

        Objective o = sb.registerNewObjective("aaa", "bbb");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName("§5Bio§2mia");
        o.getScore(" ").setScore(10);
        o.getScore("§5Coins:").setScore(9);
        o.getScore("§c").setScore(8);
        o.getScore("§a").setScore(7);
        o.getScore("§5Freunde:").setScore(6);
        o.getScore("§f").setScore(5);
        o.getScore("§1").setScore(4);
        o.getScore("§5Rank:").setScore(3);
        o.getScore("§r").setScore(2);
        o.getScore("§l").setScore(1);

        coins = sb.registerNewTeam("coins");
        freunde = sb.registerNewTeam("freunde");
        rank = sb.registerNewTeam("rank");

        rank.addEntry("§r");
        coins.addEntry("§c");
        freunde.addEntry("§f");
        p.setScoreboard(sb);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (p.isOnline())
                    reloadSB(p, sb);
                else
                    cancel();
            }
        }.runTaskTimer(Main.getPlugin(), 0, 100);
    }

    private static void reloadSB(Player p, Scoreboard sb) {

        Team freunde, rank, coins;

        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

        coins = sb.getTeam("coins");
        freunde = sb.getTeam("freunde");
        rank = sb.getTeam("rank");

        rank.setPrefix("§e" + ChatColors.getGroupName(p));
        if (rank.getPrefix().contains("Registriert")) {
            rank.setPrefix("§eNicht ");
            rank.setSuffix("§eRegistriert!");
        } else {
            rank.setSuffix("");
        }
        coins.setPrefix("§b" + bp.getCoins());
        freunde.setPrefix("§6" + bp.getOnlineFriends().size() + " §7/ §c" + bp.getFriends().size());
    }

    private static void initScoreboard(Scoreboard sb) {
        int i = 0;

        for (String s : de.biomia.api.main.Main.group) {

            Team t;

            if (i < 10)
                t = sb.registerNewTeam("0" + i + s);
            else
                t = sb.registerNewTeam(i + s);

            t.setPrefix(Rank.getPrefix(s));
            i++;
        }

    }
}
