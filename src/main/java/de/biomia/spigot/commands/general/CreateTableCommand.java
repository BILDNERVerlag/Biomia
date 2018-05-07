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
        if (sender instanceof Player && !Biomia.getBiomiaPlayer((Player) sender).isSrStaff()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(String.format("%sInkorrekte Argument-Anzahl. Die korrekte Syntax lautet:", Messages.COLOR_MAIN));
            sender.sendMessage(String.format("%s/ct <stat/achiev> <TABLE_NAME>", Messages.COLOR_MAIN));
            return;
        }

        MySQL.Databases db;
        if (args[0].startsWith("s")) {
            db = MySQL.Databases.stats_db;
        } else {
            db = MySQL.Databases.achiev_db;
        }


        switch (args[0]) {
            case "all":
                for (BiomiaStat allStats : BiomiaStat.values())
                    createStatTable(allStats.name());
                sender.sendMessage(String.format("%sAlle Tabellen erfolgreich erstellt!", Messages.COLOR_MAIN));
                break;
            case "s":
            case "stat":
            case "statistic":
                createStatTable(args[1]);
                break;
            case "achiev":
            case "achievement":
            case "achievements":
            case "a":
                MySQL.execute("CREATE TABLE IF NOT EXISTS " + args[1] + " ( `ID` INT NOT NULL , `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`ID`) );", MySQL.Databases.achiev_db);
                break;
            default:
                sender.sendMessage("\u00A7cDas Argument \u00A77" + args[0] + "\u00A7c ist ung\u00fcltig. Benutze \u00A77stat \u00A7c/ \u00A77statex\u00A7coder \u00A77achiev\u00A7c.");
                return;
        }

        sender.sendMessage("\u00A7cTabelle " + args[1] + " wurde erfolgreich in der Datenbank " + db.toString() + " gespeichert.");
    }

    private static void createStatTable(String name) {
        MySQL.execute("CREATE TABLE IF NOT EXISTS " + name + " ( " +
                "`key` INT NOT NULL AUTO_INCREMENT, " +
                "`biomiaID` INT NOT NULL , " +
                "`value` INT NOT NULL, " +
                "`inc` INT NOT NULL, " +
                "`comment` VARCHAR(36), " +
                "`timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "PRIMARY KEY (`key`) );", MySQL.Databases.stats_db);
    }
}
