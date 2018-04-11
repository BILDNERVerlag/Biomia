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
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (args.length >= 1) {
            if (sender instanceof Player) {
                ((Player) sender).openInventory(ReportManager.grund);
                OfflineBiomiaPlayer bp = Biomia.getOfflineBiomiaPlayer(args[0]);
                if (bp.getBiomiaPlayerID() == -1)
                    sender.sendMessage("00A7cDieser Spieler war noch niw Online!");
                else
                    new PlayerReport(Biomia.getBiomiaPlayer((Player) sender), bp);
            } else {
                sender.sendMessage(Messages.NO_PLAYER);
            }
        } else {
            ReportManager.openReportMenu((Player) sender);
        }

        return true;

    }

}
