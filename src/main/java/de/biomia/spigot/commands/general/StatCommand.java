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
    public void onCommand(CommandSender sender, String label, String[] args) {

        if (!Biomia.getBiomiaPlayer((Player) sender).isSrStaff()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        if (args.length == 0) {
            sender.sendMessage("\u00A7c/stat <increment/inc> <statName> [increment] [Spieler]");
            sender.sendMessage("\u00A7c/stat <get> <statName> [Spieler]");
        } else if (args.length >= 2) {

            int value;
            BiomiaStat stat;
            String switchString = args[0].toLowerCase();
            OfflineBiomiaPlayer biomiaPlayer;

            switch (switchString) {
                case "getcomments":
                    try {
                        stat = BiomiaStat.valueOf(args[1]);
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage("\u00A77" + args[1] + " \u00A7cist kein erlaubter Stat.");
                        return;
                    }
                    if (args.length == 3)
                        biomiaPlayer = Biomia.getOfflineBiomiaPlayer(args[2]);
                    else
                        biomiaPlayer = Biomia.getBiomiaPlayer((Player) sender);
                    if (biomiaPlayer.getBiomiaPlayerID() != -1)
                        sender.sendMessage(stat.getComments(biomiaPlayer.getBiomiaPlayerID()).toString());
                    else {
                        sender.sendMessage("\u00A7cDer Spieler \u00A77" + biomiaPlayer.getName() + " \u00A7cist nicht in der Datenbank vermerkt.");
                        return;
                    }
                    break;
                case "increment":
                case "inc":

                    String playerName;

                    try {
                        stat = BiomiaStat.valueOf(args[1]);
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage("\u00A77" + args[1] + " \u00A7cist kein erlaubter Stat.");
                        return;
                    }
                    switch (args.length) {
                        case 2:
                            value = 1;
                            biomiaPlayer = Biomia.getBiomiaPlayer((Player) sender);
                            playerName = sender.getName();
                            break;
                        case 3:
                            try {
                                value = Integer.parseInt(args[1]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage("\u00A77" + args[2] + " \u00A7cist keine erlaubte Zahl.");
                                return;
                            }
                            biomiaPlayer = Biomia.getOfflineBiomiaPlayer(args[2]);
                            playerName = args[2];
                            break;
                        case 4:
                            try {
                                value = Integer.parseInt(args[2]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage("\u00A77" + args[2] + " \u00A7cist keine erlaubte Zahl.");
                                return;
                            }
                            biomiaPlayer = Biomia.getOfflineBiomiaPlayer(args[3]);
                            playerName = args[3];
                            break;
                        default:
                            sendError(sender);
                            return;
                    }
                    if (biomiaPlayer.getBiomiaPlayerID() != -1) {
                        stat.increment(biomiaPlayer.getBiomiaPlayerID(), value, null);
                    } else {
                        sender.sendMessage("\u00A7cDer Spieler \u00A77" + playerName + " \u00A7cist nicht in der Datenbank vermerkt.");
                        return;
                    }
                    break;
                case "get":
                    try {
                        stat = BiomiaStat.valueOf(args[1]);
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage("\u00A77" + args[1] + " \u00A7cist kein erlaubter Stat.");
                        return;
                    }
                    if (args.length == 2) {
                        value = stat.get(Biomia.getBiomiaPlayer((Player) sender).getBiomiaPlayerID(), null);
                        sender.sendMessage("\u00A77" + args[1] + " (" + sender.getName() + "): \u00A7a" + value);
                    } else {
                        biomiaPlayer = Biomia.getOfflineBiomiaPlayer(args[2]);
                        value = stat.get(biomiaPlayer.getBiomiaPlayerID(), null);
                        if (value != -1) {
                            sender.sendMessage("\u00A77" + args[1] + " (" + args[2] + "): \u00A7a" + value);
                        } else {
                            sender.sendMessage("\u00A7cDer Spieler \u00A77" + args[2] + " \u00A7cist nicht in der Datenbank vermerkt.");
                        }
                    }
                    return;
                case "getx":
                    int amount;
                    String daytime_expr;
                    try {
                        stat = BiomiaStat.valueOf(args[1]);
                        daytime_expr = args[2];
                        amount = Integer.parseInt(args[3]);
                    } catch (Exception e) {
                        sendError(sender);
                        return;
                    }

                    String dateString = amount + " " + daytime_expr.toLowerCase() + "s";

                    if (args.length == 5)
                        biomiaPlayer = Biomia.getOfflineBiomiaPlayer(args[4]);
                    else
                        biomiaPlayer = Biomia.getBiomiaPlayer((Player) sender);

                    sender.sendMessage("\u00A77" + args[1] + " (" + biomiaPlayer.getName() + ", " + dateString + "): \u00A7a" + stat.getLastX(biomiaPlayer.getBiomiaPlayerID(), daytime_expr, amount));
                    return;
                default:
                    sendError(sender);
                    return;
            }
            sender.sendMessage("\u00A7aCommand erfolgreich ausgeführt.");
        }
    }

    private void sendError(CommandSender sender) {
        sender.sendMessage("\u00A7cInkorrekte Command-Syntax. Benutze \u00A77/stat \u00A7cum die korrekte Syntax anzusehen.");
    }
}