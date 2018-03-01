package de.biomia.commands.lobby;

import de.biomia.commands.BiomiaCommand;
import de.biomia.messages.Messages;
import de.biomia.tools.PlayerToServerConnector;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RandomServerGroupCommand extends BiomiaCommand {

    public RandomServerGroupCommand() {
        super("randomservergroup", "rsg");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (sender.hasPermission("biomia.*")) {

            if (args.length != 0) {
                if (args.length > 1) {
                    PlayerToServerConnector.connectToRandom(Bukkit.getPlayer(args[1]), args[0]);
                } else if (sender instanceof Player) {
                    PlayerToServerConnector.connectToRandom((Player) sender, args[0]);
                } else {
                    sender.sendMessage("Nutze /randomServerGroup <Gruppe> [Player]");
                }
            } else {
                sender.sendMessage("Nutze /randomServerGroup <Gruppe> [Player]");
            }
        } else {
            sender.sendMessage(Messages.NO_PERM);
        }
        return true;
    }
}
