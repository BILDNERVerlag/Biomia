package de.biomia.commands.general;

import de.biomia.commands.BiomiaCommand;
import de.biomia.tools.Hologram;
import de.biomia.tools.ItemCreator;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HologramCommand extends BiomiaCommand {

    public HologramCommand() {
        super("hologram");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

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
                            sender.sendMessage("\u00A7c/hologram create <String>");
                            sender.sendMessage(
                                    "\u00A7cFalls du mehr Zeilen willst, nutze \u00A7a' % ' \u00A7cum die Zeile zu trennen");
                        }
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        p.getInventory().addItem(ItemCreator.itemCreate(Material.ARMOR_STAND, "\u00A7cHologram remover"));
                    }
                } else {
                    sender.sendMessage("\u00A7c/hologram create <String>");
                    sender.sendMessage("\u00A7c/hologram remove");
                }
            }
        }
        return true;
    }
}
