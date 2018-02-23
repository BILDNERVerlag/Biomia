package de.biomia.versus.bw.commands;

import de.biomia.versus.bw.messages.ItemNames;
import de.biomia.versus.bw.messages.Messages;
import de.biomia.versus.bw.var.ItemType;
import de.biomia.versus.global.configs.BedWarsConfig;
import de.biomiaAPI.itemcreator.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BW implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("biomia.bedwars")) {
                if (args.length >= 1) {
                    switch (args[0].toLowerCase()) {
                        case "addloc":
                            if (args.length >= 3) {
                                BedWarsConfig.addLocation(p.getLocation(), Integer.valueOf(args[1]), Integer.valueOf(args[2]));
                                sender.sendMessage("Spawnpoint wurde hinzugef�gt!");
                            } else
                                sender.sendMessage("/bw addloc mapID teamID");
                            break;
                        case "addspawner":
                            if (args.length >= 2) {
                                int mapID = Integer.valueOf(args[1]);
                                Location l = p.getTargetBlock(null, 100).getLocation();

                                switch (l.getBlock().getType()) {
                                    case HARD_CLAY:
                                        BedWarsConfig.addSpawnerLocations(l, ItemType.BRONZE, mapID);
                                        break;
                                    case IRON_BLOCK:
                                        BedWarsConfig.addSpawnerLocations(l, ItemType.IRON, mapID);
                                        break;
                                    case GOLD_BLOCK:
                                        BedWarsConfig.addSpawnerLocations(l, ItemType.GOLD, mapID);
                                        break;
                                    default:
                                        p.sendMessage("Schau auf einen verf�gbaren Block!");
                                        return true;
                                }
                                p.sendMessage("Spawner hinzugef�gt!");
                            } else
                                sender.sendMessage("/bw addspawner mapID");
                            break;
                        case "villager":
                            p.getInventory().addItem(ItemCreator.itemCreate(Material.MONSTER_EGG, ItemNames.villagerSpawner));
                            break;
                        case "addbed":
                            if (args.length >= 3) {
                                Block blockFoot = p.getLocation().getBlock();
                                Block blockHead = p.getTargetBlock(null, 100);

                                if (blockFoot.getType() == Material.BED_BLOCK && blockHead.getType() == Material.BED_BLOCK) {
                                    BedWarsConfig.setBed(Integer.valueOf(args[1]), Integer.valueOf(args[2]), blockHead.getLocation(), blockFoot.getLocation());
                                    Bukkit.broadcastMessage("�cBett hinzugef�gt!");
                                } else
                                    p.sendMessage(Messages.blocksMustBeBeds);
                            } else
                                sender.sendMessage("/bw addbed mapID teamID");
                            break;
                        default:
                            break;
                        case "yaml":
                            Bukkit.broadcastMessage(BedWarsConfig.config.getString("BedWars." + 100 + "." + 1 + ".Spawnpoints"));
                            Bukkit.broadcastMessage(BedWarsConfig.config.getString("BedWars." + 100 + "." + 2 + ".Spawnpoints"));
                            break;
                    }
                } else {
                    sender.sendMessage("�c/bw addloc (F�gt einen Spawnpunkt hinzu)");
                    sender.sendMessage("�c/bw addbed (F�gt ein Bett hinzu)");
                    sender.sendMessage("�c/bw addspawner (Schau auf einen Spawner)");
                    sender.sendMessage("�c/bw villager (Gibt einen Villager Spawner zur�ck)");
                }
            }
        }
        return true;
    }
}