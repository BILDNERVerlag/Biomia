package de.biomia.spigot.general.reportsystem;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.tools.ItemCreator;
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

    ScrolableReportInventory(BiomiaPlayer bp) {
        this.bp = bp;
        inv = Bukkit.createInventory(null, 27, "§bUnbearbeitete Reports");
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
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
                            new ReporterInventoryOfPlayer(bp, Biomia.getOfflineBiomiaPlayer(name).getBiomiaPlayerID(), this).openInventory();
                        } else {
                            banInformationInv = new InformationInventory(bp, Biomia.getOfflineBiomiaPlayer(name).getBiomiaPlayerID());
                        }
                    } else if (e.getClick().isLeftClick()) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "sendbungee gtp " + bp.getPlayer().getName() + " " + name);
                    }

                }
            } else if (banInformationInv != null && e.getClickedInventory().equals(banInformationInv.getInventory())) {
                if (e.getCurrentItem() != null) {
                    int backItemSlotInBanInformation = 26;
                    if (e.getSlot() == backItemSlotInBanInformation) {
                        openInventory();
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cReport Entfernen")) {
                        ReportManager.removeReports(ReportManager.getReports(banInformationInv.getBiomiaID()));
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aReport Fertigstellen")) {
                        ReportManager.removeReports(ReportManager.getReports(banInformationInv.getBiomiaID()));
                        new PlayerBan(bp, banInformationInv.getBiomiaID());
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cBannen")) {
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
            next = ItemCreator.itemCreate(Material.BLAZE_ROD, "§aNächste Seite");
        inv.setItem(inv.getSize() - 3, next);
    }

    private void setBack() {
        if (back == null)
            back = ItemCreator.itemCreate(Material.STICK, "§aLetzte Seite");
        inv.setItem(inv.getSize() - 7, back);
    }

    public void openInventory() {

        items.clear();

        ArrayList<Integer> ids = new ArrayList<>();
        for (PlayerReport playerReport : ReportManager.plReports) {
            if (!ids.contains(playerReport.getReporteterBiomiaPlayer().getBiomiaPlayerID())) {

                ids.add(playerReport.getReporteterBiomiaPlayer().getBiomiaPlayerID());

                ItemStack stack = ItemCreator.headWithSkin(playerReport.getReporteterBiomiaPlayer().getName(), "§c" + playerReport.getReporteterBiomiaPlayer().getName());
                ItemMeta meta = stack.getItemMeta();
                meta.setLore(Arrays.asList("", "§r§bReport Level: §c" + playerReport.getLevel(), "§r§bReportet von §c" + playerReport.getReporterBiomiaPlayer().getName(), "§r§bGrund: §c" + playerReport.getGrund(), ""));
                stack.setItemMeta(meta);
                items.add(stack);
            }
        }
        displaySide(0);
        bp.getPlayer().openInventory(inv);
    }
}
