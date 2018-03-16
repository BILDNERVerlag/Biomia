package de.biomia.spigot.commands.minigames.versus;

import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.configs.SkyWarsConfig;
import de.biomia.spigot.events.skywars.SkyWarsOpenChestEvent;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class SWCommand extends BiomiaCommand {

    public SWCommand() {
        super("skywars", "sw");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("biomia.skywars.setup")) {
                if (args.length >= 1) {
                    switch (args[0].toLowerCase()) {
                        case "addloc":
                            if (args.length >= 3) {
                                TeamColor team = TeamColor.valueOf(args[2].toUpperCase());
                                new SkyWarsConfig().addSpawnLocation(p.getLocation(), team);
                                sender.sendMessage("Spawnpoint wurde hinzugef\u00fcgt!");
                            } else
                                sender.sendMessage("/sw addloc mapID teamID");
                            break;
                        case "addchest":
                            if (args.length >= 3) {
                                Location l = p.getTargetBlock((Set<Material>) null, 100).getLocation();
                                if (l.getBlock().getType() == Material.CHEST) {
                                    String mapDisplayName = args[1];
                                    switch (args[2].toLowerCase()) {
                                        case "good":
                                            new SkyWarsConfig().addChestLocation(l, SkyWarsOpenChestEvent.ChestType.GoodChest);
                                            sender.sendMessage("Bessere Kiste hinzugef\u00fcgt!");
                                            break;
                                        case "normal":
                                            new SkyWarsConfig().addChestLocation(l, SkyWarsOpenChestEvent.ChestType.NormalChest);
                                            sender.sendMessage("Normale Kiste hinzugef\u00fcgt!");
                                            break;
                                        default:
                                            sender.sendMessage("/sw addchest mapID <normal/good>");
                                            break;
                                    }
                                } else {
                                    sender.sendMessage("\u00A7cSchau auf eine Kiste!");
                                }
                            } else {
                                sender.sendMessage("/sw addchest mapID <normal/good>");
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    sender.sendMessage("\u00A7c/sw addloc (F\u00fcgt einen Spawnpunkt hinzu)");
                    sender.sendMessage("\u00A7c/sw addchest (F\u00fcgt eine Kiste hinzu)");
                }
            }
        }
        return true;
    }
}
