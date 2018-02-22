package de.biomia.plugin.commands;

import de.biomiaAPI.itemcreator.ItemCreator;
import de.biomiaAPI.tools.Hologram;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HologramCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            if (sender.hasPermission("biomia.holograms")) {
                Player p = (Player) sender;
                if (args.length >= 1) {
                    if (args[0].equalsIgnoreCase("create")) {
                        StringBuilder string;
                        if (args.length >= 2) {
                            string = new StringBuilder(args[1]);
                            for (int i = 2; i < args.length; i++) {
                                string.append(" ").append(args[i]);
                            }
                            String[] out = string.toString().split(" % ");
                            Hologram.newHologram(p, out);
                        } else {
                            sender.sendMessage("§c/hologram create <String>");
                            sender.sendMessage(
                                    "§cFalls du mehr Zeilen willst, nutze §a' % ' §cum die Zeile zu trennen");
                        }
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        p.getInventory().addItem(ItemCreator.itemCreate(Material.ARMOR_STAND, "§cHologram remover"));
                    }
                } else {
                    sender.sendMessage("§c/hologram create <String>");
                    sender.sendMessage("§c/hologram remove");
                }
            }
        }
        return true;
    }
}
