package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.configs.Config;
import de.biomia.spigot.specialEvents.easterEvent.EasterEvent;
import de.biomia.spigot.specialEvents.winterEvent.WinterTag;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.universal.Messages;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static de.biomia.spigot.configs.Config.saveConfig;

public class EventCommands extends BiomiaCommand {

    private final static boolean winterEventOn = false;
    private final static boolean easterEventOn = false;
    private final static boolean schnitzelEventOn = false;

    public EventCommands(String string) {
        super(string);
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {

        Player p = (Player) sender;
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        if (!bp.isOwnerOrDev()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        // WINTER
        if (winterEventOn) {
            if (getName().equalsIgnoreCase("calendar")) {
                if (args.length >= 1) {

                    Entity entity = p.getNearbyEntities(1, 5, 1).get(0);
                    if (entity == null) {
                        sender.sendMessage("Stell dich in die Nähe eines Entities");
                        return;
                    }
                    switch (args[0].toLowerCase()) {
                        case "add":
                            if (args.length == 2) {
                                WinterTag.bindCalendarDayToEntity(Integer.valueOf(args[1]), entity.getUniqueId());
                                sender.sendMessage("Entity mit der uuid " + entity.getUniqueId().toString()
                                        + " wurde hinzugefügt zu Tag " + args[1] + "!");
                            } else
                                sender.sendMessage("/calendar add <Tag>");

                            break;
                        case "remove":
                            if (args.length == 2) {
                                Config.getConfig().set("Calendar." + args[1], null);
                                saveConfig();
                                sender.sendMessage("Entities für den Tag " + args[1] + " wurden gelöscht!");
                            } else
                                sender.sendMessage("/calendar remove <Tag>");

                            break;
                        default:
                            break;
                    }
                } else {
                    sender.sendMessage("/calendar (add | remove)");
                }
            }
        }

        //Ostern
        if (easterEventOn) {
            if (getName().equals("givereward") && bp.isOwnerOrDev()) {
                if (args.length >= 1) {
                    EasterEvent.giveReward(Biomia.getOfflineBiomiaPlayer(args[0]));
                } else
                    EasterEvent.giveReward(bp);
            }
            if (getName().equals("addeggs") && bp.isOwnerOrDev()) {
                //noinspection StatementWithEmptyBody
                if (args.length >= 2) {
                    //Main.getEvent().addEggs(Biomia.getOfflineBiomiaPlayer(args[0]).getBiomiaPlayerID(), Integer.valueOf(args[1]));
                }
            }
        }

        //Schnitzel
        if (schnitzelEventOn) {
            if (getName().equals("schnitzel") && bp.isOwnerOrDev()) {
                ItemStack is = ItemCreator.itemCreate(Material.BROWN_GLAZED_TERRACOTTA, "00A7eSchnitzel");
                p.getLocation().getWorld().dropItem(p.getLocation(), is);
            }
        }
    }
}
