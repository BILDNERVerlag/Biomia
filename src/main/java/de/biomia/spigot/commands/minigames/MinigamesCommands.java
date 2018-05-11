package de.biomia.spigot.commands.minigames;

import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.configs.BedWarsConfig;
import de.biomia.spigot.configs.Config;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.minigames.GameType;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MinigamesCommands extends BiomiaCommand {

    public MinigamesCommands(String command, String... args) {
        super(command, args);
    }

    @Override
    protected void onCommand(CommandSender sender, String label, String[] args) {

        Player p = (Player) sender;

        switch (label) {
            case "addloc":
            case "al":
                if (args.length >= 1) {
                    BedWarsConfig.addSpawnLocation(p.getLocation(), TeamColor.valueOf(args[1]), GameType.BED_WARS);
                    sender.sendMessage("Spawnpoint wurde hinzugefügt!");
                } else
                    sender.sendMessage(String.format("/%s team", label));
                break;
            case "setup":
                if (args.length >= 3) {
                    int spielerProTeam, teams;
                    try {
                        spielerProTeam = Integer.parseInt(args[0]);
                        teams = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage("\u00A77/\u00A7bsetup \u00A77<\u00A7bSpielerProTeam\u00A77> <\u00A7bTeams\u00A77> <\u00A7bMapName\u00A77>");
                        return;
                    }
                    String name = args[2];
                    Config.getConfig().set("Name", name);
                    MinigamesConfig.mapName = name;
                    Config.getConfig().set("TeamSize", spielerProTeam);
                    Config.getConfig().set("NumberOfTeams", teams);
                    Config.saveConfig();
                } else
                    sender.sendMessage("\u00A77/\u00A7bsetup \u00A77<\u00A7bSpielerProTeam\u00A77> <\u00A7bTeams\u00A77> <\u00A7bMapName\u00A77>");
                break;
        }
    }
}
