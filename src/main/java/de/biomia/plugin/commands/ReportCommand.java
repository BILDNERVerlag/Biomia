package de.biomia.plugin.commands;

import de.biomia.plugin.reportsystem.PlayerReport;
import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.Main;
import de.biomia.api.messages.Messages;
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
                new PlayerReport(Biomia.getBiomiaPlayer((Player) sender).getBiomiaPlayerID(), BiomiaPlayer.getBiomiaPlayerID(args[0]));

            } else {
                sender.sendMessage(Messages.NO_PLAYER);
            }
        } else {
            ((Player) sender).openInventory(Main.menu);
        }
        
        return true;

    }

}
