package de.biomia.commands.general;

import de.biomia.Main;
import de.biomia.commands.BiomiaCommand;
import de.biomia.tools.RankManager;
import org.bukkit.Bukkit;
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
                if (Main.RANK_NAMES_PREFIXES.keySet().contains(args[1])) {
                    Player p = Bukkit.getPlayer(args[0]);
                    if (p != null) {
                        RankManager.setRank(p, args[1]);
                        sender.sendMessage("\u00A7aDer Spieler " + args[0] + " ist nun " + args[1] + ".");
                        for (Player pl : Bukkit.getOnlinePlayers()) {
                            Scoreboard asb = pl.getScoreboard();
                            for (Team t : asb.getTeams()) {
                                if (t.getName().contains(RankManager.getRank(p))) {
                                    t.addEntry(p.getName());
                                    break;
                                }
                            }
                        }
                    } else {
                        RankManager.setRank(args[0], args[1]);
                        sender.sendMessage("\u00A7aDer Spieler " + args[0] + " ist nun " + args[1]);
                    }
                } else {
                    sender.sendMessage("\u00A7cEs sind nur diese R\u00fcnge verf\u00fcgbar:");

                    for (String s : Main.RANK_NAMES_PREFIXES.keySet()) {
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
