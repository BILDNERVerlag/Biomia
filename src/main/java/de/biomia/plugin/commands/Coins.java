package de.biomia.plugin.commands;

import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.msg.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Coins implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            BiomiaPlayer p = Biomia.getBiomiaPlayer((Player) sender);
            if (args.length == 0) {
                sender.sendMessage(Messages.PREFIX + "00A7aDu besitzt 00A7b" + p.getCoins() + " 00A7aBC's!");
            }

            if (p.getPlayer().hasPermission("biomia.coins")) {
                if (args.length >= 1) {
                    if (args[0].equalsIgnoreCase("take")) {
                        if (args.length == 3) {
                            if (Bukkit.getPlayer(args[1]) != null) {

                                BiomiaPlayer target = Biomia.getBiomiaPlayer(Bukkit.getPlayer(args[1]));
                                int coins = Integer.valueOf(args[2]);

                                if (target.getCoins() < coins) {
                                    sender.sendMessage(Messages.PREFIX + "00A7aDer Spieler " + args[1]
                                            + " kann keinen negativen Betrag besitzen!");
                                } else {
                                    target.takeCoins(coins);
                                    sender.sendMessage(Messages.PREFIX + "00A7aDem Spieler" + target.getPlayer().getName() + " wurden 00A7b"
                                            + coins + " 00A7aBC's genommen!");
                                    sender.sendMessage(Messages.PREFIX + "00A7a" + target.getPlayer().getName()
                                            + " besitzt jetzt 00A7b" + target.getCoins() + " 00A7aBC's!");
                                }
                            } else {
                                sender.sendMessage(Messages.NOT_ONLINE);
                            }
                        } else {
                            sender.sendMessage(Messages.PREFIX + "00A7c/coins take <Spieler> <Menge>");
                        }
                    }
                    if (args[0].equalsIgnoreCase("set")) {
                        if (args.length == 3) {
                            if (Bukkit.getPlayer(args[1]) != null) {

                                BiomiaPlayer target = Biomia.getBiomiaPlayer(Bukkit.getPlayer(args[1]));
                                int coins = Integer.valueOf(args[2]);

                                target.setCoins(coins);

                                sender.sendMessage(Messages.PREFIX + "00A7aDer Spieler " + args[1] + " besitzt jetzt 00A7b"
                                        + args[2] + " 00A7aBC's!");
                            } else {
                                sender.sendMessage(Messages.NOT_ONLINE);
                            }
                        } else {
                            sender.sendMessage(Messages.PREFIX + "00A7c/coins set <Spieler> <Menge>");
                        }
                    }
                    if (args[0].equalsIgnoreCase("add")) {
                        if (args.length == 3) {
                            if (Bukkit.getPlayer(args[1]) != null) {

                                BiomiaPlayer target = Biomia.getBiomiaPlayer(Bukkit.getPlayer(args[1]));
                                int coins = Integer.valueOf(args[2]);

                                target.addCoins(coins, false);

                                sender.sendMessage(Messages.PREFIX + "00A7a" + target.getPlayer().getName() + " wurden 00A7b"
                                        + coins + " 00A7aBC's hinzugf\u00fcgt!");
                                sender.sendMessage(Messages.PREFIX + "00A7a" + target.getPlayer().getName()
                                        + " besitzt jetzt 00A7b" + target.getCoins() + " 00A7aBC's!");
                            } else {
                                sender.sendMessage(Messages.NOT_ONLINE);
                            }
                        } else {
                            sender.sendMessage(Messages.PREFIX + "00A7c/coins add <Spieler> <Menge>");
                        }
                    }
                    if (args[0].equalsIgnoreCase("get")) {
                        if (args.length == 2) {
                            if (Bukkit.getPlayer(args[1]) != null) {

                                BiomiaPlayer target = Biomia.getBiomiaPlayer(Bukkit.getPlayer(args[1]));

                                sender.sendMessage(Messages.PREFIX + "00A7aDer Spieler " + target.getPlayer().getName()
                                        + " besitzt 00A7b" + target.getCoins() + " 00A7aBC's!");
                            } else {
                                sender.sendMessage(Messages.NOT_ONLINE);
                            }
                        } else {
                            sender.sendMessage(Messages.PREFIX + "00A7c/coins get <Spieler>");
                        }
                    }
                }
            }
        }
        return true;
    }
}
