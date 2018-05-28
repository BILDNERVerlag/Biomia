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
    public void onCommand(CommandSender sender, String label, String[] args) {

        BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) sender);

        if (args.length == 0)
            sender.sendMessage(String.format("%s%sDu besitzt %s%d%s BC's!", Messages.PREFIX, Messages.COLOR_MAIN, Messages.COLOR_SUB, bp.getCoins(), Messages.COLOR_MAIN));

        if (!bp.isSrStaff()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        if (args.length >= 1) {
            String arg1 = args[0].toLowerCase();
            if (args.length >= 2) {
                OfflineBiomiaPlayer target = bp;

                if (args.length >= 3)
                    target = Biomia.getOfflineBiomiaPlayer(args[2]);

                int coins = 0;
                try {
                    coins = Integer.valueOf(args[1]);
                } catch (NumberFormatException e) {
                    if (arg1.equals("get")) {
                        target = Biomia.getOfflineBiomiaPlayer(args[1]);
                    } else {
                        sender.sendMessage(String.format("%sBitte gib eine Zahl ein!", Messages.COLOR_MAIN));
                        return;
                    }
                }

                switch (arg1) {
                    case "take":
                        if (target.getCoins() < coins) {
                            sender.sendMessage(String.format("%s%sDer Spieler %s%s%s kann keinen negativen Betrag besitzen!", Messages.PREFIX, Messages.COLOR_MAIN, Messages.COLOR_SUB, target.getName(), Messages.COLOR_MAIN));
                            return;
                        } else {
                            target.takeCoins(coins);
                            sender.sendMessage(String.format("%s%sDem Spieler %s%s%s wurden %s%d%s BC genomen!", Messages.PREFIX, Messages.COLOR_MAIN, Messages.COLOR_SUB, target.getName(), Messages.COLOR_MAIN, Messages.COLOR_SUB, coins, Messages.COLOR_MAIN));
                        }
                        break;
                    case "set":
                        target.setCoins(coins);
                        break;
                    case "add":
                        target.addCoins(coins, false);
                        sender.sendMessage(String.format("%s%sDem Spieler %s%s%s wurden %s%d%s BC hinzugefügt!", Messages.PREFIX, Messages.COLOR_MAIN, Messages.COLOR_SUB, target.getName(), Messages.COLOR_MAIN, Messages.COLOR_SUB, coins, Messages.COLOR_MAIN));
                        break;
                    case "get":
                        break;
                    default:
                        return;
                }
                sender.sendMessage(String.format("%s%sDer Spieler %s%s%s besitzt jetzt %s%d%s BC", Messages.PREFIX, Messages.COLOR_MAIN, Messages.COLOR_SUB, target.getName(), Messages.COLOR_MAIN, Messages.COLOR_SUB, coins, Messages.COLOR_MAIN));
            }
        }
    }
}