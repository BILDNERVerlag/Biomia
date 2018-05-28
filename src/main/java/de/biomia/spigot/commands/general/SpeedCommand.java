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
                    sender.sendMessage("§b" + args[0] + "§cliegt nicht zwischen -1 und 1! §b" + speed + " §c wird verwendet.");
                }
            }
            if (args.length == 1) {
                p.setWalkSpeed(speed);
                p.setFlySpeed(speed);
                sender.sendMessage("§cDeine Geschwindigkeit wurde angepasst!");
            } else {
                p = Bukkit.getPlayer(args[1]);
                if (p != null) {
                    p.setWalkSpeed(speed);
                    p.setFlySpeed(speed);
                    sender.sendMessage("§cDie Geschwindigkeit von " + args[1] + " wurde angepasst!");
                    p.sendMessage("§cDeine Geschwindigkeit wurde von " + sender.getName() + " angepasst!");
                } else {
                    sender.sendMessage("§cDer Spieler " + args[1] + " ist nicht online!");
                }
            }
        } else {
            sender.sendMessage("§c/speed <0.1 - 0.9 (0.2 ist Standard)> [Spieler]");
        }
    }
}
