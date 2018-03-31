package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.universal.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionCommand extends BiomiaCommand {

    public PermissionCommand() {
        super("permission");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) sender);

        if (sender.hasPermission("biomia.setpermission")) {
            if (args.length >= 3) {

                OfflineBiomiaPlayer user = Biomia.getOfflineBiomiaPlayer(args[1]);
                if (args[0].equalsIgnoreCase("add")) {
                    user.addPermission(args[2]);
                    sender.sendMessage(
                            "\u00A76Der Spieler \u00A7a" + args[1] + " \u00A76hat jetzt die Permission\u00A7a " + args[2] + "\u00A76!");
                } else if (args[0].equalsIgnoreCase("remove")) {
                    user.removePermission(args[2]);
                    sender.sendMessage(
                            "\u00A76Dem Spieler \u00A7a" + args[1] + " \u00A76wurde die Permission\u00A7a " + args[2] + " \u00A76entzogen!");
                } else {
                    sender.sendMessage("\u00A7c/permission <add|remove> <Spieler> <Permission>");
                }
            } else {
                sender.sendMessage("\u00A7c/permission <add|remove> <Spieler> <Permission>");
            }
        } else {
            sender.sendMessage(Messages.NO_PERM);

        }
        return true;
    }
}
