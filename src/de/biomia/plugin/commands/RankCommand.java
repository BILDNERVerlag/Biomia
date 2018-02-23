package de.biomia.plugin.commands;

import de.biomiaAPI.main.Main;
import de.biomiaAPI.pex.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class RankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("rank")) {
            if (sender.hasPermission("biomia.setrank")) {
                if (args.length == 2) {
                    if (Main.group.contains(args[1])) {
                        Player p = Bukkit.getPlayer(args[0]);
                        if (p != null) {
                            Rank.setRank(p, args[1]);
                            sender.sendMessage("�aDer Spieler " + args[0] + " ist nun " + args[1] + ".");
                            for (Player pl : Bukkit.getOnlinePlayers()) {
                                Scoreboard asb = pl.getScoreboard();
                                for (Team t : asb.getTeams()) {
                                    if (t.getName().contains(Rank.getRank(p))) {
                                        t.addEntry(p.getName());
                                        break;
                                    }
                                }
                            }
                        } else {
                            Rank.setRank(args[0], args[1]);
                            sender.sendMessage("�aDer Spieler " + args[0] + " ist nun " + args[1]);
                        }
                    } else {
                        sender.sendMessage("�cEs sind nur diese R�nge verf�gbar:");

                        for (String s : Main.group) {
                            sender.sendMessage("�c" + s);
                        }
                    }

                } else {
                    sender.sendMessage("�c/rank <Spieler> <Rank>");
                }

            }
        }

        return false;
    }

}