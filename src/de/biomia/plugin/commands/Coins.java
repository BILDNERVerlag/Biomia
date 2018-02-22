package de.biomia.plugin.commands;

import de.biomiaAPI.Biomia;
import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.msg.Messages;
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
                sender.sendMessage(Messages.prefix + "§aDu besitzt §b" + p.getCoins() + " §aBC's!");
            }

            if (p.getPlayer().hasPermission("biomia.coins")) {
                if (args.length >= 1) {
                    if (args[0].equalsIgnoreCase("take")) {
                        if (args.length == 3) {
                            if (Bukkit.getPlayer(args[1]) != null) {

                                BiomiaPlayer target = Biomia.getBiomiaPlayer(Bukkit.getPlayer(args[1]));
                                int coins = Integer.valueOf(args[2]);

                                if (target.getCoins() < coins) {
                                    sender.sendMessage(Messages.prefix + "§aDer Spieler " + args[1]
                                            + " kann keinen negativen Betrag besitzen!");
                                } else {
                                    target.takeCoins(coins);
                                    sender.sendMessage(Messages.prefix + "§aDem Spieler" + target.getPlayer().getName() + " wurden §b"
                                            + coins + " §aBC's genommen!");
                                    sender.sendMessage(Messages.prefix + "§a" + target.getPlayer().getName()
                                            + " besitzt jetzt §b" + target.getCoins() + " §aBC's!");
                                }
                            } else {
                                sender.sendMessage(Messages.notonline);
                            }
                        } else {
                            sender.sendMessage(Messages.prefix + "§c/coins take <Spieler> <Menge>");
                        }
                    }
                    if (args[0].equalsIgnoreCase("set")) {
                        if (args.length == 3) {
                            if (Bukkit.getPlayer(args[1]) != null) {

                                BiomiaPlayer target = Biomia.getBiomiaPlayer(Bukkit.getPlayer(args[1]));
                                int coins = Integer.valueOf(args[2]);

                                target.setCoins(coins);

                                sender.sendMessage(Messages.prefix + "§aDer Spieler " + args[1] + " besitzt jetzt §b"
                                        + args[2] + " §aBC's!");
                            } else {
                                sender.sendMessage(Messages.notonline);
                            }
                        } else {
                            sender.sendMessage(Messages.prefix + "§c/coins set <Spieler> <Menge>");
                        }
                    }
                    if (args[0].equalsIgnoreCase("add")) {
                        if (args.length == 3) {
                            if (Bukkit.getPlayer(args[1]) != null) {

                                BiomiaPlayer target = Biomia.getBiomiaPlayer(Bukkit.getPlayer(args[1]));
                                int coins = Integer.valueOf(args[2]);

                                target.addCoins(coins, false);

                                sender.sendMessage(Messages.prefix + "§a" + target.getPlayer().getName() + " wurden §b"
                                        + coins + " §aBC's hinzugfügt!");
                                sender.sendMessage(Messages.prefix + "§a" + target.getPlayer().getName()
                                        + " besitzt jetzt §b" + target.getCoins() + " §aBC's!");
                            } else {
                                sender.sendMessage(Messages.notonline);
                            }
                        } else {
                            sender.sendMessage(Messages.prefix + "§c/coins add <Spieler> <Menge>");
                        }
                    }
                    if (args[0].equalsIgnoreCase("get")) {
                        if (args.length == 2) {
                            if (Bukkit.getPlayer(args[1]) != null) {

                                BiomiaPlayer target = Biomia.getBiomiaPlayer(Bukkit.getPlayer(args[1]));

                                sender.sendMessage(Messages.prefix + "§aDer Spieler " + target.getPlayer().getName()
                                        + " besitzt §b" + target.getCoins() + " §aBC's!");
                            } else {
                                sender.sendMessage(Messages.notonline);
                            }
                        } else {
                            sender.sendMessage(Messages.prefix + "§c/coins get <Spieler>");
                        }
                    }
                }
            }
        }
        return true;
    }
}
