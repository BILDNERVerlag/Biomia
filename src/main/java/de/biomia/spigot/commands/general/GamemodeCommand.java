package de.biomia.spigot.commands.general;

import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand extends BiomiaCommand {

    public GamemodeCommand() {
        super("gm");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {

        if (sender instanceof Player) {
            if (sender.hasPermission("biomia.gamemode")) {
                Player p = (Player) sender;
                if (args.length >= 1) {

                    GameMode gameMode = null;

                    switch (args[0]) {
                        case "0":
                        case "s":
                        case "survival":
                            gameMode = GameMode.SURVIVAL;
                            break;
                        case "1":
                        case "c":
                        case "creative":
                            gameMode = GameMode.CREATIVE;
                            break;
                        case "2":
                        case "a":
                        case "adventure":
                            gameMode = GameMode.ADVENTURE;
                            break;
                        case "3":
                        case "sp":
                        case "spectator":
                            gameMode = GameMode.SPECTATOR;
                            break;
                        default:
                            break;
                    }
                    if (gameMode != null) {
                        if (args.length >= 2) {
                            Player target = Bukkit.getPlayer(args[1]);
                            target.setGameMode(gameMode);
                            target.sendMessage("\u00A7b>>\u00A77Du bist nun im GameMode \u00A7b" + gameMode.name().toLowerCase());
                            p.sendMessage("\u00A7b>>\u00A77Der Spieler\u00A7c " + target.getName() + " \u00A77ist nun im GameMode \u00A7b" + gameMode.name().toLowerCase());
                        } else {
                            p.setGameMode(gameMode);
                            p.sendMessage("\u00A7b>>\u00A77Du bist nun im GameMode \u00A7b" + gameMode.name().toLowerCase());
                        }
                    } else {
                        p.sendMessage("\u00A77/\u00A7bgm \u00A77<\u00A7bGameMode\u00A77> [\u00A7bSpieler\u00A77]");
                    }
                } else {
                    sender.sendMessage(Messages.NO_PERM);
                }
            }
        }
    }
}