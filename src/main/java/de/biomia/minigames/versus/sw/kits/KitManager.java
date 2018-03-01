package de.biomia.minigames.versus.sw.kits;

import de.biomia.minigames.versus.sw.messages.ItemNames;
import de.biomia.minigames.versus.sw.messages.Messages;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.api.mysql.MySQL;
import de.biomia.api.tools.SkyWarsKit;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class KitManager {

    public static final HashMap<Integer, Kit> allKits = new HashMap<>();
    private static final HashMap<BiomiaPlayer, KitManager> playerKits = new HashMap<>();
    public static Kit standardKit;
    private final ArrayList<Kit> availableKits = new ArrayList<>();
    private final Inventory inv;
    private final BiomiaPlayer bp;
    private Kit selectedKit = standardKit;

    private KitManager(BiomiaPlayer bp) {
        this.bp = bp;
        this.inv = Bukkit.createInventory(null, 36, "Kits");
        playerKits.put(bp, this);
        loadKits();
    }

    public static void initKits() {

        // Standart Kit
        Kit standartKit = new Kit("Standard Kit", 0, 0, ItemCreator.itemCreate(Material.IRON_PICKAXE), true);
        standartKit.addItem(0, ItemCreator.itemCreate(Material.IRON_SWORD));
        standartKit.addItem(1, ItemCreator.itemCreate(Material.IRON_PICKAXE));
        standartKit.addItem(2, ItemCreator.itemCreate(Material.IRON_AXE));
        standartKit.setDescription(Arrays.asList("Die gute alte", "Standardausr\u00fcstung"));
        standardKit = standartKit;

        // Bauarbeiter
        ItemStack yellow_hat = new ItemStack(Material.LEATHER_HELMET, 1);
        LeatherArmorMeta lch = (LeatherArmorMeta) yellow_hat.getItemMeta();
        lch.setColor(Color.fromRGB(255, 255, 0));
        yellow_hat.setItemMeta(lch);
        Kit bauarbeiter = new Kit("Bauarbeiter", 1, 30000, yellow_hat, true);
        ItemStack clay = ItemCreator.itemCreate(Material.HARD_CLAY);
        clay.setAmount(64);
        bauarbeiter.addItem(1, clay);
        bauarbeiter.addItem(2, clay);
        bauarbeiter.addItem(3, clay);
        ItemStack efficiency_pickaxe = ItemCreator.itemCreate(Material.IRON_PICKAXE);
        efficiency_pickaxe.addUnsafeEnchantment(Enchantment.DIG_SPEED, 2);
        bauarbeiter.addItem(0, efficiency_pickaxe);
        bauarbeiter.addItem(EquipmentSlot.HEAD, yellow_hat);
        bauarbeiter.setDescription(Arrays.asList("Schutzhelm aufsetzen", "nicht vergessen!"));

        // MLG
        Kit mlg = new Kit("MLG", 2, 20000, ItemCreator.itemCreate(Material.TNT), true);
        ItemStack tnt = ItemCreator.itemCreate(Material.TNT);
        tnt.setAmount(32);
        ItemStack slime = ItemCreator.itemCreate(Material.SLIME_BLOCK);
        slime.setAmount(4);
        ItemStack redstone = ItemCreator.itemCreate(Material.REDSTONE_BLOCK);
        redstone.setAmount(2);
        ItemStack wooden_p_plate = ItemCreator.itemCreate(Material.WOOD_PLATE);
        wooden_p_plate.setAmount(4);
        mlg.addItem(0, tnt);
        mlg.addItem(1, ItemCreator.itemCreate(Material.WATER_BUCKET));
        mlg.addItem(2, ItemCreator.itemCreate(Material.WATER_BUCKET));
        mlg.addItem(3, slime);
        mlg.addItem(4, redstone);
        mlg.addItem(5, wooden_p_plate);

        // TANK
        Kit tank = new Kit("TANK", 3, 50000, ItemCreator.itemCreate(Material.IRON_CHESTPLATE), true);
        ItemStack tank_hat = ItemCreator.itemCreate(Material.IRON_HELMET);
        tank_hat.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        tank_hat.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        ItemStack tank_chest = ItemCreator.itemCreate(Material.IRON_CHESTPLATE);
        tank_chest.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        tank_chest.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        ItemStack tank_pants = ItemCreator.itemCreate(Material.IRON_LEGGINGS);
        tank_pants.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        tank_pants.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        ItemStack tank_boots = ItemCreator.itemCreate(Material.IRON_BOOTS);
        tank_boots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        tank_boots.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        tank.addItem(EquipmentSlot.HEAD, tank_hat);
        tank.addItem(EquipmentSlot.CHEST, tank_chest);
        tank.addItem(EquipmentSlot.LEGS, tank_pants);
        tank.addItem(EquipmentSlot.FEET, tank_boots);

        // ENDERMAN
        Kit enderman = new Kit("Enderman", 4, 50000, ItemCreator.itemCreate(Material.ENDER_PEARL), true);
        ItemStack pearl = ItemCreator.itemCreate(Material.ENDER_PEARL);
        tank_hat.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
        enderman.addItem(0, pearl);

        // THORNS
        Kit thorns = new Kit("Dornen", 5, 30000, ItemCreator.itemCreate(Material.CHAINMAIL_HELMET), true);
        ItemStack thorns_hat = ItemCreator.itemCreate(Material.IRON_HELMET);
        thorns_hat.addUnsafeEnchantment(Enchantment.THORNS, 1);
        ItemStack thorns_chest = ItemCreator.itemCreate(Material.CHAINMAIL_CHESTPLATE);
        thorns_chest.addUnsafeEnchantment(Enchantment.THORNS, 2);
        ItemStack thorns_pants = ItemCreator.itemCreate(Material.CHAINMAIL_LEGGINGS);
        thorns_pants.addUnsafeEnchantment(Enchantment.THORNS, 2);
        ItemStack thorns_boots = ItemCreator.itemCreate(Material.IRON_BOOTS);
        thorns_boots.addUnsafeEnchantment(Enchantment.THORNS, 1);
        ItemStack vines = ItemCreator.itemCreate(Material.VINE);
        vines.setAmount(16);

        thorns.addItem(EquipmentSlot.HEAD, thorns_hat);
        thorns.addItem(EquipmentSlot.CHEST, thorns_chest);
        thorns.addItem(EquipmentSlot.LEGS, thorns_pants);
        thorns.addItem(EquipmentSlot.FEET, thorns_boots);
        thorns.addItem(4, vines);

        // Feuerleger
        Kit feuerleger = new Kit("Feuerleger", 6, 20000, ItemCreator.itemCreate(Material.LAVA_BUCKET), true);
        ItemStack fire_charge = ItemCreator.itemCreate(Material.FIREBALL);
        fire_charge.setAmount(16);
        ItemStack glowstone = ItemCreator.itemCreate(Material.GLOWSTONE);
        glowstone.setAmount(32);
        feuerleger.addItem(0, fire_charge);
        feuerleger.addItem(1, ItemCreator.itemCreate(Material.LAVA_BUCKET));
        feuerleger.addItem(2, ItemCreator.itemCreate(Material.LAVA_BUCKET));
        feuerleger.addItem(3, ItemCreator.itemCreate(Material.LAVA_BUCKET));
        feuerleger.addItem(4, glowstone);

        // potion master
        Kit trankmaster = new Kit("Trankmeister", 7, 50000, ItemCreator.itemCreate(Material.POTION), true);
        trankmaster.addItem(0, getPotionItemStack(Material.SPLASH_POTION, PotionType.INSTANT_HEAL, true));
        trankmaster.addItem(1, getPotionItemStack(Material.SPLASH_POTION, PotionType.INSTANT_HEAL, false));
        trankmaster.addItem(2, getPotionItemStack(Material.SPLASH_POTION, PotionType.REGEN, false));
        trankmaster.addItem(3, getPotionItemStack(Material.LINGERING_POTION, PotionType.INSTANT_HEAL, false));
        trankmaster.addItem(4, getPotionItemStack(Material.LINGERING_POTION, PotionType.INSTANT_HEAL, false));

        // Assassin
        Kit assassin = new Kit("Assassin", 8, 50000, ItemCreator.itemCreate(Material.DIAMOND_SWORD), true);

        ItemStack diamondSword = ItemCreator.itemCreate(Material.DIAMOND_SWORD);
        diamondSword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
        diamondSword.addUnsafeEnchantment(Enchantment.DURABILITY, 2);
        diamondSword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        assassin.addItem(0, diamondSword);

        // OneHitKit
        Kit onehit = new Kit("One-Hit-Kit", 9, 50000, ItemCreator.itemCreate(Material.SNOW_BALL), true);
        onehit.addItem(0, ItemCreator.itemCreate(Material.SNOW_BALL, ItemNames.oneHitSnowball));

        // Gummibogen
        Kit gummibogen = new Kit("Gummibogen", 10, 50000, ItemCreator.itemCreate(Material.BOW), true);
        ItemStack gummibogenitem = ItemCreator.itemCreate(Material.BOW, ItemNames.gummibogen);
        ItemStack gummipfeile = ItemCreator.itemCreate(Material.ARROW, ItemNames.gummipfeil);
        gummipfeile.setAmount(16);
        gummibogen.addItem(0, gummibogenitem);
        gummibogen.addItem(1, gummipfeile);

        // Knockback
        Kit knockbackstick = new Kit("Knockback", 11, 30000, ItemCreator.itemCreate(Material.STICK), true);
        ItemStack stick = ItemCreator.itemCreate(Material.STICK, "\u00A72Knockback-Stick");
        stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        knockbackstick.addItem(0, stick);

        // Zauberer
        Kit zauberer = new Kit("Zauberer", 12, 30000, ItemCreator.itemCreate(Material.ENCHANTMENT_TABLE), true);
        ItemStack enchantmentTable = ItemCreator.itemCreate(Material.ENCHANTMENT_TABLE);
        zauberer.addItem(0, enchantmentTable);
        ItemStack bottleofEnchant = ItemCreator.itemCreate(Material.EXP_BOTTLE);
        bottleofEnchant.setAmount(64);
        ItemStack lapislazuli = ItemCreator.itemCreate(Material.LAPIS_ORE);
        lapislazuli.setAmount(32);
        zauberer.addItem(1, bottleofEnchant);
        zauberer.addItem(2, bottleofEnchant.clone());
        zauberer.addItem(3, bottleofEnchant.clone());
        zauberer.addItem(4, lapislazuli);

        // WinterEvent Kit 1
        Kit eismeister = new Kit("Eismeister", 20171, 0, new ItemStack(Material.PACKED_ICE), false);
        // Plattenspieler + Schallplatte mit ruhiger, ann\u00fcherend weihnachtlicher Musik
        ItemStack schallplatte = ItemCreator.itemCreate(Material.RECORD_5);
        ItemStack plattenspieler = ItemCreator.itemCreate(Material.JUKEBOX);
        // etwas eis
        ItemStack ice1 = ItemCreator.itemCreate(Material.ICE);
        ice1.setAmount(16);
        ItemStack ice2 = ItemCreator.itemCreate(Material.PACKED_ICE);
        ice2.setAmount(16);
        ItemStack ice3 = ItemCreator.itemCreate(Material.SNOW_BLOCK);
        ice2.setAmount(16);
        // schneefarbene r\u00fcstung
        ItemStack winter_hat = ItemCreator.itemCreate(Material.LEATHER_HELMET, "Eishelm");
        LeatherArmorMeta hatMeta = (LeatherArmorMeta) winter_hat.getItemMeta();
        hatMeta.setColor(Color.fromRGB(219, 255, 255));
        winter_hat.setItemMeta(hatMeta);
        winter_hat.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 1);
        ItemStack winter_chest = ItemCreator.itemCreate(Material.LEATHER_CHESTPLATE, "Eisr\u00fcstung");
        LeatherArmorMeta chestMeta = (LeatherArmorMeta) winter_chest.getItemMeta();
        chestMeta.setColor(Color.fromRGB(219, 255, 255));
        winter_chest.setItemMeta(chestMeta);
        winter_chest.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 1);
        ItemStack winter_pants = ItemCreator.itemCreate(Material.LEATHER_LEGGINGS, "Eishose");
        LeatherArmorMeta pantsMeta = (LeatherArmorMeta) winter_pants.getItemMeta();
        pantsMeta.setColor(Color.fromRGB(219, 255, 255));
        winter_pants.setItemMeta(pantsMeta);
        winter_pants.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 1);
        ItemStack winter_boots = ItemCreator.itemCreate(Material.LEATHER_BOOTS, "Eisschuhe");
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) winter_boots.getItemMeta();
        bootsMeta.setColor(Color.fromRGB(219, 255, 255));
        winter_boots.setItemMeta(bootsMeta);
        winter_boots.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 1);
        eismeister.addItem(1, plattenspieler);
        eismeister.addItem(2, schallplatte);
        eismeister.addItem(3, ice1);
        eismeister.addItem(4, ice2);
        eismeister.addItem(5, ice3);
        eismeister.addItem(EquipmentSlot.HEAD, winter_hat);
        eismeister.addItem(EquipmentSlot.CHEST, winter_chest);
        eismeister.addItem(EquipmentSlot.LEGS, winter_pants);
        eismeister.addItem(EquipmentSlot.FEET, winter_boots);

        // WinterEvent Kit 2
        Kit weihnachtself = new Kit("Weihnachtself", 20172, 0, new ItemStack(Material.SNOW_BALL), false);
        // weihnachtselfenr\u00fcstung
        ItemStack elfen_hat = ItemCreator.itemCreate(Material.LEATHER_HELMET, "Hut des Weihnachtselfs");
        LeatherArmorMeta elfenHatMeta = (LeatherArmorMeta) elfen_hat.getItemMeta();
        elfenHatMeta.setColor(Color.fromRGB(179, 0, 12));
        elfen_hat.setItemMeta(elfenHatMeta);
        elfen_hat.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        ItemStack elfen_chest = ItemCreator.itemCreate(Material.LEATHER_CHESTPLATE, "Hemd des Weihnachtselfs");
        LeatherArmorMeta elfenChestMeta = (LeatherArmorMeta) elfen_chest.getItemMeta();
        elfenChestMeta.setColor(Color.fromRGB(0, 179, 44));
        elfen_chest.setItemMeta(elfenChestMeta);
        elfen_chest.addUnsafeEnchantment(Enchantment.THORNS, 2);
        ItemStack elfen_pants = ItemCreator.itemCreate(Material.LEATHER_LEGGINGS, "Hose des Weihnachtselfs");
        LeatherArmorMeta elfenPantsMeta = (LeatherArmorMeta) elfen_pants.getItemMeta();
        elfenPantsMeta.setColor(Color.fromRGB(0, 179, 44));
        elfen_pants.setItemMeta(elfenPantsMeta);
        elfen_pants.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 2);
        ItemStack elfen_boots = ItemCreator.itemCreate(Material.LEATHER_BOOTS, "Schuhe des Weihnachtselfs");
        LeatherArmorMeta elfenBootsMeta = (LeatherArmorMeta) elfen_boots.getItemMeta();
        elfenBootsMeta.setColor(Color.fromRGB(0, 179, 44));
        elfen_boots.setItemMeta(elfenBootsMeta);
        elfen_boots.addUnsafeEnchantment(Enchantment.FROST_WALKER, 2);
        weihnachtself.addItem(0, getPotionItemStack(Material.SPLASH_POTION, PotionType.INSTANT_HEAL, false));
        weihnachtself.addItem(1, getPotionItemStack(Material.SPLASH_POTION, PotionType.INSTANT_HEAL, false));
        weihnachtself.addItem(2, getPotionItemStack(Material.SPLASH_POTION, PotionType.INSTANT_DAMAGE, false));
        weihnachtself.addItem(3, getPotionItemStack(Material.SPLASH_POTION, PotionType.INSTANT_DAMAGE, false));
        weihnachtself.addItem(EquipmentSlot.HEAD, elfen_hat);
        weihnachtself.addItem(EquipmentSlot.CHEST, elfen_chest);
        weihnachtself.addItem(EquipmentSlot.LEGS, elfen_pants);
        weihnachtself.addItem(EquipmentSlot.FEET, elfen_boots);

    }

    private static ItemStack getPotionItemStack(Material potionType, PotionType type, boolean higherLevel) {
        ItemStack potion = new ItemStack(potionType, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.getCustomEffects();
        meta.setBasePotionData(new PotionData(type, false, higherLevel));
        potion.setItemMeta(meta);
        return potion;
    }

    public static KitManager getManager(BiomiaPlayer bp) {
        return playerKits.computeIfAbsent(bp, kits -> new KitManager(bp));
    }

    private void loadKits() {

        String name = MySQL.executeQuery("SELECT * FROM `LSSkyWarsKit` WHERE uuid = '" + bp.getPlayer().getUniqueId() + "'", "kit", MySQL.Databases.biomia_db);

        // Add Standard Kit
        availableKits.add(standardKit);

        for (int i : SkyWarsKit.getAvailableKit(bp)) {
            Kit k = allKits.get(i);
            if (k != null)
                availableKits.add(k);
        }

        // Add Kits with Permission
        for (Kit allKits : allKits.values()) {
            if (!availableKits.contains(allKits) && (bp.getPlayer().hasPermission("biomia.sw.kit." + allKits.getName()) || bp.getPlayer().hasPermission("biomia.sw.kit.*")))
                availableKits.add(allKits);
            if (allKits.getName().equals(name) && availableKits.contains(allKits))
                selectedKit = allKits;
        }
    }

    public void showInventory(Kit kit) {
        bp.getPlayer().openInventory(kit.getDemoInv());
    }

    public void openKitMenu() {

        inv.clear();
        for (Kit allKits : allKits.values()) {
            if (allKits.isShowable() || availableKits.contains(allKits))
                inv.addItem(allKits.getIcon());
        }
        inv.setItem(inv.getSize() - 5, selectedKit.getIcon());
        bp.getPlayer().openInventory(inv);
    }

    public boolean buy(Kit k) {

        if (!availableKits.contains(k)) {
            if (bp.getCoins() >= k.getPrice()) {
                boolean b = SkyWarsKit.addKit(bp, k.getID());
                if (b) {
                    bp.takeCoins(k.getPrice());
                    bp.getPlayer().sendMessage(Messages.kitPurchased.replaceAll("%k", k.getName()));
                    availableKits.add(k);
                } else
                    bp.getPlayer().sendMessage(Messages.errorWhilePurchasing.replaceAll("%k", k.getName()));
                return b;
            } else {
                bp.getPlayer().sendMessage(Messages.notEnoughCoins.replaceAll("%k", k.getName()));
                bp.getPlayer().sendMessage(
                        Messages.missingCoins.replaceAll("%k", k.getName()).replaceAll("%c", k.getPrice() - bp.getCoins() + ""));
            }
        } else {
            bp.getPlayer().sendMessage(Messages.alreadyPurchased.replaceAll("%k", k.getName()));
        }
        return false;
    }

    public void setKitInventory() {
        selectedKit.copy(bp.getPlayer());
    }

    public boolean selectSkyWarsKit(Kit k) {

        if (selectedKit.equals(k))
            return false;
        selectedKit = k;
        if (MySQL.executeQuery("SELECT * FROM `LSSkyWarsKit` WHERE uuid = '" + bp.getPlayer().getUniqueId() + "'", "kit", MySQL.Databases.biomia_db) != null)
            MySQL.executeUpdate("UPDATE LSSkyWarsKit SET `kit` = '" + k.getName() + "' WHERE uuid = '" + bp.getPlayer().getUniqueId() + "'", MySQL.Databases.biomia_db);
        else
            MySQL.executeUpdate("INSERT INTO `LSSkyWarsKit` (uuid,kit) Values('" + bp.getPlayer().getUniqueId() + "', '" + k.getName() + "')", MySQL.Databases.biomia_db);
        return true;
    }

    public ArrayList<Kit> getAvailableKits() {
        return availableKits;
    }
}
