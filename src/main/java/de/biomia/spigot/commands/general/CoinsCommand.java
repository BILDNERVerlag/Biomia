package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.universal.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand extends BiomiaCommand {

    public CoinsCommand() {
        super("coins");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        BiomiaPlayer p = Biomia.getBiomiaPlayer((Player) sender);

        if (args.length == 0)
            sender.sendMessage(Messages.PREFIX + "\u00A7aDu besitzt \u00A7b" + p.getCoins() + " \u00A7aBC's!");

        if (p.getPlayer().hasPermission("biomia.coins")) {
            if (args.length >= 1) {
                String arg1 = args[0];
                if (args.length >= 2) {
                    OfflineBiomiaPlayer target = p;

                    if (args.length >= 3)
                        target = Biomia.getOfflineBiomiaPlayer(args[2]);

                    int coins;
                    try {
                        coins = Integer.valueOf(args[1]);
                    } catch (Exception e) {
                        sender.sendMessage("§cBitte gib eine §bZahl §cein§7!");
                        return true;
                    }

                    switch (arg1) {
                    case "take":
                        if (target.getCoins() < coins) {
                            sender.sendMessage(Messages.PREFIX + "\u00A7aDer Spieler " + target.getName() + " kann keinen negativen Betrag besitzen!");
                            return true;
                        } else {
                            target.takeCoins(coins);
                            sender.sendMessage(Messages.PREFIX + "\u00A7aDem Spieler" + target.getName() + " wurden \u00A7b" + coins + " \u00A7aBC's genommen!");
                        }
                        break;
                    case "set":
                        target.setCoins(coins);
                        break;
                    case "add":
                        target.addCoins(coins, false);
                        sender.sendMessage(Messages.PREFIX + "\u00A7a" + target.getName() + " wurden \u00A7b" + coins + " \u00A7aBC's hinzugf\u00fcgt!");
                        break;
                    case "get":
                        sender.sendMessage(Messages.PREFIX + "\u00A7aDer Spieler " + target.getName() + " besitzt \u00A7b" + target.getCoins() + " \u00A7aBC's!");
                    default:
                        return true;
                    }
                    sender.sendMessage(Messages.PREFIX + "\u00A7aDer Spieler " + target.getName() + " besitzt jetzt \u00A7b" + target.getCoins() + " \u00A7aBC's!");
                }
            }
        }
        return true;
    }
}