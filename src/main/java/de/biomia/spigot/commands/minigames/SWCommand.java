package de.biomia.spigot.commands.minigames;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.configs.SkyWarsConfig;
import de.biomia.spigot.events.game.skywars.SkyWarsOpenChestEvent;
import de.biomia.universal.Messages;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SWCommand extends BiomiaCommand {
    public SWCommand() {
        super("skywars", "sw");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {

        Player p = (Player) sender;
        if (!Biomia.getBiomiaPlayer(p).isOwnerOrDev()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "addchest":
                    if (args.length >= 2) {
                        Location l = p.getTargetBlock(null, 100).getLocation();
                        if (l.getBlock().getType() == Material.CHEST) {
                            switch (args[1].toLowerCase()) {
                                case "g":
                                case "good":
                                    SkyWarsConfig.addChestLocation(l, SkyWarsOpenChestEvent.ChestType.GoodChest);
                                    sender.sendMessage("Bessere Kiste hinzugef00fcgt!");
                                    break;
                                case "n":
                                case "normal":
                                    SkyWarsConfig.addChestLocation(l, SkyWarsOpenChestEvent.ChestType.NormalChest);
                                    sender.sendMessage("Normale Kiste hinzugef00fcgt!");
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
            sender.sendMessage("\u00A7c/sw addchest <normal/good> (F00fcgt eine Kiste hinzu)");
        }
    }
}
