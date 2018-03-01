package de.biomia.server.freebuild.home.commands;

import de.biomia.Biomia;
import de.biomia.server.freebuild.home.Home;
import de.biomia.server.freebuild.home.homes.HomeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {
    private final HomeManager homeManager;

    public SetHomeCommand(HomeManager manager) {
        homeManager = manager;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if ((sender instanceof Player)) {
            Player player = (Player) sender;
            String homeName = "default";
            if ((args.length == 1) && (sender.hasPermission("simplehomes.multihomes"))) {
                homeName = args[0].toLowerCase();
            }
            if (homeManager == null) Bukkit.broadcastMessage("test");
            int bpID = Biomia.getBiomiaPlayer(player).getBiomiaPlayerID();
            assert homeManager != null;
            if ((homeManager.reachedMaxHomes(bpID))
                    && (!homeManager.getPlayerHomes(bpID).containsKey(homeName))) {
                player.sendMessage(Home.HOME_MAX_REACHED);
                return true;
            }
            homeManager.saveHome(player, homeName);
            player.sendMessage(Home.HOME_SET);
            return true;
        }
        sender.sendMessage(Home.PLAYER_COMMAND_ONLY);

        return false;
    }
}
