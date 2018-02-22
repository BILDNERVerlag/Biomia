package de.biomia.plugin.commands;

import de.biomiaAPI.msg.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gamemode implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            if (sender.hasPermission("biomia.gamemode")) {
                Player p = (Player) sender;
                if (args.length >= 1) {

                    GameMode gameMode = null;

                    switch (args[0]) {
                        case "0":
                            gameMode = GameMode.SURVIVAL;
                            break;
                        case "1":
                            gameMode = GameMode.CREATIVE;
                            break;
                        case "2":
                            gameMode = GameMode.ADVENTURE;
                            break;
                        case "3":
                            gameMode = GameMode.SPECTATOR;
                            break;
                        case "s":
                            gameMode = GameMode.SURVIVAL;
                            break;
                        case "c":
                            gameMode = GameMode.CREATIVE;
                            break;
                        case "a":
                            gameMode = GameMode.ADVENTURE;
                            break;
                        case "sp":
                            gameMode = GameMode.SPECTATOR;
                            break;
                        case "survival":
                            gameMode = GameMode.SURVIVAL;
                            break;
                        case "creative":
                            gameMode = GameMode.CREATIVE;
                            break;
                        case "adventure":
                            gameMode = GameMode.ADVENTURE;
                            break;
                        case "spectator":
                            gameMode = GameMode.SPECTATOR;
                            break;
                        default:
                            break;
                    }

                    if (args.length >= 2) {
                        if (gameMode != null) {

                            Player target = Bukkit.getPlayer(args[1]);

                            target.setGameMode(gameMode);
                            target.sendMessage("§5Du bist nun im GameMode §2" + gameMode.name().toLowerCase());
                            p.sendMessage("§5Der Spieler§2 " + target.getName() + " §5ist nun im GameMode §2"
                                    + gameMode.name().toLowerCase());
                        }
                    } else {

                        if (gameMode != null) {
                            p.setGameMode(gameMode);
                            p.sendMessage("§5Du bist nun im GameMode §2" + gameMode.name().toLowerCase());
                        }
                    }
                } else {
                    p.sendMessage("/gm <GameMode> [Spieler]");
                }
            } else {
                sender.sendMessage(Messages.noperm);
            }
        }
        return true;
    }
}