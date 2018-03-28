package de.biomia.spigot.minigames.versus.settings;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.GameType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class VSGroup {

    private final HashMap<BiomiaPlayer, VSGroupInventory> inventory = new HashMap<>();
    private final ArrayList<VSSettingItem> settingItems = new ArrayList<>();
    private final ArrayList<VSGroup> settingGroups = new ArrayList<>();
    private final GameType mode;
    private final String titel;
    private final int slot;
    private final ItemStack icon;
    private final boolean deaktivateable;
    private final VSGroup mainGroup;

    public VSGroup(GameType mode, String title, ItemStack is, int slot, boolean deaktivateable, VSGroup mainGroup) {
        this.mode = mode;
        this.titel = title;
        this.slot = slot;
        this.icon = is;
        this.deaktivateable = deaktivateable;
        this.mainGroup = mainGroup;
    }

    public VSGroup getMainGroup() {
        return mainGroup;
    }

    public VSGroupInventory getInventory(BiomiaPlayer bp) {
        return inventory.computeIfAbsent(bp, b -> new VSGroupInventory(bp, this));
    }

    public VSGroup registerGroup(GameType mode, ItemStack item, String title, int slot) {
        VSGroup group = new VSGroup(mode, title, item, slot, false, this);
        settingGroups.add(group);
        return group;
    }

    public VSGroup registerDeaktivatableGroup(GameType mode, ItemStack item, String title, int slot, int id, boolean standard) {
        VSGroup group = new VSGroup(mode, title, item, slot, true, this);
        settingGroups.add(group);
        registerSetting(new VSSettingItem(item, id, slot, standard, group, title));
        return group;
    }

    public ArrayList<VSSettingItem> getItems() {
        return settingItems;
    }

    public void registerSetting(VSSettingItem item) {
        settingItems.add(item);
    }

    public ArrayList<VSGroup> getGroups() {
        return settingGroups;
    }

    public GameType getMode() {
        return mode;
    }

    public String getTitle() {
        return titel;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public boolean isDeaktivateable() {
        return deaktivateable;
    }
}
