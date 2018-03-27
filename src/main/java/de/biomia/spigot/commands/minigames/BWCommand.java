package de.biomia.spigot.commands.minigames;

import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.configs.BedWarsConfig;
import de.biomia.spigot.configs.Config;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.minigames.GameType;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.minigames.general.shop.ItemType;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BWCommand extends BiomiaCommand {
    //TODO renew (merge)
    public BWCommand() {
        super("bedwars", "bw");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("biomia.bedwars")) {
                if (args.length >= 1) {

                    args[0] = args[0].toLowerCase();
                    switch (args[0]) {
                        case "addloc":
                            if (args.length >= 2) {
                                BedWarsConfig.addSpawnLocation(p.getLocation(), TeamColor.valueOf(args[1]), GameType.BED_WARS);
                                sender.sendMessage("Spawnpoint wurde hinzugef\u00fcgt!");
                            } else
                                sender.sendMessage("/bedwars addloc team");
                            break;
                        case "addspawner":
                            Location l = p.getTargetBlock(null, 100).getLocation();

                            switch (l.getBlock().getType()) {
                                case HARD_CLAY:
                                    BedWarsConfig.addSpawnerLocations(l, ItemType.BRONZE);
                                    break;
                                case IRON_BLOCK:
                                    BedWarsConfig.addSpawnerLocations(l, ItemType.IRON);
                                    break;
                                case GOLD_BLOCK:
                                    BedWarsConfig.addSpawnerLocations(l, ItemType.GOLD);
                                    break;
                                default:
                                    p.sendMessage("Schau auf einen verf\u00fcgbaren Block!");
                                    return true;
                            }
                            p.sendMessage("Spawner hinzugef\u00fcgt!");
                            break;
                        case "villager":
                            p.getInventory().addItem(ItemCreator.itemCreate(Material.MONSTER_EGG, BedWarsItemNames.villagerSpawner));
                            break;
                        case "addbed":
                            if (args.length >= 2) {
                                Block blockFoot = p.getLocation().getBlock();
                                Block blockHead = p.getTargetBlock(null, 100);

                                if (blockFoot.getType() == Material.BED_BLOCK && blockHead.getType() == Material.BED_BLOCK) {
                                    BedWarsConfig.addBedsLocations(blockHead.getLocation(), blockFoot.getLocation(), TeamColor.valueOf(args[1]));
                                    Bukkit.broadcastMessage("\u00A7cBett hinzugef\u00fcgt!");
                                } else
                                    p.sendMessage(BedWarsMessages.blocksMustBeBeds);
                            } else
                                sender.sendMessage("/bedwars addbed mapDisplayName teamID");
                            break;
                        default:
                            break;
                        case "setup":
                            if (args.length >= 4) {
                                int spielerProTeam, teams;
                                try {
                                    spielerProTeam = Integer.parseInt(args[1]);
                                    teams = Integer.parseInt(args[2]);
                                } catch (NumberFormatException e) {
                                    sender.sendMessage("\u00A77/\u00A7bsw setup \u00A77<\u00A7bSpielerProTeam\u00A77> <\u00A7bTeams\u00A77> <\u00A7bMapName\u00A77>");
                                    return true;
                                }
                                String name = args[3];
                                Config.getConfig().set("Name", name);
                                MinigamesConfig.mapName = name;
                                Config.getConfig().set("TeamSize", spielerProTeam);
                                Config.getConfig().set("NumberOfTeams", teams);
                                Config.saveConfig();
                            } else
                                sender.sendMessage("\u00A77/\u00A7bsw setup \u00A77<\u00A7bSpielerProTeam\u00A77> <\u00A7bTeams\u00A77> <\u00A7bMapName\u00A77>");
                            break;
                    }
                } else {
                    sender.sendMessage("\u00A7c/bedwars setup (Setup f\u00fcr BedWars-Map)");
                    sender.sendMessage("\u00A7c/bedwars addloc (F\u00fcgt einen Spawnpunkt hinzu)");
                    sender.sendMessage("\u00A7c/bedwars removelocs (Entfernt alle Spawnpunkte)");
                    sender.sendMessage("\u00A7c/bedwars deleteallsigns (Entfernt alle Schilder)");
                    sender.sendMessage("\u00A7c/bedwars getTeamjoinersetter (Gibt den Teamjoinersetter zur\u00fcck)");
                    sender.sendMessage("\u00A7c/bedwars getbedsetter (Gibt den Bedsetter zur\u00fcck)");
                    sender.sendMessage("\u00A7c/bedwars getSpawner (Gibt alle Verf\u00fcgbaren Spawner zur\u00fcck)");
                    sender.sendMessage("\u00A7c/bedwars villager (Gibt einen Villager Spawner zur\u00fcck)");
                }
            }
        }
        return false;
    }
}
