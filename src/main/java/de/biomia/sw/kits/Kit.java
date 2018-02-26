package de.biomia.sw.kits;

import de.biomia.sw.messages.ItemNames;
import de.biomia.sw.messages.Messages;
import de.biomia.sw.var.Variables;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.achievements.statEvents.skywars.KitBuyEvent;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.api.tools.SkyWarsKit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Kit {

    private final ItemStack icon;
    private final HashMap<Player, Inventory> setupInventorys = new HashMap<>();
    private final String name;
    private final int price;
    private final ArrayList<ItemStack> contents = new ArrayList<>();
    private boolean isShowable;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private int ID = -1;
    private ItemStack offHand;

    Kit(String name, int id, int preis, ItemStack icon, boolean showable) {
        for (int i = 0; i < 26; i++)
            contents.add(ItemCreator.itemCreate(Material.AIR));
        this.name = name;
        this.price = preis;
        this.icon = icon;
        this.setShowable(showable);
        this.setId(id);
        Variables.kits.put(id, this);
        ItemMeta meta = getIcon().getItemMeta();
        meta.setDisplayName("00A7a" + getName());
        getIcon().setItemMeta(meta);
    }

    public void addItem(int slot, ItemStack itemStack) {
        contents.set(slot, itemStack);
    }

    public void setDescription(List<String> lore) {
        ItemMeta meta = icon.getItemMeta();
        meta.setLore(lore);
        icon.setItemMeta(meta);
    }

    public void addItem(EquipmentSlot slot, ItemStack itemStack) {

        switch (slot) {
            case HEAD:
                helmet = itemStack;
                break;
            case CHEST:
                chestplate = itemStack;
                break;
            case LEGS:
                leggings = itemStack;
                break;
            case FEET:
                boots = itemStack;
                break;
            case OFF_HAND:
                offHand = itemStack;
                break;
            default:
                break;
        }
    }

    public void copy(PlayerInventory inv, Player p) {

        if (getName().equals("Assassin")) {
            p.setHealthScale(10);
        }

        inv.clear();

        for (int i = 0; i < 26; i++) {
            inv.setItem(i, contents.get(i));
        }

        if (chestplate != null)
            inv.setChestplate(chestplate);
        if (boots != null)
            inv.setBoots(boots);
        if (helmet != null)
            inv.setHelmet(helmet);
        if (leggings != null)
            inv.setLeggings(leggings);
        if (offHand != null)
            inv.setItemInOffHand(offHand);

        if (p != null)
            p.updateInventory();
    }

    public Inventory getDemoInv() {

        Inventory inv = Bukkit.createInventory(null, 36, Messages.demoInventory.replaceAll("%k", getName()));

        for (int i = 0; i < 26; i++) {
            inv.setItem(35 - i, contents.get(i));
        }

        inv.setItem(0, helmet);
        inv.setItem(1, chestplate);
        inv.setItem(2, leggings);
        inv.setItem(3, boots);
        inv.setItem(8, offHand);

        return inv;

    }

    public boolean buy(BiomiaPlayer bp) {

        if (!Variables.availableKits.get(bp.getPlayer()).contains(this)) {
            if (bp.getCoins() >= price) {
                boolean b = SkyWarsKit.addKit(bp, getID());
                if (b) {
                    Bukkit.getPluginManager().callEvent(new KitBuyEvent(bp, getID()));
                    bp.takeCoins(price);
                    bp.getPlayer().sendMessage(Messages.kitPurchased.replaceAll("%k", getName()));
                    Variables.availableKits.get(bp.getPlayer()).add(this);
                } else
                    bp.getPlayer().sendMessage(Messages.errorWhilePurchasing.replaceAll("%k", getName()));
                return b;
            } else {
                bp.getPlayer().sendMessage(Messages.notEnoughCoins.replaceAll("%k", getName()));
                bp.getPlayer().sendMessage(
                        Messages.missingCoins.replaceAll("%k", getName()).replaceAll("%c", price - bp.getCoins() + ""));
            }
        } else {
            bp.getPlayer().sendMessage(Messages.alreadyPurchased.replaceAll("%k", getName()));
        }
        return false;
    }

    public Inventory getSetupInv(Player p) {

        if (setupInventorys.containsKey(p))
            return setupInventorys.get(p);

        Inventory inv = Bukkit.createInventory(null, 9, Messages.setupInventory.replaceAll("%k", getName()));

        ItemStack pay = ItemCreator.itemCreate(Material.GOLD_INGOT, ItemNames.purchaseKit);
        ItemStack select = ItemCreator.itemCreate(Material.ARMOR_STAND, ItemNames.selectKit);
        ItemStack look = ItemCreator.itemCreate(Material.THIN_GLASS, ItemNames.showKit);

        ItemMeta meta = pay.getItemMeta();

        if (Variables.availableKits.get(p) != null)
            if (Variables.availableKits.get(p).contains(this)) {
                int i = 0;
                ArrayList<String> list = new ArrayList<>(ItemNames.purchasedKitLore);
                for (String s : list) {
                    list.set(i, s.replaceAll("%c", price + ""));
                    i++;
                }
                meta.setLore(list);
            } else {
                int i = 0;
                ArrayList<String> list = new ArrayList<>(ItemNames.notPurchasedKitLore);
                for (String s : list) {
                    list.set(i, s.replaceAll("%c", price + ""));
                    i++;
                }
                meta.setLore(list);
            }

        pay.setItemMeta(meta);

        inv.setItem(2, pay);
        inv.setItem(4, select);
        inv.setItem(6, look);

        setupInventorys.put(p, inv);

        return inv;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public String getName() {
        return this.name;
    }

    public boolean isShowable() {
        return isShowable;
    }

    private void setShowable(boolean isShowable) {
        this.isShowable = isShowable;
    }

    public int getID() {
        return ID;
    }

    private void setId(int id) {
        this.ID = id;
    }

}