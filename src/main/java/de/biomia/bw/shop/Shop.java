package de.biomia.bw.shop;

import de.biomia.bw.messages.ItemNames;
import de.biomia.bw.messages.Messages;
import de.biomia.bw.var.ColorType;
import de.biomia.bw.var.ItemType;
import de.biomia.bw.var.Variables;
import de.biomia.api.itemcreator.ItemCreator;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;

public class Shop {

    public static final ItemStack backItem = ItemCreator.headWithSkin("MHF_ArrowLeft", ItemNames.back);
    private static final ArrayList<ShopGroup> groups = new ArrayList<>();
    private static Inventory inv = null;

    private static ShopGroup addNewGroup(String groupName, ChatColor color, ItemStack item) {
        ShopGroup sg = new ShopGroup(color, groupName, item);
        groups.add(sg);
        return sg;
    }

    public static ArrayList<ShopGroup> getGroups() {
        return groups;
    }

    public static Inventory getInventory() {
        int invSize = (int) (Math.ceil(groups.size() / 7) * 9) + 18;

        if (inv == null) {
            inv = Bukkit.createInventory(null, invSize, Messages.shopInventory);
            int groupNum = 0;
            for (ShopGroup group : groups) {
                if (groupNum % 9 == 8) {
                    groupNum += 2;
                }
                inv.setItem(groupNum + 10, group.getIcon());
                groupNum++;
            }
        }
        return inv;
    }

    public static void init() {

        // Groups
        ShopGroup baumaterialien = addNewGroup("Baumaterialien", ChatColor.GOLD,
                ItemCreator.itemCreate(Material.GOLD_PICKAXE));

        ShopGroup waffen = addNewGroup("Waffen", ChatColor.GRAY, ItemCreator.itemCreate(Material.IRON_SWORD));

        ShopGroup ruestung = addNewGroup("Rüstung", ChatColor.DARK_PURPLE,
                ItemCreator.itemCreate(Material.IRON_CHESTPLATE));

        ShopGroup boegen = addNewGroup("Bögen", ChatColor.BLUE, ItemCreator.itemCreate(Material.BOW));

        ShopGroup essen = addNewGroup("Essen", ChatColor.GREEN, ItemCreator.itemCreate(Material.MELON));

        ShopGroup traenke = addNewGroup("Tränke", ChatColor.YELLOW, ItemCreator.itemCreate(Material.BREWING_STAND_ITEM));

        ShopGroup special = addNewGroup("Special", ChatColor.LIGHT_PURPLE,
                ItemCreator.itemCreate(Material.EYE_OF_ENDER));

        // Bau-Gruppe
        ItemStack clay = ItemCreator.itemCreate(Material.STAINED_CLAY);
        clay.setAmount(4);
        baumaterialien.addItem(new ShopItem(4, clay, ColorType.BLOCK));

        ItemStack glass = ItemCreator.itemCreate(Material.STAINED_GLASS);
        baumaterialien.addItem(new ShopItem(2, glass, ColorType.BLOCK));

        ItemStack glowstone = ItemCreator.itemCreate(Material.GLOWSTONE);
        baumaterialien.addItem(new ShopItem(ItemType.BRONZE, 2, glowstone));

        ItemStack ironblock = ItemCreator.itemCreate(Material.IRON_BLOCK);
        baumaterialien.addItem(new ShopItem(ItemType.IRON, 3, ironblock));

        ItemStack woodpick = ItemCreator.itemCreate(Material.WOOD_PICKAXE);
        ItemStack stonepick = ItemCreator.itemCreate(Material.STONE_PICKAXE);
        ItemStack ironpick = ItemCreator.itemCreate(Material.IRON_PICKAXE);
        woodpick.addEnchantment(Enchantment.DIG_SPEED, 1);
        stonepick.addEnchantment(Enchantment.DIG_SPEED, 1);
        ironpick.addEnchantment(Enchantment.DIG_SPEED, 1);

        baumaterialien.addItem(new ShopItem(ItemType.BRONZE, 5, woodpick));
        baumaterialien.addItem(new ShopItem(ItemType.IRON, 2, stonepick));
        baumaterialien.addItem(new ShopItem(ItemType.GOLD, 1, ironpick));

        // Waffen
        ItemStack stick = ItemCreator.itemCreate(Material.STICK);
        stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        waffen.addItem(new ShopItem(ItemType.BRONZE, 8, stick));

        ItemStack woodsword = ItemCreator.itemCreate(Material.WOOD_SWORD);
        woodsword.addEnchantment(Enchantment.DURABILITY, 1);
        woodsword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        waffen.addItem(new ShopItem(ItemType.IRON, 1, woodsword));

        ItemStack woodsword2 = ItemCreator.itemCreate(Material.WOOD_SWORD);
        woodsword2.addEnchantment(Enchantment.DURABILITY, 1);
        woodsword2.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        waffen.addItem(new ShopItem(ItemType.IRON, 3, woodsword2));

        ItemStack stonesword = ItemCreator.itemCreate(Material.STONE_SWORD);
        stonesword.addEnchantment(Enchantment.DURABILITY, 1);
        stonesword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        waffen.addItem(new ShopItem(ItemType.IRON, 5, stonesword));

        ItemStack ironsword = ItemCreator.itemCreate(Material.IRON_SWORD);
        ironsword.addEnchantment(Enchantment.DURABILITY, 1);
        ironsword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        waffen.addItem(new ShopItem(ItemType.GOLD, 5, ironsword));

        ItemStack flintsteel = ItemCreator.itemCreate(Material.FLINT_AND_STEEL);
        waffen.addItem(new ShopItem(ItemType.IRON, 4, flintsteel));

        ItemStack rod = ItemCreator.itemCreate(Material.FISHING_ROD);
        rod.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        waffen.addItem(new ShopItem(ItemType.IRON, 5, rod));

        // Rässi
        ItemStack leathercap = ItemCreator.itemCreate(Material.LEATHER_HELMET);
        ItemStack leatherchest = ItemCreator.itemCreate(Material.LEATHER_CHESTPLATE);
        ItemStack leatherpants = ItemCreator.itemCreate(Material.LEATHER_LEGGINGS);
        ItemStack leatherboots = ItemCreator.itemCreate(Material.LEATHER_BOOTS);

        leatherboots.addEnchantment(Enchantment.DURABILITY, 1);
        leatherboots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leathercap.addEnchantment(Enchantment.DURABILITY, 1);
        leathercap.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leatherchest.addEnchantment(Enchantment.DURABILITY, 1);
        leatherchest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leatherpants.addEnchantment(Enchantment.DURABILITY, 1);
        leatherpants.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        ruestung.addItem(new ShopItem(1, leathercap, ColorType.LEATHER));
        ruestung.addItem(new ShopItem(1, leatherpants, ColorType.LEATHER));
        ruestung.addItem(new ShopItem(1, leatherboots, ColorType.LEATHER));
        ruestung.addItem(new ShopItem(2, leatherchest, ColorType.LEATHER));

        ItemStack chainchest1 = ItemCreator.itemCreate(Material.CHAINMAIL_CHESTPLATE);
        ItemStack chainchest2 = ItemCreator.itemCreate(Material.CHAINMAIL_CHESTPLATE);
        ItemStack ironchest = ItemCreator.itemCreate(Material.IRON_CHESTPLATE);

        chainchest1.addEnchantment(Enchantment.DURABILITY, 1);
        chainchest1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

        chainchest2.addEnchantment(Enchantment.DURABILITY, 1);
        chainchest2.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        chainchest2.addEnchantment(Enchantment.THORNS, 1);

        ironchest.addEnchantment(Enchantment.DURABILITY, 1);
        ironchest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

        ruestung.addItem(new ShopItem(ItemType.IRON, 2, chainchest1));
        ruestung.addItem(new ShopItem(ItemType.IRON, 5, chainchest2));
        ruestung.addItem(new ShopItem(ItemType.GOLD, 6, ironchest));

        // Bogen
        ItemStack bow1 = ItemCreator.itemCreate(Material.BOW);
        ItemStack bow2 = ItemCreator.itemCreate(Material.BOW);
        ItemStack bow3 = ItemCreator.itemCreate(Material.BOW);
        ItemStack arrow = ItemCreator.itemCreate(Material.ARROW);

        bow1.addEnchantment(Enchantment.DURABILITY, 1);
        bow2.addEnchantment(Enchantment.DURABILITY, 1);
        bow3.addEnchantment(Enchantment.DURABILITY, 1);
        bow1.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow2.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow3.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow1.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        bow2.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
        bow3.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
        bow3.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);

        boegen.addItem(new ShopItem(ItemType.GOLD, 3, bow1));
        boegen.addItem(new ShopItem(ItemType.GOLD, 6, bow2));
        boegen.addItem(new ShopItem(ItemType.GOLD, 12, bow3));
        boegen.addItem(new ShopItem(ItemType.GOLD, 1, arrow));

        // Essen
        ItemStack carrot = ItemCreator.itemCreate(Material.GOLDEN_CARROT);
        essen.addItem(new ShopItem(ItemType.BRONZE, 1, carrot));

        ItemStack steak = ItemCreator.itemCreate(Material.COOKED_BEEF);
        essen.addItem(new ShopItem(ItemType.BRONZE, 2, steak));

        ItemStack cake = ItemCreator.itemCreate(Material.CAKE);
        essen.addItem(new ShopItem(ItemType.IRON, 1, cake));

        ItemStack goldenApple = ItemCreator.itemCreate(Material.GOLDEN_APPLE);
        essen.addItem(new ShopItem(ItemType.GOLD, 1, goldenApple));

        // Tränke
        ItemStack heal1 = getPotionItemStack(Material.POTION, PotionType.INSTANT_HEAL, false);
        traenke.addItem(new ShopItem(ItemType.IRON, 1, heal1));

        ItemStack heal2 = getPotionItemStack(Material.POTION, PotionType.INSTANT_HEAL, true);
        traenke.addItem(new ShopItem(ItemType.IRON, 2, heal2));

        ItemStack heal3 = getPotionItemStack(Material.LINGERING_POTION, PotionType.INSTANT_HEAL, false);
        traenke.addItem(new ShopItem(ItemType.IRON, 2, heal3));

        ItemStack heal4 = getPotionItemStack(Material.LINGERING_POTION, PotionType.INSTANT_HEAL, true);
        traenke.addItem(new ShopItem(ItemType.IRON, 3, heal4));

        ItemStack speed = getPotionItemStack(Material.POTION, PotionType.SPEED, false);
        traenke.addItem(new ShopItem(ItemType.IRON, 6, speed));

        ItemStack regen = getPotionItemStack(Material.POTION, PotionType.REGEN, false);
        traenke.addItem(new ShopItem(ItemType.GOLD, 2, regen));

        ItemStack strength = getPotionItemStack(Material.POTION, PotionType.STRENGTH, false);
        traenke.addItem(new ShopItem(ItemType.GOLD, 7, strength));

        ItemStack wand = ItemCreator.itemCreate(Material.BRICK, ItemNames.wand);
        special.addItem(new ShopItem(ItemType.BRONZE, 64, wand));

        ItemStack warp = ItemCreator.itemCreate(Material.BLAZE_POWDER, ItemNames.warper);
        special.addItem(new ShopItem(ItemType.IRON, 3, warp));

        ItemStack platform = ItemCreator.itemCreate(Material.END_ROD, ItemNames.rettungsPlattform);
        special.addItem(new ShopItem(ItemType.GOLD, 3, platform));

        ItemStack shop = ItemCreator.itemCreate(Material.ARMOR_STAND, ItemNames.shortShop);
        special.addItem(new ShopItem(ItemType.GOLD, 5, shop));

        ItemStack enderpearl = ItemCreator.itemCreate(Material.ENDER_PEARL);
        special.addItem(new ShopItem(ItemType.GOLD, 11, enderpearl));

        ItemStack ladder = ItemCreator.itemCreate(Material.LADDER);
        special.addItem(new ShopItem(ItemType.BRONZE, 4, ladder));

        ItemStack cobweb = ItemCreator.itemCreate(Material.WEB);
        special.addItem(new ShopItem(ItemType.BRONZE, 16, cobweb));

        ItemStack tnt = ItemCreator.itemCreate(Material.TNT);
        special.addItem(new ShopItem(ItemType.GOLD, 3, tnt));

        ItemStack chest = ItemCreator.itemCreate(Material.CHEST);
        special.addItem(new ShopItem(ItemType.IRON, 3, chest));

        special.addItem(new ShopItem(ItemType.GOLD, 1, Variables.teamChest));
    }

    private static ItemStack getPotionItemStack(Material potionType, PotionType type, boolean higherLevel) {
        ItemStack potion = new ItemStack(potionType, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setBasePotionData(new PotionData(type, false, higherLevel));
        potion.setItemMeta(meta);
        return potion;
    }
}
