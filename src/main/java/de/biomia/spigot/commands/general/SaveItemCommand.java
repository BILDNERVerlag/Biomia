package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.ItemStackSaver;
import de.biomia.universal.Messages;
import de.biomia.universal.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SaveItemCommand extends BiomiaCommand {

    public SaveItemCommand() {
        super("si");
    }

    public static final Inventory editItem;

    static {
        editItem = Bukkit.createInventory(null, 18, "Edit ItemStack");
        editItem.setItem(2, ItemCreator.itemCreate(Material.WOOL, "Name"));
        editItem.setItem(3, ItemCreator.itemCreate(Material.WOOL, "Lore"));
        editItem.setItem(4, ItemCreator.itemCreate(Material.WOOL, "Durability"));
        editItem.setItem(5, ItemCreator.itemCreate(Material.WOOL, "Data"));
        editItem.setItem(6, ItemCreator.itemCreate(Material.WOOL, "Enchantments"));
        editItem.setItem(11, ItemCreator.itemCreate(Material.WOOL, "Spieler"));
        editItem.setItem(12, ItemCreator.itemCreate(Material.WOOL, "Server"));
    }

    private final static String erfolgreichGespeichert = Messages.format("Item erfolgreich mit ID XX gespeichert!");
    private final static String gibEineIDEin = Messages.format("Bitte gib eine ID ein!");
    private final static String erfolgreichKopiert = Messages.format("XX erfolgreich heruntergeladen!");
    private final static String erfolgreichHeruntergeladen = Messages.format("XX erfolgreich kopiert!");
    private final static String keinItemGefunden = Messages.format("Kein Item gefunden. Ist die ID korrekt?");
    private final static String speichernFehlgeschlagen = Messages.format("Speichern fehlgeschlagen. Hast du fehlerhafte Daten übergeben?");
    private final static String bearbeitenFehlgeschlagen = Messages.format("Bearbeitung fehlgeschlagen. Hast du fehlerhafte Daten übergeben?");
    private final static String bearbeitenFehlgeschlagenNF = Messages.format("Bearbeitung fehlgeschlagen. Du hast keine gültige Zahl übergeben!");

    public void onCommand(CommandSender sender, String label, String[] args) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) sender);
        if (!bp.isOwnerOrDev()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(Messages.format("/si save [spielername]"));
            sender.sendMessage(Messages.format("/si save <name> <lore> <material> <amount> <data> <durability> <enchants>"));
            sender.sendMessage(Messages.format("/si edit <ID> <%s> <new value>", "name / lore / enchants / type / data / amount / durability / server"));
            sender.sendMessage(Messages.format("/si load <ID>"));
            sender.sendMessage(Messages.format("/si pop <ID>"));
            return;
        }
        switch (args[0].toLowerCase()) {
            case "loadinv":
                ItemStackSaver.loadInventory(bp);
                bp.getPlayer().sendMessage(Messages.format("Inventar erfolgreich geladen!"));
                break;
            case "saveinv":
                ItemStackSaver.saveInventory(bp);
                sender.sendMessage(Messages.format("Inventar erfolgreich hochgeladen."));
                break;
            case "info":
                int itemid;
                try {
                    itemid = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(bearbeitenFehlgeschlagen);
                    return;
                }
                ItemStack is = ItemStackSaver.getItemStack(itemid);
                if (is == null) {
                    sender.sendMessage(keinItemGefunden);
                }
                sender.sendMessage(Messages.COLOR_MAIN + "<" + Messages.COLOR_AUX + "ID: " + args[1]);
                if (is.hasItemMeta()) {
                    if (is.getItemMeta().hasDisplayName())
                        sender.sendMessage(Messages.COLOR_AUX + "Name: " + is.getItemMeta().getDisplayName());
                    if (is.getItemMeta().hasLore())
                        sender.sendMessage(Messages.COLOR_AUX + "Lore: " + is.getItemMeta().getLore());
                } else {
                    sender.sendMessage(Messages.COLOR_AUX + "No meta.");
                }
                sender.sendMessage(Messages.COLOR_AUX + "Type: " + is.getType().name());
                sender.sendMessage(Messages.COLOR_AUX + "Amount: " + is.getAmount());
                sender.sendMessage(Messages.COLOR_AUX + "PlayerID: " + ItemStackSaver.getBiomiaPlayerID(itemid));
                sender.sendMessage(Messages.COLOR_AUX + "Server: " + ItemStackSaver.getServer(itemid) + Messages.COLOR_MAIN + ">");
                break;
            case "edit":
                if (args.length < 3) {
                    sender.sendMessage(Messages.format("/si edit <ID> <%s> <new value>", "name / lore / enchants / type / data / amount / durability / server"));
                    return;
                }
                switch (args[2].toLowerCase()) {
                    case "name":
                    case "lore":
                    case "enchants":
                        break;
                    case "type":
                        try {
                            Material.valueOf(args[3]);
                        } catch (Exception e) {
                            sender.sendMessage(bearbeitenFehlgeschlagen);
                            return;
                        }
                        break;
                    case "data":
                    case "amount":
                    case "durability":
                    case "biomiaID":
                        try {
                            Integer.parseInt(args[3]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(bearbeitenFehlgeschlagenNF);
                            return;
                        }
                        break;
                    default:
                        sender.sendMessage(Messages.format("/si edit <ID> <%s> <new value>", "name / lore / enchants / type / data / amount / durability / server"));
                        return;
                }
                MySQL.executeUpdate("UPDATE SavedItemStacks SET " + args[2] + " = '" + args[3] + "' WHERE ID = " + Integer.parseInt(args[1]), MySQL.Databases.biomia_db);
                sender.sendMessage(Messages.format("Du hast das Feld <" + args[2] + "> erfolgreich editiert. Der neue Wert ist jetzt <" + args[3] + ">."));
                break;
            case "save":
                int id = -1;
                if (args.length < 2) {
                    id = ItemStackSaver.saveItemStack(bp.getPlayer().getInventory().getItemInMainHand(), null);
                } else if (args.length == 2) {
                    try {
                        id = ItemStackSaver.saveItemStack(bp.getPlayer().getInventory().getItemInMainHand(), Biomia.getBiomiaPlayer((Player) sender));
                    } catch (Exception e) {
                        sender.sendMessage(speichernFehlgeschlagen);
                        return;
                    }
                } else {
                    try {
                        String name = args[1];
                        String lore = args[2];
                        String material = args[3];
                        int amount = Integer.parseInt(args[4]);
                        int data = Integer.parseInt(args[5]);
                        int durability = Integer.parseInt(args[6]);
                        String enchantments = args[7];
                        id = ItemStackSaver.saveItemStack(name, lore, material, amount, data, durability, null, enchantments, -1, null);
                    } catch (Exception e) {
                        sender.sendMessage(Messages.format("/si save <name> <lore> <material> <amount> <data> <durability> <enchantments>"));
                        e.printStackTrace();
                    }
                }
                if (id != -1)
                    sender.sendMessage(erfolgreichGespeichert.replace("XX", Messages.COLOR_SUB + id + Messages.COLOR_MAIN));
                else sender.sendMessage(speichernFehlgeschlagen);
                break;
            case "load":
            case "pop":
                ItemStack toLoad;
                if (args.length < 2) {
                    sender.sendMessage(gibEineIDEin);
                    return;
                }
                try {
                    toLoad = (args[0].equals("load"))
                            ? ItemStackSaver.getItemStack(Integer.parseInt(args[1]))
                            : ItemStackSaver.popItemStack(Integer.parseInt(args[1]));
                } catch (NumberFormatException e) {
                    sender.sendMessage(gibEineIDEin);
                    break;
                }
                if (toLoad == null) {
                    sender.sendMessage(keinItemGefunden);
                } else {
                    bp.getPlayer().getInventory().addItem(toLoad);
                    if (args[0].equals("pop"))
                        sender.sendMessage(erfolgreichHeruntergeladen.replace("XX", toLoad.getItemMeta().getDisplayName()));
                    else sender.sendMessage(erfolgreichKopiert.replace("XX", toLoad.getItemMeta().getDisplayName()));
                }
                break;
            default:
                break;
        }
    }

}
