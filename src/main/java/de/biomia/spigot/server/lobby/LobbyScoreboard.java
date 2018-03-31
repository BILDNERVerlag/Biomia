package de.biomia.spigot.server.lobby;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.messages.manager.Scoreboards;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class LobbyScoreboard {

    public static void sendScoreboard(Player p) {
        Team freunde, rank, coins;

        Scoreboard sb = Scoreboards.setTabList(p, false, true);

        Objective o = sb.registerNewObjective("aaa", "bbb");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName("\u00A7cBIO\u00A7bMIA");
        o.getScore(" ").setScore(10);
        o.getScore("\u00A7cCoins:").setScore(9);
        o.getScore("\u00A7c").setScore(8);
        o.getScore("\u00A7a").setScore(7);
        o.getScore("\u00A7cFreunde:").setScore(6);
        o.getScore("\u00A7f").setScore(5);
        o.getScore("\u00A71").setScore(4);
        o.getScore("\u00A7cRank:").setScore(3);
        o.getScore("\u00A7r").setScore(2);
        o.getScore("\u00A7l").setScore(1);

        coins = sb.registerNewTeam("coins");
        freunde = sb.registerNewTeam("freunde");
        rank = sb.registerNewTeam("rank");

        rank.addEntry("\u00A7r");
        coins.addEntry("\u00A7c");
        freunde.addEntry("\u00A7f");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!p.getScoreboard().equals(sb))
                    cancel();
                else
                reloadSB(p, sb);
            }
        }.runTaskTimer(Main.getPlugin(), 0, 100);

        p.setScoreboard(sb);
    }

    private static void reloadSB(Player p, Scoreboard sb) {

        Team freunde, rank, coins;

        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

        coins = sb.getTeam("coins");
        freunde = sb.getTeam("freunde");
        rank = sb.getTeam("rank");

        rank.setPrefix("\u00A7b" + getGroupName(p));
        coins.setPrefix("\u00A7b" + bp.getCoins());
        freunde.setPrefix("\u00A7b" + bp.getOnlineFriends().size() + " \u00A77/ \u00A7b" + bp.getFriends().size());
    }
    private static String getGroupName(Player p) {
        String rankName = Biomia.getBiomiaPlayer(p).getRank().getName();
        rankName = rankName.replaceAll("premium", "");
        rankName = rankName.replaceAll("spieler", "");
        switch (rankName) {
            case "eins":
                rankName = "Premium I";
                break;
            case "zwei":
                rankName = "Premium II";
                break;
            case "drei":
                rankName = "Premium III";
                break;
            case "vier":
                rankName = "Premium IV";
                break;
            case "fünf":
                rankName = "Premium V";
                break;
            case "sechs":
                rankName = "Premium VI";
                break;
            case "sieben":
                rankName = "Premium VII";
                break;
            case "acht":
                rankName = "Premium VIII";
                break;
            case "neun":
                rankName = "Premium IX";
                break;
            case "zehn":
                rankName = "Premium X";
                break;
            case "reg":
                rankName = "Spieler";
                break;
            case "unreg":
                rankName = "Unregistriert";
                break;
            case "owner":
                rankName = "Owner";
                break;
            case "supporter":
                rankName = "Supporter";
                break;
            case "jrbuilder":
                rankName = "JrBuilder";
                break;
            case "builder":
                rankName = "Builder";
                break;
            case "srbuilder":
                rankName = "SrBuilder";
                break;
            case "moderator":
                rankName = "Moderator";
                break;
            case "srmoderator":
                rankName = "SrMod";
                break;
            case "developer":
                rankName = "Dev";
                break;
            case "admin":
                rankName = "Admin";
                break;
        }
        return rankName;
    }
}
