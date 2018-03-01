package de.biomia.api.cosmetics;

import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

class CosmeticInventory implements Listener {

    private final BiomiaPlayer bp;
    private final ArrayList<CosmeticItem> cosmeticItems;
    private final Inventory inv;
    private CosmeticGroup group;
    private final ArrayList<ItemStack> items = new ArrayList<>();
    private ItemStack next;
    private ItemStack back;
    private ItemStack remove;
    private ItemStack home;
    private int side = 0;

    @SuppressWarnings("unchecked")
    public CosmeticInventory(ArrayList<? super CosmeticItem> items, BiomiaPlayer bp) {
        this.bp = bp;
        this.cosmeticItems = (ArrayList<CosmeticItem>) items;
        inv = Bukkit.createInventory(null, 27, "\u00A75Cosmetics");
        Bukkit.getPluginManager().registerEvents(this, Main.plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();

            if (e.getInventory().equals(inv)) {
                e.setCancelled(true);
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {

                    for (CosmeticItem item : cosmeticItems) {
                        if (item.getItem().getItemMeta().getDisplayName()
                                .equals(e.getCurrentItem().getItemMeta().getDisplayName())) {
                            item.use(Biomia.getBiomiaPlayer((Player) e.getWhoClicked()));
                            p.closeInventory();
                        }
                    }
                    if (e.getCurrentItem().equals(back)) {
                        displaySide(side - 1);
                    } else if (e.getCurrentItem().equals(next)) {
                        displaySide(side + 1);
                    } else if (e.getCurrentItem().equals(remove)) {
                        group.remove(bp);
                    } else if (e.getCurrentItem().equals(home)) {
                        Cosmetic.openMainInventory(bp);
                    }
                }
            }
        }
    }

    private void displaySide(int i) {
        inv.clear();
        side = i;

        setRemove();
        setHome();
        int items_per_side = 18;
        if (items.size() - side - items_per_side > side * items_per_side)
            setNext();
        if (side > 0)
            setBack();
        int from = side * items_per_side;
        int to = (side + 1) * items_per_side;
        int actualItem = 0;
        for (ItemStack is : items.subList(from, (to > items.size() ? items.size() : to))) {
            inv.setItem(actualItem, is);
            actualItem++;
        }
    }

    private void setRemove() {
        if (remove == null)
            remove = ItemCreator.itemCreate(Material.BARRIER, "\u00A7cEntfernen");
        inv.setItem(inv.getSize() - 5, remove);
    }

    private void setNext() {
        if (next == null)
            next = ItemCreator.itemCreate(Material.BLAZE_ROD, "\u00A7aN\u00e4chste Seite");
        inv.setItem(inv.getSize() - 3, next);
    }

    private void setBack() {
        if (back == null)
            back = ItemCreator.itemCreate(Material.STICK, "\u00A7aLetzte Seite");
        inv.setItem(inv.getSize() - 7, back);
    }

    private void setHome() {
        if (home == null)
            home = ItemCreator.itemCreate(Material.GOLDEN_CARROT, "\u00A7aZur\u00fcck");
        inv.setItem(inv.getSize() - 9, home);
    }

    public void openInventory(CosmeticGroup group) {

        this.group = group;
        items.clear();
        for (CosmeticItem cosmeticitem : cosmeticItems)
            if (cosmeticitem.getGroup() == group.getGroup()) {
                int limit = Cosmetic.getLimit(bp, cosmeticitem.getID());
                if (limit != -1) {
                    ItemStack is = cosmeticitem.getItem().clone();
                    is.setAmount(limit);
                    items.add(is);
                } else
                    items.add(cosmeticitem.getItem());
            }
        displaySide(0);
        bp.getPlayer().openInventory(inv);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public void removeItem(int id) {
        cosmeticItems.remove(Cosmetic.getItems().get(id));
    }

    public void addItem(int id) {
        cosmeticItems.add((CosmeticItem) Cosmetic.getItems().get(id));
    }
}
