package de.biomia.plugin.commands;

import de.biomia.api.msg.Messages;
import de.biomia.api.mysql.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CreateTableCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmc, String label, String[] args) {
        if (!sender.hasPermission("biomia.sql")) {
            sender.sendMessage(Messages.noperm);
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("\u00A7cInkorrekte Argument-Anzahl. Die korrekte Syntax lautet:");
            sender.sendMessage("\u00A7c/ct <stat/achiev> <TABLE_NAME>");
            return false;
        }

        MySQL.Databases db;
        if (args[0].startsWith("s")) {
            db = MySQL.Databases.stats_db;
        } else {
            db = MySQL.Databases.achiev_db;
        }


        switch (args[0]) {
            case "stat":
            case "stats":
            case "s":
                MySQL.execute("CREATE TABLE IF NOT EXISTS `stats_db`.`" + args[1] + "` ( " +
                        "`key` INT NOT NULL AUTO_INCREMENT, " +
                        "`ID` INT NOT NULL , " +
                        "`value` INT NOT NULL, " +
                        "`inc` INT NOT NULL, " +
                        "`timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                        "PRIMARY KEY (`key`) );", MySQL.Databases.stats_db);
                break;
            case "sx":
            case "statex":
            case "statsextended":
                MySQL.execute("CREATE TABLE IF NOT EXISTS `stats_db`.`" + args[1] + "` ( " +
                        "`key` INT NOT NULL AUTO_INCREMENT, " +
                        "`ID` INT NOT NULL , " +
                        "`value` INT NOT NULL, " +
                        "`comment` VARCHAR(36) NOT NULL, " +
                        "`timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                        "PRIMARY KEY (`key`) );", MySQL.Databases.stats_db);
                break;
            case "achiev":
            case "achievement":
            case "achievements":
            case "a":
                MySQL.execute("CREATE TABLE IF NOT EXISTS `achiev_db`.`" + args[1] + "` ( `ID` INT NOT NULL , `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`ID`) );", MySQL.Databases.achiev_db);
                break;
            default:
                sender.sendMessage("\u00A77" + args[0] + "\u00A7c ist ungültig. Benutze \u00A77stat \u00A7coder \u00A77achiev\u00A7c.");
                return true;
        }

        sender.sendMessage("\u00A7cTabelle " + args[1] + " wurde erfolgreich in der Datenbank " + db.toString() + " gespeichert.");
        return true;
    }

    private void newTableCreate(String[] args) {
        MySQL.execute("CREATE TABLE IF NOT EXISTS `stats_db`.`" + args[1] + "` ( " +
                "`key` INT NOT NULL AUTO_INCREMENT, " +
                "`ID` INT NOT NULL , " +
                "`value` INT NOT NULL, " +
                "`timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "PRIMARY KEY (`key`) );", MySQL.Databases.stats_db);
    }

}
