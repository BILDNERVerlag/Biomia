package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.messages.manager.Scoreboards;
import de.biomia.spigot.server.lobby.LobbyScoreboard;
import de.biomia.universal.Messages;
import de.biomia.universal.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class RankCommand extends BiomiaCommand {

    public RankCommand() {
        super("rank");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {

        if (!Biomia.getBiomiaPlayer((Player) sender).isOwnerOrDev()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        if (args.length == 2) {
            try {
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null) {
                    sender.sendMessage("§aDer Spieler " + args[0] + " ist nicht (auf dem selben Server) online.");
                    return;
                }
                Ranks toSet = Ranks.valueOf(args[1]);
                BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
                bp.setRank(toSet);
                sender.sendMessage("§aDer Spieler " + args[0] + " ist nun " + args[1] + ".");
                if (Biomia.getServerInstance().getServerType() == BiomiaServerType.Lobby) {
                    LobbyScoreboard.sendScoreboard(p);
                } else
                    Scoreboards.setTabList(p, true, true);
            } catch (IllegalArgumentException ignored) {
                sender.sendMessage("§cEs sind nur diese Ränge verfügbar:");
                for (Ranks r : Ranks.values())
                    sender.sendMessage("§c" + r.name());
            }
        } else {
            sender.sendMessage("§c/rank <Spieler> <Rank>");
        }

    }
}
