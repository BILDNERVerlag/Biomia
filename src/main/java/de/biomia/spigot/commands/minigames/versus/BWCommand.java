package de.biomia.spigot.commands.minigames.versus;

import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.configs.BedWarsVersusConfig;
import de.biomia.spigot.configs.Config;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.minigames.general.ItemType;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class BWCommand extends BiomiaCommand {

    public BWCommand() {
        super("bedwars", "bedwars");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("biomia.bedwars")) {
                if (args.length >= 1) {
                    switch (args[0].toLowerCase()) {
                    case "addloc":
                        if (args.length >= 3) {
                            BedWarsVersusConfig.addLocation(p.getLocation(), Integer.valueOf(args[1]), Integer.valueOf(args[2]));
                            sender.sendMessage("Spawnpoint wurde hinzugef\u00fcgt!");
                        } else
                            sender.sendMessage("/bedwars addloc mapID teamID");
                        break;
                    case "addspawner":
                        if (args.length >= 2) {
                            int mapID = Integer.valueOf(args[1]);
                            Location l = p.getTargetBlock((Set<Material>) null, 100).getLocation();

                            switch (l.getBlock().getType()) {
                            case HARD_CLAY:
                                BedWarsVersusConfig.addSpawnerLocations(l, ItemType.BRONZE, mapID);
                                break;
                            case IRON_BLOCK:
                                BedWarsVersusConfig.addSpawnerLocations(l, ItemType.IRON, mapID);
                                break;
                            case GOLD_BLOCK:
                                BedWarsVersusConfig.addSpawnerLocations(l, ItemType.GOLD, mapID);
                                break;
                            default:
                                p.sendMessage("Schau auf einen verf\u00fcgbaren Block!");
                                return true;
                            }
                            p.sendMessage("Spawner hinzugef\u00fcgt!");
                        } else
                            sender.sendMessage("/bedwars addspawner mapID");
                        break;
                    case "villager":
                        p.getInventory().addItem(ItemCreator.itemCreate(Material.MONSTER_EGG, BedWarsItemNames.villagerSpawner));
                        break;
                    case "addbed":
                        if (args.length >= 3) {
                            Block blockFoot = p.getLocation().getBlock();
                            Block blockHead = p.getTargetBlock((Set<Material>) null, 100);

                            if (blockFoot.getType() == Material.BED_BLOCK && blockHead.getType() == Material.BED_BLOCK) {
                                BedWarsVersusConfig.setBed(Integer.valueOf(args[1]), Integer.valueOf(args[2]), blockHead.getLocation(), blockFoot.getLocation());
                                Bukkit.broadcastMessage("\u00A7cBett hinzugef\u00fcgt!");
                            } else
                                p.sendMessage(BedWarsMessages.blocksMustBeBeds);
                        } else
                            sender.sendMessage("/bedwars addbed mapID teamID");
                        break;
                    default:
                        break;
                    case "yaml":
                        Bukkit.broadcastMessage(Config.getConfig().getString("BedWars." + 100 + "." + 1 + ".Spawnpoints"));
                        Bukkit.broadcastMessage(Config.getConfig().getString("BedWars." + 100 + "." + 2 + ".Spawnpoints"));
                        break;
                    }
                } else {
                    sender.sendMessage("\u00A7c/bedwars addloc (F\u00fcgt einen Spawnpunkt hinzu)");
                    sender.sendMessage("\u00A7c/bedwars addbed (F\u00fcgt ein Bett hinzu)");
                    sender.sendMessage("\u00A7c/bedwars addspawner (Schau auf einen Spawner)");
                    sender.sendMessage("\u00A7c/bedwars villager (Gibt einen Villager Spawner zur\u00fcck)");
                }
            }
        }
        return true;
    }
}
