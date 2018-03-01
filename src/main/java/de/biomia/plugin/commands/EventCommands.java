package de.biomia.plugin.commands;

import de.biomia.api.BiomiaPlayer;
import de.biomia.api.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            // WINTER

//			if (cmd.getName().equalsIgnoreCase("calendar")) {
//				if (args.length >= 1) {
//
//					Entity entity = p.getNearbyEntities(1, 5, 1).get(0);
//					if (entity == null) {
//						sender.sendMessage("Stell dich in die N\u00fche eines Entities");
//						return true;
//					}
//
//					switch (args[0].toLowerCase()) {
//					case "add":
//						if (args.length == 2) {
//							WinterTag.bindCalendarDayToEntity(Integer.valueOf(args[1]), entity.getUniqueId());
//							sender.sendMessage("Entity mit der uuid " + entity.getUniqueId().toString()
//									+ " wurde hinzugef\u00fcgt zu Tag " + args[1] + "!");
//						} else
//							sender.sendMessage("/calendar add <Tag>");
//
//						break;
//					case "remove":
//						if (args.length == 2) {
//							BiomiaPluginMain.plugin.getConfig().set("Calendar." + args[1], null);
//							BiomiaPluginMain.plugin.saveConfig();
//							sender.sendMessage("Entities f\u00fcr den Tag " + args[1] + " wurden gel\u00F6scht!");
//						} else
//							sender.sendMessage("/calendar remove <Tag>");
//
//						break;
//					default:
//						break;
//					}
//				} else {
//					sender.sendMessage("/calendar (add | remove)");
//				}
//
//			}

            // Easter

            if (cmd.getName().equals("givereward") && p.hasPermission("biomia.events.givereward")) {
                if (args.length >= 1) {
                    Main.getEvent().giveReward(BiomiaPlayer.getBiomiaPlayerID(args[0]));
                }
            }
            if (cmd.getName().equals("addeggs") && p.hasPermission("biomia.events.addeggs")) {
                if (args.length >= 2) {
                    Main.getEvent().addEggs(BiomiaPlayer.getBiomiaPlayerID(args[0]), Integer.valueOf(args[1]));
                }
            }
        }
        return true;

    }
}
