package de.biomia.plugin.commands;

import de.biomia.api.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {

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
                            target.sendMessage("\u00A75Du bist nun im GameMode \u00A72" + gameMode.name().toLowerCase());
                            p.sendMessage("\u00A75Der Spieler\u00A72 " + target.getName() + " \u00A75ist nun im GameMode \u00A72"
                                    + gameMode.name().toLowerCase());
                        }
                    } else {

                        if (gameMode != null) {
                            p.setGameMode(gameMode);
                            p.sendMessage("\u00A75Du bist nun im GameMode \u00A72" + gameMode.name().toLowerCase());
                        }
                    }
                } else {
                    p.sendMessage("/gm <GameMode> [Spieler]");
                }
            } else {
                sender.sendMessage(Messages.NO_PERM);
            }
        }
        return true;
    }
}