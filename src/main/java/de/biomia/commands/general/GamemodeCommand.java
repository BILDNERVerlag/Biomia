package de.biomia.commands.general;

import de.biomia.commands.BiomiaCommand;
import de.biomia.messages.BiomiaMessages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand extends BiomiaCommand {

    public GamemodeCommand() {
        super("gm");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

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
                            target.sendMessage("\u00A75Du bist nun im GameMode \u00A72" + gameMode.name().toLowerCase());
                            p.sendMessage("\u00A75Der Spieler\u00A72 " + target.getName() + " \u00A75ist nun im GameMode \u00A72" + gameMode.name().toLowerCase());
                        } else {
                            p.setGameMode(gameMode);
                            p.sendMessage("\u00A75Du bist nun im GameMode \u00A72" + gameMode.name().toLowerCase());
                        }
                    } else {
                        p.sendMessage("/gm <GameMode> [Spieler]");
                    }
                } else {
                    sender.sendMessage(BiomiaMessages.NO_PERM);
                }
            }
        }
        return true;
    }
}