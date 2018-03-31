package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.messages.manager.Scoreboards;
import de.biomia.spigot.server.lobby.LobbyScoreboard;
import de.biomia.universal.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand extends BiomiaCommand {

    public RankCommand() {
        super("rank");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (sender.hasPermission("biomia.setrank")) {
            if (args.length == 2) {
                try {
                    Ranks toSet = Ranks.valueOf(args[1]);
                    Player p = Bukkit.getPlayer(args[0]);
                    BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
                    bp.setRank(toSet);
                    sender.sendMessage("\u00A7aDer Spieler " + args[0] + " ist nun " + args[1] + ".");
                    if (Biomia.getServerInstance().getServerType() == BiomiaServerType.Lobby) {
                        LobbyScoreboard.sendScoreboard(p);
                    } else
                        Scoreboards.setTabList(p, true, true);
                } catch (IllegalArgumentException ignored) {
                    sender.sendMessage("\u00A7cEs sind nur diese R\u00fcnge verf\u00fcgbar:");
                    for (Ranks r : Ranks.values())
                        sender.sendMessage("\u00A7c" + r.name());
                }
            } else {
                sender.sendMessage("\u00A7c/rank <Spieler> <Rank>");
            }

        }
        return false;
    }

}
