package de.biomia.spigot.commands.general;

import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.tools.PlayerToServerConnector;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RandomServerGroupCommand extends BiomiaCommand {

    public RandomServerGroupCommand() {
        super("randomservergroup", "rsg");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {

        if (sender.hasPermission("biomia.*")) {

            if (args.length != 0) {
                if (args.length > 1) {
                    PlayerToServerConnector.connectToRandom(Bukkit.getPlayer(args[1]), BiomiaServerType.valueOf(args[0]));
                } else if (sender instanceof Player) {
                    PlayerToServerConnector.connectToRandom((Player) sender, BiomiaServerType.valueOf(args[0]));
                } else {
                    sender.sendMessage("Nutze /rsg <Gruppe> [Player]");
                }
            } else {
                sender.sendMessage("Nutze /rsg <Gruppe> [Player]");
            }
        } else {
            sender.sendMessage(Messages.NO_PERM);
        }
    }
}
