package de.biomia.spigot.commands.general;

import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.universal.Messages;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.User;
import org.bukkit.command.CommandSender;

public class PermissionCommand extends BiomiaCommand {

    public PermissionCommand() {
        super("permission");
    }

    private static void removePermission(String p, String permission) {
        User user = LuckPerms.getApi().getUser(p);
        if (user != null)
            user.setPermission(LuckPerms.getApi().buildNode(permission).setValue(false).build());
    }

    private static void addPermission(String p, String permission) {
        User user = LuckPerms.getApi().getUser(p);
        if (user != null)
            user.setPermission(LuckPerms.getApi().buildNode(permission).setValue(false).build());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("biomia.setpermission")) {
            if (args.length >= 3) {

                if (args[0].equalsIgnoreCase("add")) {
                    addPermission(args[1], args[2]);
                    sender.sendMessage(
                            "\u00A76Der Spieler \u00A7a" + args[1] + " \u00A76hat jetzt die Permission\u00A7a " + args[2] + "\u00A76!");
                } else if (args[0].equalsIgnoreCase("remove")) {
                    removePermission(args[1], args[2]);
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
