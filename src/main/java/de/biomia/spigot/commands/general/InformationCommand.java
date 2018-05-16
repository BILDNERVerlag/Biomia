package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.general.reportsystem.InformationInventory;
import de.biomia.universal.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InformationCommand extends BiomiaCommand {

    public InformationCommand() {
        super("information", "baninfo", "playerinfo", "info", "reportinfo");
    }

    @Override
    protected void onCommand(CommandSender sender, String label, String[] args) {

        BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) sender);
        if (!bp.isSrStaff()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        if (args.length == 0) {
            bp.sendMessage(String.format("/%s <Player>", label));
        }
        OfflineBiomiaPlayer target = Biomia.getOfflineBiomiaPlayer(args[0]);
        if (target == null) {
            bp.sendMessage(String.format("%s%s war noch nie Online!", Messages.COLOR_MAIN, args[0]));
            return;
        }
        new InformationInventory(bp, target.getBiomiaPlayerID());
    }
}
