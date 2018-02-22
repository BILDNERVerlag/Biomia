package de.biomia.lobby.commands;

import de.biomiaAPI.msg.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class LobbyComands implements CommandExecutor {

    public static final ArrayList<Player> targetarmorstands = new ArrayList<>();
    public static boolean pvp = false;
    private static boolean flyall = false;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("lobbysettings")) {
            if (sender.hasPermission("biomia.lobbysettings")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("destroyarmorstands") && (sender instanceof Player)) {
                        Player p = (Player) sender;
                        if (!targetarmorstands.contains(p)) {
                            targetarmorstands.add(p);
                            p.sendMessage(Messages.prefix + "§aDu kannst nun ArmorStands auf der Lobby abbauen!");
                            return true;
                        } else {
                            targetarmorstands.remove(p);
                            p.sendMessage(
                                    Messages.prefix + "§cDu kannst nun keine ArmorStands mehr auf der Lobby abbauen!");
                            return true;
                        }

                    }
                    if (args[0].equalsIgnoreCase("fly")) {
                        flyall = !flyall;
                        for (Player pl : Bukkit.getOnlinePlayers()) {
                            pl.setAllowFlight(flyall);
                            pl.setFlying(flyall);
                        }
                        if (flyall) {
                            sender.sendMessage(Messages.prefix + "§aEs können nun alle auf der Lobby fliegen!");
                        } else {
                            sender.sendMessage(
                                    Messages.prefix + "§cEs kann nun keiner mehr auf der Lobby fliegen!");
                        }
                    }
                }

            } else {
                sender.sendMessage(Messages.noperm);
            }
        }
        return false;
    }

}
