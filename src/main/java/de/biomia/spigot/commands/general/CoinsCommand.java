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
            sender.sendMessage(Messages.PREFIX + "\u00A77Du besitzt \u00A7b" + p.getCoins() + " \u00A77BC's!");

        if (p.getPlayer().hasPermission("biomia.coins")) {
            if (args.length >= 1) {
                String arg1 = args[0];
                if (args.length >= 2) {
                    OfflineBiomiaPlayer target = p;

                    if (args.length >= 3)
                        target = Biomia.getOfflineBiomiaPlayer(args[2]);

                    int coins = 0;
                    try {
                        coins = Integer.valueOf(args[1]);
                    } catch (NumberFormatException e) {
                        if (arg1.equals("get")) {
                            target = Biomia.getOfflineBiomiaPlayer(args[1]);
                        } else {
                            sender.sendMessage("\u00A7cBitte gib eine \u00A7bZahl \u00A7cein\u00A77!");
                            return true;
                        }
                    }

                    switch (arg1) {
                        case "take":
                            if (target.getCoins() < coins) {
                                sender.sendMessage(Messages.PREFIX + "\u00A7cDer Spieler " + target.getName() + " kann keinen negativen Betrag besitzen!");
                                return true;
                            } else {
                                target.takeCoins(coins);
                                sender.sendMessage(Messages.PREFIX + "\u00A77Dem Spieler" + target.getName() + " wurden \u00A7b" + coins + " \u00A77BC genommen!");
                            }
                            break;
                        case "set":
                            target.setCoins(coins);
                            break;
                        case "add":
                            target.addCoins(coins, false);
                            sender.sendMessage(Messages.PREFIX + "\u00A77" + target.getName() + " wurden \u00A7b" + coins + " \u00A77BC hinzugf\u00fcgt!");
                            break;
                        case "get":
                            break;
                        default:
                            return true;
                    }
                    sender.sendMessage(Messages.PREFIX + "\u00A77Der Spieler " + target.getName() + " besitzt jetzt \u00A7b" + target.getCoins() + " \u00A77BC's!");
                }
            }
        }
        return true;
    }
}