package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.general.reportsystem.PlayerReport;
import de.biomia.spigot.general.reportsystem.ReportManager;
import de.biomia.universal.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand extends BiomiaCommand {

    public ReportCommand() {
        super("report");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {

        if (args.length >= 1) {
            if (sender instanceof Player) {
                ((Player) sender).openInventory(ReportManager.grund);
                OfflineBiomiaPlayer bp = Biomia.getOfflineBiomiaPlayer(args[0]);
                if (bp.getBiomiaPlayerID() == -1)
                    sender.sendMessage(String.format("%sDieser Spieler war noch nie Online!", Messages.COLOR_MAIN));
                else
                    new PlayerReport(Biomia.getBiomiaPlayer((Player) sender), bp);
            } else {
                sender.sendMessage(Messages.NO_PLAYER);
            }
        } else {
            ReportManager.openReportMenu((Player) sender);
        }
    }
}
