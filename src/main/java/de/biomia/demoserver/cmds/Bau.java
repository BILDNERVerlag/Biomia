package de.biomia.demoserver.cmds;

import de.biomia.demoserver.config.Config;
import de.biomia.demoserver.main.WeltenlaborMain;
import de.biomia.api.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Bau implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {

        if (sender.hasPermission("biomia.createBau")) {

            if (args.length == 0) {
                sender.sendMessage("\u00A7cBitte nutze:");
                sender.sendMessage("");
                sender.sendMessage("\u00A7c/bau add \u00A77oder \u00A7c/bau remove");
                return true;
            }

            if (args[0].equalsIgnoreCase("add")) {

                if (args.length == 4) {

                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        int seite;
                        String name = args[2];
                        Material m;

                        try {
                            m = Material.valueOf(args[3]);
                        } catch (Exception e) {
                            p.sendMessage("\u00A7cBitte gib ein verf\u00fcgbares Material ein!");
                            return true;
                        }

                        try {
                            seite = Integer.valueOf(args[1]);
                        } catch (NumberFormatException e) {
                            p.sendMessage("\u00A7cBitte gib eine Zahl als Seite ein!");
                            return true;
                        }

                        Config.addObjekt(seite, name, p.getLocation(), m);

                    } else {
                        sender.sendMessage("\u00A7cDu musst ein Spieler sein!");
                    }
                } else
                    sender.sendMessage("\u00A7c/bau add <Seite> <Name> <Material>");
            }
            if (args[0].equalsIgnoreCase("remove")) {

                if (args.length == 2)
                    Config.removeObjekt(args[1]);
                else
                    sender.sendMessage("\u00A7c/bau remove <Name>");
            }
            if (args[0].equalsIgnoreCase("reload")) {
                Bukkit.broadcastMessage("\u00A7cReloading....");
                Main.getPlugin().reloadConfig();
                WeltenlaborMain.bauten.clear();
                WeltenlaborMain.si.clearAllItems();
                Config.hookInPlugin();
                Bukkit.broadcastMessage("\u00A7aReloaded!");
            }
        }
        return true;
    }

}
