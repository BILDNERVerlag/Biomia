package de.biomia.versus.sw.commands;

import de.biomia.versus.global.configs.SkyWarsConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class SW implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("biomia.skywars.setup")) {
                if (args.length >= 1) {
                    switch (args[0].toLowerCase()) {
                        case "addloc":
                            if (args.length >= 3) {
                                int mapID = Integer.valueOf(args[1]);
                                int teamID = Integer.valueOf(args[2]);
                                SkyWarsConfig.addLocation(p.getLocation(), mapID, teamID);
                                sender.sendMessage("Spawnpoint wurde hinzugef\u00fcgt!");
                            } else
                                sender.sendMessage("/sw addloc mapID teamID");
                            break;
                        case "addchest":
                            if (args.length >= 3) {
                                Location l = p.getTargetBlock((Set<Material>)null, 100).getLocation();
                                if (l.getBlock().getType() == Material.CHEST) {
                                    int mapID = Integer.valueOf(args[1]);
                                    switch (args[2].toLowerCase()) {
                                        case "good":
                                            SkyWarsConfig.addGoodChestLocation(l, mapID);
                                            sender.sendMessage("Bessere Kiste hinzugef\u00fcgt!");
                                            break;
                                        case "normal":
                                            SkyWarsConfig.addNormalChestLocation(l, mapID);
                                            sender.sendMessage("Normale Kiste hinzugef\u00fcgt!");
                                            break;
                                        default:
                                            sender.sendMessage("/sw addchest mapID <normal/good>");
                                            break;
                                    }
                                } else {
                                    sender.sendMessage("00A7cSchau auf eine Kiste!");
                                }
                            } else {
                                sender.sendMessage("/sw addchest mapID <normal/good>");
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    sender.sendMessage("00A7c/sw addloc (F\u00fcgt einen Spawnpunkt hinzu)");
                    sender.sendMessage("00A7c/sw addchest (F\u00fcgt eine Kiste hinzu)");
                }
            }
        }
        return true;
    }
}
