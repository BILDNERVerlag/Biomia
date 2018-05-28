package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.general.cosmetics.Cosmetic;
import de.biomia.spigot.general.cosmetics.MysteryChest;
import de.biomia.spigot.general.cosmetics.items.*;
import de.biomia.spigot.general.cosmetics.items.CosmeticItem.Commonness;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.universal.Messages;
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
    public void onCommand(CommandSender sender, String label, String[] args) {

        Player p = (Player) sender;
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        if (!bp.isOwnerOrDev()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

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
                                    sb.append("§r").append(args[i]).append(" ");
                                    i++;
                                }

                                ItemStack is = ItemCreator.headWithSkin(headName, "§5" + name);
                                ItemStack head = is.clone();

                                ItemMeta isMeta = is.getItemMeta();
                                isMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                                        "§o§4" + c.deutsch() + ";§r" + sb.toString()).split(";")));
                                is.setItemMeta(isMeta);

                                Cosmetic.addItemToDatabase(new CosmeticHeadItem(-1, name, is, c, head));
                            } else {
                                p.sendMessage(
                                        "§5/§2cosmetic add head <Head Name> <Name> <Commonness> <Description (split lines with ';')>");
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
                                meta.setDisplayName("§b" + name);
                                is.setItemMeta(meta);
                                ItemStack gadgetItem = is.clone();
                                meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                                        "§o§4" + c.deutsch() + ";§r" + sb.toString()).split(";")));
                                is.setItemMeta(meta);

                                Cosmetic.addItemToDatabase(new CosmeticGadgetItem(-1, name, is, c, gadgetItem));
                            } else {
                                p.sendMessage(
                                        "§5/§2cosmetic add gadget <Name> <Commonness> <Description (split lines with ';')>");
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
                                meta.setDisplayName("§2" + name);
                                meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                                        "§o§4" + c.deutsch() + ";§r" + sb.toString()).split(";")));
                                is.setItemMeta(meta);

                                CosmeticSuitItem item = new CosmeticSuitItem(-1, name, is, c);
                                item.setBoots(p.getInventory().getBoots());
                                item.setChestplate(p.getInventory().getChestplate());
                                item.setLeggins(p.getInventory().getLeggings());
                                item.setHelmet(p.getInventory().getHelmet());

                                de.biomia.spigot.general.cosmetics.Cosmetic.addItemToDatabase(item);
                            } else {
                                p.sendMessage(
                                        "§5/§2cosmetic add suit <Name> <Commonness> <Description (split lines with ';')>");
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
                                meta.setDisplayName("§b" + name);
                                meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                                        "§o§4" + c.deutsch() + ";§r" + sb.toString()).split(";")));
                                is.setItemMeta(meta);

                                de.biomia.spigot.general.cosmetics.Cosmetic
                                        .addItemToDatabase(new CosmeticParticleItem(-1, name, is, c));
                            } else {
                                p.sendMessage(
                                        "§5/§2cosmetic add particle <Name> <Commonness> <Description (split lines with ';')>");
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
                                meta.setDisplayName("§4" + name);
                                meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                                        "§o§4" + c.deutsch() + ";§r" + sb.toString()).split(";")));
                                is.setItemMeta(meta);

                                de.biomia.spigot.general.cosmetics.Cosmetic
                                        .addItemToDatabase(new CosmeticPetItem(-1, name, is, c, type));
                            } else {
                                p.sendMessage(
                                        "§5/§2cosmetic add pet <Name> <Commonness> <entitytype> <Description (split lines with ';')>");
                            }
                            break;
                        default:
                            break;
                    }
                }
            } else if (args[0].equals("inv")) {
                de.biomia.spigot.general.cosmetics.Cosmetic.openMainInventory(Biomia.getBiomiaPlayer(p));
            } else if (args[0].equals("open")) {
                MysteryChest.open(Biomia.getBiomiaPlayer(p));
            }
        }
    }
}
