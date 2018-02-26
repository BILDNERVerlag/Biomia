package de.biomia.versus.vs.settings;

import de.biomia.versus.sw.kits.KitManager;
import de.biomia.versus.sw.messages.ItemNames;
import de.biomia.versus.vs.main.VSManager;
import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.api.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VSGroupInventory implements Listener {

    private static final ItemStack backItem = ItemCreator.itemCreate(Material.STICK, "\u00A7cZur\u00fcck");
    private static final int backItemSlot = 22;
    private final BiomiaPlayer bp;
    private final Inventory inv;
    private final VSGroup group;

    VSGroupInventory(BiomiaPlayer bp, VSGroup group) {
        this.bp = bp;
        this.group = group;
        this.inv = Bukkit.createInventory(null, 27, group.getTitle());
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
        for (VSSettingItem item : group.getItems())
            item.setToInventory(this);
        for (VSGroup g : group.getGroups())
            if (!g.isDeaktivateable())
                inv.setItem(g.getSlot(), g.getIcon());
        if (group.getMode() != VSManager.VSMode.Main) {
            inv.setItem(backItemSlot, backItem);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) e.getWhoClicked());
        if (bp.equals(this.bp)) {
            if (e.getClickedInventory() != null && e.getClickedInventory().equals(inv)) {
                interactAtItemBySlot(e.getSlot());
                e.setCancelled(true);
            }
        }
    }

    public void setItem(ItemStack is, int slot) {
        inv.setItem(slot, is);
    }

    public void openInventory() {
        bp.getPlayer().openInventory(inv);
    }

    private void interactAtItemBySlot(int slot) {

        if (slot == backItemSlot && group.getMode() != VSManager.VSMode.Main) {
            group.getMainGroup().getInventory(bp).openInventory();
            return;
        }

        for (VSGroup g : group.getGroups()) {
            if (g.getSlot() == slot) {
                if (g.getTitle().equals(ItemNames.kitItemName))
                    KitManager.getManager(bp).openKitMenu();
                else
                    g.getInventory(bp).openInventory();
                return;
            }
        }
        for (VSSettingItem item : group.getItems()) {
            if (item.getSettingSlot() == slot || item.getItemSlot() == slot) {
                item.inverse(bp, this);
                return;
            }
        }
    }

    public BiomiaPlayer getBiomiaPlayer() {
        return bp;
    }
}