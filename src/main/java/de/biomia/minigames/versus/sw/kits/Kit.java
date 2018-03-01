package de.biomia.minigames.versus.sw.kits;

import de.biomia.general.messages.SkyWarsItemNames;
import de.biomia.general.messages.SkyWarsMessages;
import de.biomia.api.Biomia;
import de.biomia.api.itemcreator.ItemCreator;
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
    private Inventory demoInv;

    Kit(String name, int id, int preis, ItemStack icon, boolean showable) {
        for (int i = 0; i < 26; i++)
            contents.add(ItemCreator.itemCreate(Material.AIR));
        this.name = name;
        this.price = preis;
        this.icon = icon;
        this.setShowable(showable);
        this.setId(id);
        KitManager.allKits.put(id, this);
        ItemMeta meta = getIcon().getItemMeta();
        meta.setDisplayName("\u00A7a" + getName());
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

    public void copy(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();

        if (getName().equals("Assassin"))
            p.setHealthScale(10);

        for (int i = 0; i < 26; i++)
            inv.setItem(i, contents.get(i));

        inv.setChestplate(chestplate);
        inv.setBoots(boots);
        inv.setHelmet(helmet);
        inv.setLeggings(leggings);
        inv.setItemInOffHand(offHand);
    }

    public Inventory getDemoInv() {

        if (demoInv == null) {
            demoInv = Bukkit.createInventory(null, 36, SkyWarsMessages.demoInventory.replaceAll("%k", getName()));

            for (int i = 0; i < 26; i++)
                demoInv.setItem(35 - i, contents.get(i));

            demoInv.setItem(0, helmet);
            demoInv.setItem(1, chestplate);
            demoInv.setItem(2, leggings);
            demoInv.setItem(3, boots);
            demoInv.setItem(8, offHand);
        }

        return demoInv;

    }

    public Inventory getSetupInv(Player p) {

        if (setupInventorys.containsKey(p))
            return setupInventorys.get(p);

        Inventory inv = Bukkit.createInventory(null, 9, SkyWarsMessages.setupInventory.replaceAll("%k", getName()));

        ItemStack pay = ItemCreator.itemCreate(Material.GOLD_INGOT, SkyWarsItemNames.purchaseKit);
        ItemStack select = ItemCreator.itemCreate(Material.ARMOR_STAND, SkyWarsItemNames.selectKit);
        ItemStack look = ItemCreator.itemCreate(Material.THIN_GLASS, SkyWarsItemNames.showKit);

        ItemMeta meta = pay.getItemMeta();

        ArrayList<Kit> playerAvailableKits = KitManager.getManager(Biomia.getBiomiaPlayer(p)).getAvailableKits();
        ArrayList<String> list = new ArrayList<>();
        if (playerAvailableKits != null) {
            if (playerAvailableKits.contains(this)) {
                list.addAll(SkyWarsItemNames.purchasedKitLore);
            } else {
                list.addAll(SkyWarsItemNames.notPurchasedKitLore);
            }
            int i = 0;
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

    public String getDisplayName() {
        return "\u00A7a" + getName();
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

    public int getPrice() {
        return price;
    }
}