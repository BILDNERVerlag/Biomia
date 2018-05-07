package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand extends BiomiaCommand {

    public FlyCommand() {
        super("fly");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {

        if (!Biomia.getBiomiaPlayer((Player) sender).isSrStaff())
            sender.sendMessage(Messages.NO_PERM);

        Player p = (Player) sender;

        if (args.length > 0) {
            if ((p = Bukkit.getPlayer(args[0])) == null) {
                sender.sendMessage(Messages.NOT_ONLINE);
                return;
            }
        }
        p.setAllowFlight(!p.getAllowFlight());
        if (p.getAllowFlight())
            sender.sendMessage(String.format("%sFly Mode aktiviert.", Messages.COLOR_MAIN));
        else
            sender.sendMessage(String.format("%sFly Mode deaktiviert.", Messages.COLOR_MAIN));
    }

}
