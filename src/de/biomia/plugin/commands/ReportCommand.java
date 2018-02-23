package de.biomia.plugin.commands;

import de.biomia.plugin.reportsystem.PlayerReport;
import de.biomia.plugin.reportsystem.ReportManager;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.main.Main;
import de.biomiaAPI.msg.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("report")) {
            if (args.length >= 1) {
                if (sender instanceof Player) {

                    ((Player) sender).openInventory(Main.grund);
                    new PlayerReport(Biomia.getBiomiaPlayer((Player) sender).getBiomiaPlayerID(), BiomiaPlayer.getID(args[0]));

                } else {
                    sender.sendMessage(Messages.noPlayer);
                }
            } else {
                ((Player) sender).openInventory(Main.menu);
            }
        } else if (cmd.getName().equalsIgnoreCase("seereports")) {
            if (sender.hasPermission("biomia.reports.seereports") || sender.hasPermission("biomia.*")) {
                ReportManager.openScrolableInventory(Biomia.getBiomiaPlayer((Player) sender));
            }
        }
        return true;
    }

}