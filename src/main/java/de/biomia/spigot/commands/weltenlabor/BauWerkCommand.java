package de.biomia.spigot.commands.weltenlabor;

import de.biomia.spigot.Main;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.configs.DemoConfig;
import de.biomia.spigot.server.demoserver.Weltenlabor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BauWerkCommand extends BiomiaCommand {

    public BauWerkCommand() {
        super("bauwerk", "building");
    }

    @Override
    public boolean execute(CommandSender sender, String arg2, String[] args) {

        if (sender.hasPermission("biomia.createBauwerk")) {

            if (args.length == 0) {
                sender.sendMessage("\u00A7cBitte nutze:");
                sender.sendMessage("");
                sender.sendMessage("\u00A7c/bauwerk add \u00A77oder \u00A7c/bau remove");
                return true;
            }

            switch (args[0].toLowerCase()) {
            case "add":
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

                        DemoConfig.addObjekt(seite, name, p.getLocation(), m);

                    } else {
                        sender.sendMessage("\u00A7cDu musst ein Spieler sein!");
                    }
                } else
                    sender.sendMessage("\u00A7c/bauwerk add <Seite> <Name> <Material>");
                break;
            case "remove":
                if (args.length == 2)
                    DemoConfig.removeObjekt(args[1]);
                else
                    sender.sendMessage("\u00A7c/bauwerk remove <Name>");
                break;
            case "reload":
                Bukkit.broadcastMessage("\u00A7cReloading....");
                Main.getPlugin().reloadConfig();
                Weltenlabor.bauten.clear();
                Weltenlabor.si.clear();
                DemoConfig.hookInPlugin();
                Bukkit.broadcastMessage("\u00A7aReloaded!");
                break;
            }
        }
        return true;
    }

}
