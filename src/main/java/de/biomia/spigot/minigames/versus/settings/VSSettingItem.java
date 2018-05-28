package de.biomia.spigot.minigames.versus.settings;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.versus.Versus;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class VSSettingItem {

    private static final ItemStack enabled = ItemCreator.itemCreate(Material.STAINED_GLASS, "\u00A7aAktiviert", (short) 5);
    private static final ItemStack disabled = ItemCreator.itemCreate(Material.STAINED_GLASS, "\u00A7cDeaktiviert", (short) 14);

    private final int itemSlot;
    private final int settingSlot;
    private final ItemStack item;
    private final int ID;
    private final boolean standard;
    private final VSGroup group;
    private final String name;

    public VSSettingItem(ItemStack item, int ID, int slot, boolean standard, VSGroup group, String name) {
        this.ID = ID;
        this.item = item;
        this.itemSlot = slot;
        this.settingSlot = slot + 9;
        this.standard = standard;
        this.group = group;
        this.name = name;

        VSSettings.putSettingItem(group.getMode(), ID, this);
    }

    public void setAsMap() {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("00A7c" + name);
        item.setItemMeta(meta);
    }

    public void inverse(BiomiaPlayer bp, VSGroupInventory inv) {
        ItemStack switcher;
        VSSettings settings = Versus.getInstance().getManager().getSettings(bp);
        settings.invertSetting(this);
        if (settings.isEnabled(this))
            switcher = enabled;
        else
            switcher = disabled;
        inv.setItem(switcher, settingSlot);
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getItemSlot() {
        return itemSlot;
    }

    public int getSettingSlot() {
        return settingSlot;
    }

    public boolean getStandard() {
        return standard;
    }

    public VSGroup getGroup() {
        return group;
    }

    public void setToInventory(VSGroupInventory inv) {
        inv.setItem(item, itemSlot);
        VSSettings settings = Versus.getInstance().getManager().getSettings(inv.getBiomiaPlayer());
        ItemStack switcher;
        if (settings.isEnabled(this))
            switcher = enabled;
        else
            switcher = disabled;

        inv.setItem(switcher, settingSlot);
    }
}
