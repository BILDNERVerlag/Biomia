package de.biomia.plugin.commands;

import de.biomia.api.main.Main;
import de.biomia.api.pex.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class RankCommand extends BiomiaCommand {

    public RankCommand() {
        super("rank");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (sender.hasPermission("biomia.setrank")) {
            if (args.length == 2) {
                if (Main.group.contains(args[1])) {
                    Player p = Bukkit.getPlayer(args[0]);
                    if (p != null) {
                        Rank.setRank(p, args[1]);
                        sender.sendMessage("\u00A7aDer Spieler " + args[0] + " ist nun " + args[1] + ".");
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
                        sender.sendMessage("\u00A7aDer Spieler " + args[0] + " ist nun " + args[1]);
                    }
                } else {
                    sender.sendMessage("\u00A7cEs sind nur diese R\u00fcnge verf\u00fcgbar:");

                    for (String s : Main.group) {
                        sender.sendMessage("\u00A7c" + s);
                    }
                }

            } else {
                sender.sendMessage("\u00A7c/rank <Spieler> <Rank>");
            }

        }

        return false;
    }

}
