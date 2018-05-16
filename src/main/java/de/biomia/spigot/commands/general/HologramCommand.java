package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.tools.Hologram;
import de.biomia.universal.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HologramCommand extends BiomiaCommand {

    public HologramCommand() {
        super("hologram");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {

        Player p = (Player) sender;
        if (!Biomia.getBiomiaPlayer(p).isSrStaff()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("create")) {
                StringBuilder string;
                if (args.length >= 2) {
                    string = new StringBuilder(args[1]);
                    for (int i = 2; i < args.length; i++) {
                        string.append(" ").append(args[i]);
                    }
                    String[] out = string.toString().split(" % ");
                    Hologram.newHologram(p.getLocation(), out);
                } else {
                    sender.sendMessage("\u00A7c/hologram create <String>");
                    sender.sendMessage(
                            "\u00A7cFalls du mehr Zeilen willst, nutze \u00A7a' % ' \u00A7cum die Zeile zu trennen");
                }
            }
        } else
            sender.sendMessage("\u00A7c/hologram create <String>");
    }
}
