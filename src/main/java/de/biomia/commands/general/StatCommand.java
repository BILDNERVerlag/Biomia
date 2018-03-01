package de.biomia.commands.general;

import de.biomia.Biomia;
import de.biomia.BiomiaPlayer;
import de.biomia.achievements.Stats;
import de.biomia.commands.BiomiaCommand;
import de.biomia.messages.Messages;
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
            Stats.BiomiaStat stat;
            String switchString = args[0].toLowerCase();
            int tempID;

            switch (switchString) {
            case "getcomments":
                try {
                    statString = args[1];
                    stat = Stats.BiomiaStat.valueOf(statString);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("\u00A77" + args[1] + " \u00A7cist kein erlaubter Stat.");
                    return true;
                }
                tempID = BiomiaPlayer.getBiomiaPlayerID(args[2]);
                if (tempID != -1) {
                    sender.sendMessage(Stats.getComments(stat, tempID).toString());
                } else {
                    sender.sendMessage("\u00A7cDer Spieler \u00A77" + args[2] + " \u00A7cist nicht in der Datenbank vermerkt.");
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
                    stat = Stats.BiomiaStat.valueOf(statString);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("\u00A77" + args[1] + " \u00A7cist kein erlaubter Stat.");
                    return true;
                }
                if (args.length == 2) {
                    Stats.incrementStat(stat, Biomia.getBiomiaPlayer((Player) sender).getBiomiaPlayerID());
                } else {
                    tempID = BiomiaPlayer.getBiomiaPlayerID(args[2]);
                    if (tempID != -1) {
                        Stats.incrementStat(stat, tempID);
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
                    stat = Stats.BiomiaStat.valueOf(statString);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("\u00A77" + args[1] + " \u00A7cist kein erlaubter Stat.");
                    return true;
                }
                if (args.length == 3) {
                    Stats.incrementStat(stat, Biomia.getBiomiaPlayer((Player) sender).getBiomiaPlayerID(), args[2]);
                } else {
                    tempID = BiomiaPlayer.getBiomiaPlayerID(args[3]);
                    if (tempID != -1) {
                        Stats.incrementStat(stat, tempID, args[2]);
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
                    stat = Stats.BiomiaStat.valueOf(statString);
                    value = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("\u00A77" + args[2] + " \u00A7cist keine erlaubte Zahl.");
                    return true;
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("\u00A77" + args[1] + " \u00A7cist kein erlaubter Stat.");
                    return true;
                }
                if (args.length == 3) {
                    Stats.incrementStatBy(stat, Biomia.getBiomiaPlayer((Player) sender).getBiomiaPlayerID(), value);
                } else {
                    tempID = BiomiaPlayer.getBiomiaPlayerID(args[3]);
                    if (tempID != -1) {
                        Stats.incrementStatBy(stat, tempID, value);
                    } else {
                        sender.sendMessage("\u00A7cDer Spieler \u00A77" + args[3] + " \u00A7cist nicht in der Datenbank vermerkt.");
                        return true;
                    }
                }
                break;
            case "get":
                try {
                    statString = args[1];
                    stat = Stats.BiomiaStat.valueOf(statString);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("\u00A77" + args[1] + " \u00A7cist kein erlaubter Stat.");
                    return true;
                }
                if (args.length == 2) {
                    value = Stats.getStat(stat, Biomia.getBiomiaPlayer((Player) sender).getBiomiaPlayerID());
                    sender.sendMessage("\u00A77" + statString + " (" + sender.getName() + "): \u00A7a" + value);
                } else {
                    value = Stats.getStat(stat, BiomiaPlayer.getBiomiaPlayerID(args[2]));
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
                    stat = Stats.BiomiaStat.valueOf(statString);
                    daytime_expr = args[2];
                    amount = Integer.parseInt(args[3]);
                } catch (Exception e) {
                    sendError(sender);
                    return true;
                }

                String dateString = amount + " " + daytime_expr.toLowerCase() + "s";

                if (args.length == 5) {
                    String playerName = args[4];
                    sender.sendMessage("\u00A77" + statString + " (" + playerName + ", " + dateString + "): \u00A7a" + Stats.getStatLastX(stat, BiomiaPlayer.getBiomiaPlayerID(playerName), daytime_expr, amount));
                } else
                    sender.sendMessage("\u00A77" + statString + " (" + sender.getName() + ", " + dateString + "): \u00A7a" + Stats.getStatLastX(stat, BiomiaPlayer.getBiomiaPlayerID(sender.getName()), daytime_expr, amount));
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