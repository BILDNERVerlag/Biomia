package de.biomia.commands.lobby;

import de.biomia.tools.PlayerToServerConnector;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {
            if (args[0].equals("change")) {
                if (sender instanceof Player) {
                    PlayerToServerConnector.connect((Player) sender, "Maxi");
                }
            }
        }

        return true;
    }

}
