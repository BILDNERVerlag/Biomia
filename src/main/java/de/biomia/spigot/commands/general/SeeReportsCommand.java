package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.general.reportsystem.ReportManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SeeReportsCommand extends BiomiaCommand {

    public SeeReportsCommand() {
        super("seereports");
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("biomia.reports.seereports") || sender.hasPermission("biomia.*")) {
            ReportManager.openScrollableInventory(Biomia.getBiomiaPlayer((Player) sender));
        }
        return true;
    }

}
