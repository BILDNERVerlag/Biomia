package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand extends BiomiaCommand {

    public BuildCommand() {
        super("build");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {

        BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) sender);

        if (!bp.isSrStaff()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        if (args.length > 0) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p != null)
                bp = Biomia.getBiomiaPlayer(p);
            else {
                sender.sendMessage(Messages.NOT_ONLINE);
                return;
            }
        }
        if (!bp.canBuild()) {
            bp.setBuild(true);
            bp.sendMessage(Messages.format("Du kannst nun bauen!"));
            if (sender.equals(bp.getPlayer()))
                sender.sendMessage(Messages.format("Der Spieler %s kann nun bauen!", bp.getName()));
        } else {
            bp.setBuild(false);
            bp.sendMessage(Messages.format("Du kannst nun nicht mehr bauen!"));
            if (!sender.equals(bp.getPlayer()))
                sender.sendMessage(Messages.format("Der Spieler %s kann nun nicht mehr bauen!", bp.getName()));
        }
    }
}