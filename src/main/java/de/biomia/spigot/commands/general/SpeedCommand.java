package de.biomia.spigot.commands.general;

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
    public boolean execute(CommandSender sender, String label, String[] args) {
        // fly command, first argument is flight speed on a scale from 1-10, second
        // (optional) argument is another player

        if (!sender.hasPermission("biomia.speed")) {
            sender.sendMessage(Messages.NO_PERM);
            return true;
        }

        Player p = (Player) sender;

        if (args.length >= 1 && args.length < 3) {
            float speed = 2;
            try {
                speed = Float.valueOf(args[0]);
            } catch (NumberFormatException ignore) {
                // incorrect speed parameter, so default speed is used
            } finally {
                if (speed == 1)
                    speed = 2;
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
            sender.sendMessage("\u00A7c/speed <1-10> [Spieler]");
        }

        return false;
    }

    // returns a corresponding speed to a value from 0-10 (1 is default speed)
    private float getSpeed(int i) {
        return i / 10;
    }
}
