package de.biomia.spigot.commands.minigames;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.configs.BedWarsConfig;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.minigames.general.shop.ItemType;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BWCommand extends BiomiaCommand {
    public BWCommand() {
        super("bedwars", "bw");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {

        Player p = (Player) sender;
        if (!Biomia.getBiomiaPlayer(p).isOwnerOrDev()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        if (args.length >= 1) {
            args[0] = args[0].toLowerCase();
            switch (args[0]) {
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
                            p.sendMessage("Schau auf einen verfügbaren Block!");
                            return;
                    }
                    p.sendMessage("Spawner hinzugefügt!");
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
                            Bukkit.broadcastMessage("\u00A7cBett hinzugefügt!");
                        } else
                            p.sendMessage(BedWarsMessages.blocksMustBeBeds);
                    } else
                        sender.sendMessage("/bedwars addbed teamName");
                    break;
                default:
                    break;
            }
        } else {
            sender.sendMessage("\u00A7c/bedwars addspawner (Fügt Spawner hinzu)");
            sender.sendMessage("\u00A7c/bedwars addbed (Fügt Betten hinzu)");
            sender.sendMessage("\u00A7c/bedwars villager (Gibt einen Villager Spawner zurück)");
        }
    }
}
