package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.spigot.achievements.BiomiaStat;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.universal.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatCommand extends BiomiaCommand {

    public StatCommand() {
        super("stat");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (!sender.hasPermission("biomia.stats")) {
            sender.sendMessage(Messages.NO_PERM);
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("\u00A7c/stat <save> <statName> <value> [Spieler]");
            sender.sendMessage("\u00A7c/stat <increment/inc> <statName> [Spieler]");
            sender.sendMessage("\u00A7c/stat <incrementBy/incby> <statName> <increment> [Spieler]");
            sender.sendMessage("\u00A7c/stat <get> <statName> [Spieler]");
        } else if (args.length >= 2) {

            String statString;
            int value;
            BiomiaStat stat;
            String switchString = args[0].toLowerCase();
            OfflineBiomiaPlayer biomiaPlayer;

            switch (switchString) {
                case "getcomments":
                    try {
                        statString = args[1];
                        stat = BiomiaStat.valueOf(statString);
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage("\u00A77" + args[1] + " \u00A7cist kein erlaubter Stat.");
                        return true;
                    }
                    if (args.length == 3)
                        biomiaPlayer = Biomia.getOfflineBiomiaPlayer(args[2]);
                    else
                        biomiaPlayer = Biomia.getBiomiaPlayer((Player) sender);
                    if (biomiaPlayer.getBiomiaPlayerID() != -1)
                        sender.sendMessage(stat.getComments(biomiaPlayer.getBiomiaPlayerID()).toString());
                    else {
                        sender.sendMessage("\u00A7cDer Spieler \u00A77" + biomiaPlayer.getName() + " \u00A7cist nicht in der Datenbank vermerkt.");
                        return true;
                    }
                    break;
                case "save":
                    sender.sendMessage("No longer exists.");
                    break;
                case "increment":
                case "inc":
                    try {
                        statString = args[1];
                        stat = BiomiaStat.valueOf(statString);
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage("\u00A77" + args[1] + " \u00A7cist kein erlaubter Stat.");
                        return true;
                    }
                    if (args.length == 2) {
                        stat.increment(Biomia.getBiomiaPlayer((Player) sender).getBiomiaPlayerID(), 1, null);
                    } else {
                        biomiaPlayer = Biomia.getOfflineBiomiaPlayer(args[2]);
                        if (biomiaPlayer.getBiomiaPlayerID() != -1) {
                            stat.increment(biomiaPlayer.getBiomiaPlayerID(), 1, null);
                        } else {
                            sender.sendMessage("\u00A7cDer Spieler \u00A77" + args[2] + " \u00A7cist nicht in der Datenbank vermerkt.");
                            return true;
                        }
                    }
                    break;
                case "incrementx":
                case "incx":
                    try {
                        statString = args[1];
                        stat = BiomiaStat.valueOf(statString);
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage("\u00A77" + args[1] + " \u00A7cist kein erlaubter Stat.");
                        return true;
                    }
                    if (args.length == 3) {
                        stat.increment(Biomia.getBiomiaPlayer((Player) sender).getBiomiaPlayerID(), 1, args[2]);
                    } else {
                        biomiaPlayer = Biomia.getOfflineBiomiaPlayer(args[2]);
                        if (biomiaPlayer.getBiomiaPlayerID() != -1) {
                            stat.increment(biomiaPlayer.getBiomiaPlayerID(), 1, args[2]);
                        } else {
                            sender.sendMessage("\u00A7cDer Spieler \u00A77" + args[3] + " \u00A7cist nicht in der Datenbank vermerkt.");
                            return true;
                        }
                    }
                    break;
                case "incrementby":
                case "incby":
                    try {
                        statString = args[1];
                        stat = BiomiaStat.valueOf(statString);
                        value = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage("\u00A77" + args[2] + " \u00A7cist keine erlaubte Zahl.");
                        return true;
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage("\u00A77" + args[1] + " \u00A7cist kein erlaubter Stat.");
                        return true;
                    }
                    if (args.length == 3) {
                        stat.increment(Biomia.getBiomiaPlayer((Player) sender).getBiomiaPlayerID(), value, null);
                    } else {
                        biomiaPlayer = Biomia.getOfflineBiomiaPlayer(args[2]);
                        if (biomiaPlayer.getBiomiaPlayerID() != -1) {
                            stat.increment(biomiaPlayer.getBiomiaPlayerID(), value, null);
                        } else {
                            sender.sendMessage("\u00A7cDer Spieler \u00A77" + args[3] + " \u00A7cist nicht in der Datenbank vermerkt.");
                            return true;
                        }
                    }
                    break;
                case "get":
                    try {
                        statString = args[1];
                        stat = BiomiaStat.valueOf(statString);
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage("\u00A77" + args[1] + " \u00A7cist kein erlaubter Stat.");
                        return true;
                    }
                    if (args.length == 2) {
                        value = stat.get(Biomia.getBiomiaPlayer((Player) sender).getBiomiaPlayerID(), null);
                        sender.sendMessage("\u00A77" + statString + " (" + sender.getName() + "): \u00A7a" + value);
                    } else {
                        biomiaPlayer = Biomia.getOfflineBiomiaPlayer(args[2]);
                        value = stat.get(biomiaPlayer.getBiomiaPlayerID(), null);
                        if (value != -1) {
                            sender.sendMessage("\u00A77" + statString + " (" + args[2] + "): \u00A7a" + value);
                        } else {
                            sender.sendMessage("\u00A7cDer Spieler \u00A77" + args[2] + " \u00A7cist nicht in der Datenbank vermerkt.");
                        }
                    }
                    return true;
                case "getx":
                    int amount;
                    String daytime_expr;
                    try {
                        statString = args[1];
                        stat = BiomiaStat.valueOf(statString);
                        daytime_expr = args[2];
                        amount = Integer.parseInt(args[3]);
                    } catch (Exception e) {
                        sendError(sender);
                        return true;
                    }

                    String dateString = amount + " " + daytime_expr.toLowerCase() + "s";

                    if (args.length == 5)
                        biomiaPlayer = Biomia.getOfflineBiomiaPlayer(args[4]);
                    else
                        biomiaPlayer = Biomia.getBiomiaPlayer((Player) sender);

                    sender.sendMessage("\u00A77" + statString + " (" + biomiaPlayer.getName() + ", " + dateString + "): \u00A7a" + stat.getLastX(biomiaPlayer.getBiomiaPlayerID(), daytime_expr, amount));
                    return true;
                default:
                    sendError(sender);
                    return true;
            }
            sender.sendMessage("\u00A7aCommand erfolgreich ausgef\u00fchrt.");
        }
        return false;
    }

    private void sendError(CommandSender sender) {
        sender.sendMessage("\u00A7cInkorrekte Command-Syntax. Benutze \u00A77/stat \u00A7cum die korrekte Syntax anzusehen.");
    }
}