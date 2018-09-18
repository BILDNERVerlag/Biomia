package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
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

        Player p = (Player) sender;
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        if (!bp.isSrStaff() && !bp.isModerator()) {
            sender.sendMessage(Messages.NO_PERM);
        } else {
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
                        target.sendMessage("§b>>§7Du bist nun im GameMode §b" + gameMode.name().toLowerCase());
                        p.sendMessage("§b>>§7Der Spieler§c " + target.getName() + " §7ist nun im GameMode §b" + gameMode.name().toLowerCase());
                    } else {
                        p.setGameMode(gameMode);
                        p.sendMessage("§b>>§7Du bist nun im GameMode §b" + gameMode.name().toLowerCase());
                    }
                } else {
                    p.sendMessage("§7/§bgm §7<§bGameMode§7> [§bSpieler§7]");
                }
            }
        }
    }
}