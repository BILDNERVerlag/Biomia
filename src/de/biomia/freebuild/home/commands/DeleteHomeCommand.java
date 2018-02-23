package de.biomia.freebuild.home.commands;

import de.biomia.freebuild.home.configuration.languages.LanguageManager;
import de.biomia.freebuild.home.homes.HomeManager;
import de.biomiaAPI.Biomia;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteHomeCommand implements CommandExecutor {
    private final HomeManager homeManager;

    public DeleteHomeCommand(HomeManager manager) {
        homeManager = manager;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if ((sender instanceof Player)) {
            Player player = (Player) sender;
            String homeName = "default";
            if ((strings.length == 1) && (sender.hasPermission("simplehomes.multihomes"))) {
                homeName = strings[0].toLowerCase();
            }
            homeManager.deleteHome(Biomia.getBiomiaPlayer(player).getBiomiaPlayerID(), homeName);
            player.sendMessage(LanguageManager.HOME_DELETED);
            return true;
        }
        sender.sendMessage(LanguageManager.PLAYER_COMMAND_ONLY);
        return true;
    }
}
