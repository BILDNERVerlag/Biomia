package de.biomia.commands.general;

import de.biomia.Biomia;
import de.biomia.Main;
import de.biomia.commands.BiomiaCommand;
import de.biomia.general.reportsystem.PlayerReport;
import de.biomia.messages.Messages;
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
                ((Player) sender).openInventory(Main.grund);
                new PlayerReport(Biomia.getBiomiaPlayer((Player) sender).getBiomiaPlayerID(), Biomia.getOfflineBiomiaPlayer(args[0]).getBiomiaPlayerID());

            } else {
                sender.sendMessage(Messages.NO_PLAYER);
            }
        } else {
            ((Player) sender).openInventory(Main.reportMenu);
        }

        return true;

    }

}
