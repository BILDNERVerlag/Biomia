package de.biomia.commands.general;

import de.biomia.Biomia;
import de.biomia.commands.BiomiaCommand;
import de.biomia.general.cosmetics.*;
import de.biomia.general.cosmetics.items.*;
import de.biomia.general.cosmetics.items.CosmeticItem.Commonness;
import de.biomia.tools.ItemCreator;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class CosmeticCommand extends BiomiaCommand {

    public CosmeticCommand() {
        super("cosmetic");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("biomia.cosmetics")) {
                if (args.length >= 1) {
                    if (args[0].equalsIgnoreCase("add")) {
                        if (args.length >= 2) {

                            switch (args[1].toLowerCase()) {
                            case "head":
                                if (args.length >= 6) {
                                    String headName = args[2];
                                    String name = args[3];
                                    Commonness c = Commonness.valueOf(args[4].toUpperCase());

                                    StringBuilder sb = new StringBuilder();
                                    int i = 5;
                                    while (args.length > i) {
                                        sb.append("\u00A7r").append(args[i]).append(" ");
                                        i++;
                                    }

                                    ItemStack is = ItemCreator.headWithSkin(headName, "\u00A75" + name);
                                    ItemStack head = is.clone();

                                    ItemMeta isMeta = is.getItemMeta();
                                    isMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                                            "\u00A7o\u00A74" + c.deutsch() + ";\u00A7r" + sb.toString()).split(";")));
                                    is.setItemMeta(isMeta);

                                    Cosmetic.addItemToDatabase(new CosmeticHeadItem(-1, name, is, c, head));
                                } else {
                                    p.sendMessage(
                                            "\u00A75/\u00A72cosmetic add head <Head Name> <Name> <Commonness> <Description (split lines with ';')>");
                                }
                                break;
                            case "gadget":
                                if (args.length >= 5) {
                                    String name = args[2];
                                    Commonness c = Commonness.valueOf(args[3].toUpperCase());

                                    StringBuilder sb = new StringBuilder();
                                    int i = 4;
                                    while (args.length > i) {
                                        sb.append(args[i]).append(" ");
                                        i++;
                                    }

                                    ItemStack is = p.getInventory().getItemInMainHand();

                                    ItemMeta meta = is.getItemMeta();
                                    meta.setDisplayName("\u00A7d" + name);
                                    is.setItemMeta(meta);
                                    ItemStack gadgetItem = is.clone();
                                    meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                                            "\u00A7o\u00A74" + c.deutsch() + ";\u00A7r" + sb.toString()).split(";")));
                                    is.setItemMeta(meta);

                                    Cosmetic.addItemToDatabase(new CosmeticGadgetItem(-1, name, is, c, gadgetItem));
                                } else {
                                    p.sendMessage(
                                            "\u00A75/\u00A72cosmetic add gadget <Name> <Commonness> <Description (split lines with ';')>");
                                }
                                break;
                            case "suit":
                                if (args.length >= 5) {
                                    String name = args[2];
                                    Commonness c = Commonness.valueOf(args[3].toUpperCase());

                                    StringBuilder sb = new StringBuilder();
                                    int i = 4;
                                    while (args.length > i) {
                                        sb.append(args[i]).append(" ");
                                        i++;
                                    }

                                    ItemStack is = p.getInventory().getItemInMainHand();

                                    ItemMeta meta = is.getItemMeta();
                                    meta.setDisplayName("\u00A72" + name);
                                    meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                                            "\u00A7o\u00A74" + c.deutsch() + ";\u00A7r" + sb.toString()).split(";")));
                                    is.setItemMeta(meta);

                                    CosmeticSuitItem item = new CosmeticSuitItem(-1, name, is, c);
                                    item.setBoots(p.getInventory().getBoots());
                                    item.setChestplate(p.getInventory().getChestplate());
                                    item.setLeggins(p.getInventory().getLeggings());
                                    item.setHelmet(p.getInventory().getHelmet());

                                    de.biomia.general.cosmetics.Cosmetic.addItemToDatabase(item);
                                } else {
                                    p.sendMessage(
                                            "\u00A75/\u00A72cosmetic add suit <Name> <Commonness> <Description (split lines with ';')>");
                                }
                                break;
                            case "particle":
                                if (args.length >= 5) {
                                    String name = args[2];
                                    Commonness c = Commonness.valueOf(args[3].toUpperCase());

                                    StringBuilder sb = new StringBuilder();
                                    int i = 4;
                                    while (args.length > i) {
                                        sb.append(args[i]).append(" ");
                                        i++;
                                    }

                                    ItemStack is = p.getInventory().getItemInMainHand();

                                    ItemMeta meta = is.getItemMeta();
                                    meta.setDisplayName("\u00A7b" + name);
                                    meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                                            "\u00A7o\u00A74" + c.deutsch() + ";\u00A7r" + sb.toString()).split(";")));
                                    is.setItemMeta(meta);

                                    de.biomia.general.cosmetics.Cosmetic
                                            .addItemToDatabase(new CosmeticParticleItem(-1, name, is, c));
                                } else {
                                    p.sendMessage(
                                            "\u00A75/\u00A72cosmetic add particle <Name> <Commonness> <Description (split lines with ';')>");
                                }
                                break;
                            case "pet":
                                if (args.length >= 5) {
                                    String name = args[2];
                                    Commonness c = Commonness.valueOf(args[3].toUpperCase());
                                    EntityType type = EntityType.valueOf(args[4].toUpperCase());

                                    StringBuilder sb = new StringBuilder();
                                    int i = 5;
                                    while (args.length > i) {
                                        sb.append(args[i]).append(" ");
                                        i++;
                                    }

                                    ItemStack is = p.getInventory().getItemInMainHand();

                                    ItemMeta meta = is.getItemMeta();
                                    meta.setDisplayName("\u00A74" + name);
                                    meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                                            "\u00A7o\u00A74" + c.deutsch() + ";\u00A7r" + sb.toString()).split(";")));
                                    is.setItemMeta(meta);

                                    de.biomia.general.cosmetics.Cosmetic
                                            .addItemToDatabase(new CosmeticPetItem(-1, name, is, c, type));
                                } else {
                                    p.sendMessage(
                                            "\u00A75/\u00A72cosmetic add pet <Name> <Commonness> <entitytype> <Description (split lines with ';')>");
                                }
                                break;
                            default:
                                break;
                            }
                        }
                    } else if (args[0].equals("inv")) {
                        de.biomia.general.cosmetics.Cosmetic.openMainInventory(Biomia.getBiomiaPlayer(p));
                    } else if (args[0].equals("open")) {
                        MysteryChest.open(Biomia.getBiomiaPlayer(p));
                    }
                }
            }
        }
        return false;
    }
}
