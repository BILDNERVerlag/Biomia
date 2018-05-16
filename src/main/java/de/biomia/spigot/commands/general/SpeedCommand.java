package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand extends BiomiaCommand {

    public SpeedCommand() {
        super("speed");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {

        Player p = (Player) sender;
        if (!Biomia.getBiomiaPlayer(p).isSrStaff()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        if (args.length >= 1 && args.length < 3) {
            float speed = 0.2f;
            try {
                speed = Float.valueOf(args[0]);
            } catch (NumberFormatException ignore) {
                // incorrect speed parameter, so default speed is used
            } finally {
                if (speed < -1 || speed > 1) {
                    if (speed < -1) {
                        speed = -1;
                    } else if (speed > 1) {
                        speed = 1;
                    }
                    sender.sendMessage("\u00A7b" + args[0] + "\u00A7cliegt nicht zwischen -1 und 1! \u00A7b" + speed + " \u00A7c wird verwendet.");
                }
            }
            if (args.length == 1) {
                p.setWalkSpeed(speed);
                p.setFlySpeed(speed);
                sender.sendMessage("\u00A7cDeine Geschwindigkeit wurde angepasst!");
            } else {
                p = Bukkit.getPlayer(args[1]);
                if (p != null) {
                    p.setWalkSpeed(speed);
                    p.setFlySpeed(speed);
                    sender.sendMessage("\u00A7cDie Geschwindigkeit von " + args[1] + " wurde angepasst!");
                    p.sendMessage("\u00A7cDeine Geschwindigkeit wurde von " + sender.getName() + " angepasst!");
                } else {
                    sender.sendMessage("\u00A7cDer Spieler " + args[1] + " ist nicht online!");
                }
            }
        } else {
            sender.sendMessage("\u00A7c/speed <0.1 - 0.9 (0.2 ist Standard)> [Spieler]");
        }
    }
}
