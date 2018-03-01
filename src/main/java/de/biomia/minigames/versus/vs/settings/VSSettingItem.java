package de.biomia.minigames.versus.vs.settings;

import de.biomia.minigames.versus.vs.main.VSMain;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.itemcreator.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class VSSettingItem {

    private static final ItemStack enabled = ItemCreator.itemCreate(Material.STAINED_GLASS, "\u00A7aAktiviert", (short) 5);
    private static final ItemStack disabled = ItemCreator.itemCreate(Material.STAINED_GLASS, "\u00A7cDeaktiviert", (short) 14);

    private final int itemSlot;
    private final int settingSlot;
    private final ItemStack item;
    private final int id;
    private final boolean standard;
    private final VSGroup group;

    public VSSettingItem(ItemStack item, int id, int slot, boolean standard, VSGroup group) {
        this.id = id;
        this.item = item;
        this.itemSlot = slot;
        this.settingSlot = slot + 9;
        this.standard = standard;
        this.group = group;

        VSSettings.putSettingItem(group.getMode(), id, this);
    }

    public void inverse(BiomiaPlayer bp, VSGroupInventory inv) {
        ItemStack switcher;
        VSSettings settings = VSMain.getManager().getSettings(bp);
        settings.invertSetting(this);
        if (settings.getSetting(this))
            switcher = enabled;
        else
            switcher = disabled;
        inv.setItem(switcher, settingSlot);
    }

    public int getId() {
        return id;
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
        VSSettings settings = VSMain.getManager().getSettings(inv.getBiomiaPlayer());
        ItemStack switcher;
        if (settings.getSetting(this))
            switcher = enabled;
        else
            switcher = disabled;

        inv.setItem(switcher, settingSlot);
    }
}
