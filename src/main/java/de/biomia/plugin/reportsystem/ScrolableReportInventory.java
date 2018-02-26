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

class ScrolableReportInventory implements Listener {

    private final BiomiaPlayer bp;
    private final Inventory inv;
    private final ArrayList<ItemStack> items = new ArrayList<>();
    private ItemStack next;
    private ItemStack back;
    private int side = 0;
    private InformationInventory banInformationInv;

    public ScrolableReportInventory(BiomiaPlayer bp) {
        this.bp = bp;
        inv = Bukkit.createInventory(null, 27, "00A7dUnbearbeitete Reports");
        Bukkit.getPluginManager().registerEvents(this, Main.plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (bp.equals(Biomia.getBiomiaPlayer((Player) e.getWhoClicked())))
            if (e.getClickedInventory().equals(inv)) {
                e.setCancelled(true);
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    if (e.getCurrentItem().equals(back)) {
                        displaySide(side - 1);
                        return;
                    } else if (e.getCurrentItem().equals(next)) {
                        displaySide(side + 1);
                        return;
                    }
                    String name = e.getCurrentItem().getItemMeta().getDisplayName();
                    name = name.substring(2, name.length());

                    if (e.getClick().isRightClick()) {
                        if (e.getClick().isShiftClick()) {

                            new ReporterInventoryOfPlayer(bp, BiomiaPlayer.getID(name), this).openInventory();

                        } else {

                            banInformationInv = new InformationInventory(bp, BiomiaPlayer.getID(name));
                            banInformationInv.openInventory();
                        }
                    } else if (e.getClick().isLeftClick()) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "sendbungee gtp " + bp.getPlayer().getName() + " " + name);
                    }

                }
            } else if (e.getClickedInventory().equals(banInformationInv.getInventory())) {
                if (e.getCurrentItem() != null) {
                    int backItemSlotInBanInformation = 26;
                    if (e.getSlot() == backItemSlotInBanInformation) {
                        openInventory();
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("00A7cReport Entfernen")) {
                        //TODO
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("00A7aReport Fertigstellen")) {
                        //TODO
                        new PlayerBan(bp, banInformationInv.getBiomiaID());
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("00A7cBannen")) {
                        new PlayerBan(bp, banInformationInv.getBiomiaID());
                    }
                }
            }


    }

    private void displaySide(int i) {
        inv.clear();
        side = i;
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

    public void openInventory() {

        items.clear();

        ArrayList<Integer> ids = new ArrayList<>();
        for (PlayerReport playerReport : ReportManager.plReports) {
            if (!ids.contains(playerReport.getReporteterBiomiaID())) {

                ids.add(playerReport.getReporteterBiomiaID());

                ItemStack stack = ItemCreator.headWithSkin(playerReport.getReporteterName(), "00A7c" + playerReport.getReporteterName());
                ItemMeta meta = stack.getItemMeta();
                meta.setLore(Arrays.asList("", "00A7r00A7bReport Level: 00A7c" + playerReport.getLevel(), "00A7r00A7bReportet von 00A7c" + playerReport.getReporterName(), "00A7r00A7bGrund: 00A7c" + playerReport.getGrund(), ""));
                stack.setItemMeta(meta);
                items.add(stack);
            }
        }
        displaySide(0);
        bp.getPlayer().openInventory(inv);
    }
}
