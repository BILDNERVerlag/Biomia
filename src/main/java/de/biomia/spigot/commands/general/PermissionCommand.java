package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.universal.Messages;
import org.bukkit.command.CommandSender;

public class PermissionCommand extends BiomiaCommand {

    public PermissionCommand() {
        super("permission");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("biomia.setpermission")) {
            if (args.length >= 3) {
                OfflineBiomiaPlayer user = Biomia.getOfflineBiomiaPlayer(args[1]);
                if (args[0].equalsIgnoreCase("add")) {
                    user.addPermission(args[2]);
                    sender.sendMessage(
                            String.format("%sDer Spieler %s%s%s hat jetzt die Permission %s%s%s!", Messages.COLOR_MAIN, Messages.COLOR_SUB, args[1], Messages.COLOR_MAIN, Messages.COLOR_SUB, args[2], Messages.COLOR_AUX));
                } else if (args[0].equalsIgnoreCase("remove")) {
                    user.removePermission(args[2]);
                    sender.sendMessage(
                            String.format("%sDem Spieler %s%s%s wurde die Permission %s%s%s entfernt%s!", Messages.COLOR_MAIN, Messages.COLOR_SUB, args[1], Messages.COLOR_MAIN, Messages.COLOR_SUB, args[2], Messages.COLOR_MAIN, Messages.COLOR_AUX));
                } else
                    sender.sendMessage("\u00A7c/permission <add|remove> <Spieler> <Permission>");
            } else
                sender.sendMessage("\u00A7c/permission <add|remove> <Spieler> <Permission>");

        } else
            sender.sendMessage(Messages.NO_PERM);
    }
}
