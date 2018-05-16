package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.general.reportsystem.ReportManager;
import de.biomia.universal.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SeeReportsCommand extends BiomiaCommand {

    public SeeReportsCommand() {
        super("seereports");
    }

    public void onCommand(CommandSender sender, String label, String[] args) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) sender);
        if (!(bp.isModerator() || bp.isSrStaff())) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }
        ReportManager.openScrollableInventory(bp);
    }

}
