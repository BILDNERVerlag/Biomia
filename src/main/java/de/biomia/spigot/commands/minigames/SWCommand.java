package de.biomia.spigot.commands.minigames;

import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.configs.Config;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.configs.SkyWarsConfig;
import de.biomia.spigot.minigames.GameType;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.biomia.spigot.configs.Config.saveConfig;

public class SWCommand extends BiomiaCommand {
    //TODO renew (merge)
    public SWCommand() {
        super("skywars", "sw");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("biomia.skywars")) {

                if (args.length >= 1) {
                    if (args.length == 4 && args[0].equalsIgnoreCase("setup")) {
                        int spielerProTeam, teams;
                        try {
                            spielerProTeam = Integer.parseInt(args[1]);
                            teams = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            Bukkit.broadcastMessage("\u00A7c/sw setup <SpielerProTeam> <Teams> <MapName>");
                            return true;
                        }
                        String name = args[3];

                        Config.getConfig().set("Name", name);
                        MinigamesConfig.mapName = name;
                        Config.getConfig().set("TeamSize", spielerProTeam);
                        Config.getConfig().set("NumberOfTeams", teams);

                        saveConfig();
                    } else if (args[0].equalsIgnoreCase("setup")) {
                        sender.sendMessage("\u00A7c/sw setup <SpielerProTeam> <Teams> <MapName>");
                    } else if (args[0].equalsIgnoreCase("addloc") && args.length >= 2) {
                        SkyWarsConfig.addSpawnLocation(p.getLocation(), TeamColor.valueOf(args[1]), GameType.SKY_WARS);
                        sender.sendMessage("Spawnpoint wurde hinzugef\u00fcgt!");
                        return true;
                    }
                } else {
                    sender.sendMessage("\u00A7c/sw setup (Setup f\u00fcr SkyWars-Map)");
                    sender.sendMessage("\u00A7c/sw addloc (F\u00fcgt einen Spawnpunkt hinzu)");
                }
            }
        }
        return false;
    }
}
