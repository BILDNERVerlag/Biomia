package de.biomia.plugin.commands;

import de.biomia.api.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
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

        Player p = null;
        Player toSet;

        if (sender instanceof Player) {
            p = (Player) sender;
        }

        if (args.length >= 1 && args.length < 3) {
            float speed;
            try {
                speed = getSpeed(Integer.parseInt(args[0]));
            } catch (NumberFormatException e) {
                // incorrect speed parameter, so default speed is used
                speed = 0.2f;
            }

            if (args.length == 1) {
                if (p != null) {
                    p.setWalkSpeed(speed);
                    p.setFlySpeed(speed);
                    sender.sendMessage("\u00A7cDeine Geschwindigkeit wurde angepasst!");
                } else {
                    sender.sendMessage("\u00A7cDu musst ein Spieler sein, um diesen Command auszuf\u00fchren!");
                }
            } else {
                toSet = Bukkit.getPlayer(args[1]);

                if (toSet != null) {
                    toSet.setWalkSpeed(speed);
                    toSet.setFlySpeed(speed);
                    sender.sendMessage("\u00A7cGeschwindigkeit von " + args[1] + " wurde angepasst!");
                    toSet.sendMessage("\u00A7cDeine Geschwindigkeit wurde von " + sender.getName() + " angepasst!");
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
        switch (i) {
            case 10:
                return 1.0f;
            case 9:
                return 0.999f;
            case 8:
                return 0.9f;
            case 7:
                return 0.8f;
            case 6:
                return 0.7f;
            case 5:
                return 0.6f;
            case 4:
                return 0.5f;
            case 3:
                return 0.4f;
            case 2:
                return 0.3f;
            case 1:
            default:
                return 0.2f;
            case 0:
                return 0;
        }
    }

}
