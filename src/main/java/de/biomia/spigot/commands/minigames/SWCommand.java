package de.biomia.spigot.commands.minigames;

import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.configs.Config;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.configs.SkyWarsConfig;
import de.biomia.spigot.events.game.skywars.SkyWarsOpenChestEvent;
import de.biomia.spigot.minigames.GameType;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.biomia.spigot.configs.Config.saveConfig;

public class SWCommand extends BiomiaCommand {
    public SWCommand() {
        super("skywars", "sw");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("biomia.skywars")) {

                if (args.length >= 1) {

                    switch (args[0].toLowerCase()) {
                        case "setup":
                            int spielerProTeam, teams;
                            if (args.length == 4) {
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
                            } else {
                                sender.sendMessage("\u00A7c/sw setup <SpielerProTeam> <Teams> <MapName>");
                            }
                            break;
                        case "addloc":
                            if (args.length >= 2) {
                                SkyWarsConfig.addSpawnLocation(p.getLocation(), TeamColor.valueOf(args[1]), GameType.SKY_WARS);
                                sender.sendMessage("Spawnpoint wurde hinzugef\u00fcgt!");
                                return true;
                            }
                        case "addchest":
                            if (args.length >= 2) {
                                Location l = p.getTargetBlock(null, 100).getLocation();
                                if (l.getBlock().getType() == Material.CHEST) {
                                    switch (args[1].toLowerCase()) {
                                        case "g":
                                        case "good":
                                            SkyWarsConfig.addChestLocation(l, SkyWarsOpenChestEvent.ChestType.GoodChest);
                                            sender.sendMessage("Bessere Kiste hinzugef\u00fcgt!");
                                            break;
                                        case "n":
                                        case "normal":
                                            SkyWarsConfig.addChestLocation(l, SkyWarsOpenChestEvent.ChestType.NormalChest);
                                            sender.sendMessage("Normale Kiste hinzugef\u00fcgt!");
                                            break;
                                        default:
                                            sender.sendMessage("/sw addchest <normal/good>");
                                            break;
                                    }
                                } else {
                                    sender.sendMessage("\u00A7cSchau auf eine Kiste!");
                                }
                            } else {
                                sender.sendMessage("/sw addchest <normal/good>");
                            }
                            break;
                    }
                } else {
                    sender.sendMessage("\u00A7c/sw setup (Setup f\u00fcr SkyWars-Map)");
                    sender.sendMessage("\u00A7c/sw addloc (F\u00fcgt einen Spawnpunkt hinzu)");
                    sender.sendMessage("\u00A7c/sw addchest (F\u00fcgt eine Kiste hinzu)");
                }
            }
        }
        return false;
    }
}
