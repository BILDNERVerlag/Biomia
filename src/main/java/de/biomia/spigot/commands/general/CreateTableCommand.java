package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.achievements.BiomiaStat;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.universal.Messages;
import de.biomia.universal.MySQL;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateTableCommand extends BiomiaCommand {

    public CreateTableCommand() {
        super("ct", "createtable");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {
        if (!Biomia.getBiomiaPlayer((Player) sender).isSrStaff()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        if (args.length > 0 && (args.length == 2 || args[0].equalsIgnoreCase("all"))) {
            switch (args[0]) {
                case "all":
                    for (BiomiaStat allStats : BiomiaStat.values())
                        createStatTable(allStats.name(), sender);
                    sender.sendMessage(String.format("%sAlle Tabellen erfolgreich erstellt!", Messages.COLOR_MAIN));
                    break;
                case "s":
                case "stat":
                case "statistic":
                    createStatTable(args[1], sender);
                    break;
                case "achiev":
                case "achievement":
                case "achievements":
                case "a":
                    MySQL.execute(String.format("CREATE TABLE IF NOT EXISTS %s (`ID` INT NOT NULL , `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`ID`))", args[1]), MySQL.Databases.achiev_db);
                    sender.sendMessage(String.format("00A7cTabelle %s wurde erfolgreich in der Datenbank %s gespeichert.", args[1], MySQL.Databases.achiev_db.name()));
                    break;
                default:
                    sender.sendMessage(String.format("00A7cDas Argument 00A77%s00A7c ist ungültig. Benutze 00A77stat 00A7c/ 00A77statex00A7coder 00A77achiev00A7c.", args[0]));
            }
        } else {
            sender.sendMessage(String.format("%sInkorrekte Argument-Anzahl. Die korrekte Syntax lautet:", Messages.COLOR_MAIN));
            sender.sendMessage(String.format("%s/ct <stat/achiev/all> <TABLE_NAME>", Messages.COLOR_MAIN));
        }
    }

    private static void createStatTable(String name, CommandSender sender) {
        MySQL.execute(String.format("CREATE TABLE IF NOT EXISTS %s (`key` INT NOT NULL AUTO_INCREMENT, `biomiaID` INT NOT NULL , `value` INT NOT NULL, `inc` INT NOT NULL, `comment` VARCHAR(36), `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`key`))", name), MySQL.Databases.stats_db);
        sender.sendMessage(String.format("00A7cTabelle %s wurde erfolgreich in der Datenbank %s gespeichert.", name, MySQL.Databases.stats_db.name()));
    }
}
