package de.biomia.spigot.specialEvents.schnitzelEvent;

import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.spigot.achievements.BiomiaStat;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

class MonsterPunkte {

    public static final ItemStack backpack = ItemCreator.itemCreate(Material.CHEST, "\u00A7cRucksack");

    private static final ItemStack helmetLVL1 = ItemCreator.itemCreate(Material.LEATHER_HELMET);
    private static final ItemStack helmetLVL2 = ItemCreator.itemCreate(Material.GOLD_HELMET);
    private static final ItemStack helmetLVL3 = ItemCreator.itemCreate(Material.IRON_HELMET);
    private static final ItemStack helmetLVL4 = ItemCreator.itemCreate(Material.DIAMOND_HELMET);
    private static final ItemStack helmetLVL5 = ItemCreator.itemCreate(Material.DIAMOND_HELMET);
    private static final ItemStack helmetLVL6 = ItemCreator.itemCreate(Material.DIAMOND_HELMET);
    private static final ItemStack helmetLVL7 = ItemCreator.itemCreate(Material.DIAMOND_HELMET);

    private static final ItemStack bootsLVL1 = ItemCreator.itemCreate(Material.LEATHER_BOOTS);
    private static final ItemStack bootsLVL2 = ItemCreator.itemCreate(Material.CHAINMAIL_BOOTS);
    private static final ItemStack bootsLVL3 = ItemCreator.itemCreate(Material.IRON_BOOTS);
    private static final ItemStack bootsLVL4 = ItemCreator.itemCreate(Material.DIAMOND_BOOTS);
    private static final ItemStack bootsLVL5 = ItemCreator.itemCreate(Material.DIAMOND_BOOTS);
    private static final ItemStack bootsLVL6 = ItemCreator.itemCreate(Material.DIAMOND_BOOTS);
    private static final ItemStack bootsLVL7 = ItemCreator.itemCreate(Material.DIAMOND_BOOTS);

    private static final ItemStack leggingsLVL1 = ItemCreator.itemCreate(Material.LEATHER_LEGGINGS);
    private static final ItemStack leggingsLVL2 = ItemCreator.itemCreate(Material.GOLD_LEGGINGS);
    private static final ItemStack leggingsLVL3 = ItemCreator.itemCreate(Material.IRON_LEGGINGS);
    private static final ItemStack leggingsLVL4 = ItemCreator.itemCreate(Material.IRON_LEGGINGS);
    private static final ItemStack leggingsLVL5 = ItemCreator.itemCreate(Material.DIAMOND_LEGGINGS);
    private static final ItemStack leggingsLVL6 = ItemCreator.itemCreate(Material.DIAMOND_LEGGINGS);
    private static final ItemStack leggingsLVL7 = ItemCreator.itemCreate(Material.DIAMOND_LEGGINGS);

    private static final ItemStack chestplateLVL1 = ItemCreator.itemCreate(Material.LEATHER_CHESTPLATE);
    private static final ItemStack chestplateLVL2 = ItemCreator.itemCreate(Material.CHAINMAIL_CHESTPLATE);
    private static final ItemStack chestplateLVL3 = ItemCreator.itemCreate(Material.IRON_CHESTPLATE);
    private static final ItemStack chestplateLVL4 = ItemCreator.itemCreate(Material.IRON_CHESTPLATE);
    private static final ItemStack chestplateLVL5 = ItemCreator.itemCreate(Material.DIAMOND_CHESTPLATE);
    private static final ItemStack chestplateLVL6 = ItemCreator.itemCreate(Material.DIAMOND_CHESTPLATE);
    private static final ItemStack chestplateLVL7 = ItemCreator.itemCreate(Material.DIAMOND_CHESTPLATE);

    private static final ItemStack weaponLVL1 = ItemCreator.itemCreate(Material.IRON_SPADE);
    private static final ItemStack weaponLVL2 = ItemCreator.itemCreate(Material.STONE_SWORD);
    private static final ItemStack weaponLVL3 = ItemCreator.itemCreate(Material.IRON_AXE);
    private static final ItemStack weaponLVL4 = ItemCreator.itemCreate(Material.IRON_SWORD);
    private static final ItemStack weaponLVL5 = ItemCreator.itemCreate(Material.DIAMOND_AXE);
    private static final ItemStack weaponLVL6 = ItemCreator.itemCreate(Material.DIAMOND_SWORD);
    private static final ItemStack weaponLVL7 = ItemCreator.itemCreate(Material.DIAMOND_SWORD);
    private static final ItemStack weapon_bow = ItemCreator.itemCreate(Material.BOW);
    private static final ItemStack weapon_arrow = ItemCreator.itemCreate(Material.ARROW);

    static {

    }

    private int points;
    private final OfflineBiomiaPlayer bp;

    MonsterPunkte(OfflineBiomiaPlayer bp, int points) {
        this.bp = bp;
        this.points = points;
    }

    public void giveInventory(int oldPoints) {

        ArrayList<ItemStack> weapons = null;
        ItemStack chestplate = null;
        ItemStack leggings = null;
        ItemStack boots = null;
        ItemStack helmet = null;

        if (points >= 300 && oldPoints < 300) {
            weapons = new ArrayList<>();
            weapons.add(weaponLVL7);
            weapons.add(weapon_arrow);
            weapons.add(weapon_bow);
            chestplate = chestplateLVL7;
            boots = bootsLVL7;
            leggings = leggingsLVL7;
            helmet = helmetLVL7;
        } else if (points >= 200 && oldPoints < 200) {
            weapons = new ArrayList<>();
            weapons.add(weaponLVL6);
            weapons.add(weapon_arrow);
            weapons.add(weapon_bow);
            chestplate = chestplateLVL6;
            boots = bootsLVL6;
            leggings = leggingsLVL6;
            helmet = helmetLVL6;
        } else if (points >= 150 && oldPoints < 150) {
            weapons = new ArrayList<>();
            weapons.add(weaponLVL5);
            chestplate = chestplateLVL5;
            boots = bootsLVL5;
            leggings = leggingsLVL5;
            helmet = helmetLVL5;
        } else if (points >= 100 && oldPoints < 100) {
            weapons = new ArrayList<>();
            weapons.add(weaponLVL4);
            chestplate = chestplateLVL4;
            boots = bootsLVL4;
            leggings = leggingsLVL4;
            helmet = helmetLVL4;
        } else if (points >= 50 && oldPoints < 50) {
            weapons = new ArrayList<>();
            weapons.add(weaponLVL3);
            chestplate = chestplateLVL3;
            boots = bootsLVL3;
            leggings = leggingsLVL3;
            helmet = helmetLVL3;
        } else if (points >= 20 && oldPoints < 20) {
            weapons = new ArrayList<>();
            weapons.add(weaponLVL2);
            chestplate = chestplateLVL2;
            boots = bootsLVL2;
            leggings = leggingsLVL2;
            helmet = helmetLVL2;
        } else if (oldPoints == -1) {
            weapons = new ArrayList<>();
            weapons.add(weaponLVL1);
            chestplate = chestplateLVL1;
            boots = bootsLVL1;
            leggings = leggingsLVL1;
            helmet = helmetLVL1;
        }


        if (boots != null) {

            bp.sendMessage("\u00A7cLevel Up! \u00A7bDu hast ein neues Inventar erhalten!");

            ItemMeta meta;

            meta = boots.getItemMeta();
            meta.setUnbreakable(true);
            boots.setItemMeta(meta);

            meta = leggings.getItemMeta();
            meta.setUnbreakable(true);
            leggings.setItemMeta(meta);

            meta = chestplate.getItemMeta();
            meta.setUnbreakable(true);
            chestplate.setItemMeta(meta);

            meta = helmet.getItemMeta();
            meta.setUnbreakable(true);
            helmet.setItemMeta(meta);

            PlayerInventory inv = bp.getBiomiaPlayer().getPlayer().getInventory();

            inv.clear();

            inv.setItem(8, backpack);
            inv.setItem(7, SchnitzelEvent.getInfoBook());

            inv.setBoots(boots);
            inv.setChestplate(chestplate);
            inv.setLeggings(leggings);
            inv.setHelmet(helmet);
            for (ItemStack is : weapons) {

                meta = is.getItemMeta();
                meta.setUnbreakable(true);
                is.setItemMeta(meta);

                inv.addItem(is);
            }
        }

    }

    public int getPoints() {
        return points;
    }

    public void addPoint(EntityType entityType) {
        Player p = bp.getBiomiaPlayer().getPlayer();

        int inc = 0;

        switch (entityType) {
            case ZOMBIE:
            case SKELETON:
            case SPIDER:
            case CREEPER:
                inc = 1;
                break;
            case ENDERMAN:
            case WITCH:
                inc = 2;
                break;
            case WITHER_SKELETON:
            case BLAZE:
                inc = 3;
                break;
        }
        if (inc != 0)
            BiomiaStat.SchnitzelMonsterKilled.increment(bp.getBiomiaPlayerID(), inc, null);

        int oldPoints = points;
        points += inc;

        p.setLevel(points);
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        giveInventory(oldPoints);
    }

    public void removePoints() {
        Player p = bp.getBiomiaPlayer().getPlayer();

        int oldPoints = points;
        points *= 0.95;
        BiomiaStat.SchnitzelDiedByMonster.increment(bp.getBiomiaPlayerID(), oldPoints - points, null);
        p.setLevel(points);
        giveInventory(oldPoints);
    }

    public String getName() {
        return bp.getName();
    }
}
