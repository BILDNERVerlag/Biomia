package de.biomia.plugin.reportsystem;

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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

class ReporterInventoryOfPlayer implements Listener {

    private final BiomiaPlayer bp;
    private final int biomiaIDReportedPlayer;
    private final Inventory inv;
    private final ArrayList<ItemStack> items = new ArrayList<>();
    private final ScrolableReportInventory master;
    private ItemStack next;
    private ItemStack back;
    private ItemStack home;
    private int side = 0;

    public ReporterInventoryOfPlayer(BiomiaPlayer bp, int biomiaIDReportedPlayer, ScrolableReportInventory master) {
        this.bp = bp;
        this.master = master;
        this.biomiaIDReportedPlayer = biomiaIDReportedPlayer;
        String nameReportedPlayer = BiomiaPlayer.getName(biomiaIDReportedPlayer);
        inv = Bukkit.createInventory(null, 27, "§d" + nameReportedPlayer + "'s Report Profile");
        Bukkit.getPluginManager().registerEvents(this, Main.plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (bp.equals(Biomia.getBiomiaPlayer((Player) e.getWhoClicked())))
            if (e.getInventory().equals(inv)) {
                e.setCancelled(true);
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    if (e.getCurrentItem().equals(back)) {
                        displaySide(side - 1);
                    } else if (e.getCurrentItem().equals(next)) {
                        displaySide(side + 1);
                    } else if (e.getCurrentItem().equals(home)) {
                        master.openInventory();
                    }
                }
            }
    }

    private void displaySide(int i) {
        inv.clear();
        side = i;
        if (master != null)
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

    public void openInventory() {

        items.clear();
        for (PlayerReport playerReport : ReportSQL.getAllReportsOf(biomiaIDReportedPlayer)) {
            String reporter = playerReport.getReporterName();

            ItemStack stack = ItemCreator.headWithSkin(reporter, "§c" + reporter);
            ItemMeta meta = stack.getItemMeta();
            meta.setLore(Arrays.asList("", "Grund: " + playerReport.getGrund(), ""));
            stack.setItemMeta(meta);
            items.add(stack);
        }
        displaySide(0);
        bp.getPlayer().openInventory(inv);
    }
}
