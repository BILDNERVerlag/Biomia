package de.biomia.spigot.server.demoserver.teleporter;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.Main;
import de.biomia.spigot.server.demoserver.Weltenlabor;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ScrollingInventory implements Listener {
    private final Inventory inv;
    private final ArrayList<ItemStack> items = new ArrayList<>();
    private final int items_per_side;
    private ItemStack next;
    private ItemStack back;
    private int side = 0;
    private final Player player;

    public ScrollingInventory(Player player) {
        this.player = player;
        inv = Bukkit.createInventory(null, 36, "\u00A7eWähle dein Ziel!");
        this.items_per_side = 27;
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
    }

    private void clearItems() {
        inv.clear();
        items.clear();
    }

    private void displaySide(int i) {
        clearItems();
        side = i;
        if (items.size() - side - items_per_side > side * items_per_side)
            setNext();
        if (side > 0)
            setBack();
        int from = side * items_per_side;
        int to = (side + 1) * items_per_side;
        int a = 0;
        for (ItemStack is : items.subList(from, (to > items.size() ? items.size() : to))) {
            inv.setItem(a, is);
            a++;
        }

    }

    private void setNext() {
        if (next == null)
            next = ItemCreator.itemCreate(Material.BLAZE_ROD, "\u00A7aNächste Seite");
        inv.setItem(inv.getSize() - 2, next);
    }

    private void setBack() {
        if (back == null)
            back = ItemCreator.itemCreate(Material.STICK, "\u00A7aLetzte Seite");
        inv.setItem(inv.getSize() - 8, back);
    }

    public void openInventorry() {
        clearItems();
        ((Weltenlabor) Biomia.getServerInstance()).getBauten().forEach(each -> items.add(each.getItem()));
        displaySide(0);
        player.openInventory(inv);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().equals(inv))
            HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()) {
                for (Bauten b : ((Weltenlabor) Biomia.getServerInstance()).getBauten())
                    if (b.getName().equals(e.getCurrentItem().getItemMeta().getDisplayName())) {
                        e.getWhoClicked().teleport(b.getLoc());
                        return;
                    }
                if (e.getCurrentItem().equals(back)) {
                    displaySide(side - 1);
                } else if (e.getCurrentItem().equals(next)) {
                    displaySide(side + 1);
                }
            }
        }
    }
}
