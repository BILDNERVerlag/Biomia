package de.biomia.general.reportsystem.listener;

import de.biomia.Biomia;
import de.biomia.BiomiaPlayer;
import de.biomia.Main;
import de.biomia.general.reportsystem.*;
import de.biomia.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;

public class ClickEvent implements Listener {

    public static final HashMap<BiomiaPlayer, PlayerBan> waitForSetTime = new HashMap<>();
    public static final HashMap<BiomiaPlayer, PlayerBan> waitForIsPermBan = new HashMap<>();
    public static final HashMap<BiomiaPlayer, PlayerBan> waitForBanReason = new HashMap<>();
    public static HashMap<BiomiaPlayer, PlayerReport> waitForReportReason = new HashMap<>();

    private static int readSetTimeInventory(Inventory inventory) {

        int i = 11;

        int timeInSeconds = 0;

        while (i <= 15) {

            ItemStack is = inventory.getItem(i);
            String s = is.getItemMeta().getLore().get(0);
            int multiplicator = Integer.valueOf(s.substring(2, s.length()));

            switch (i) {
            case 11:
                timeInSeconds += 3600 * 24 * 365 * multiplicator;
                break;
            case 12:
                timeInSeconds += 3600 * 24 * 30 * multiplicator;
                break;
            case 13:
                timeInSeconds += 3600 * 24 * multiplicator;
                break;
            case 14:
                timeInSeconds += 3600 * multiplicator;
                break;
            case 15:
                timeInSeconds += 60 * multiplicator;
                break;
            }
            i++;
        }

        return timeInSeconds;
    }

    public static void openSetTimeInventory(BiomiaPlayer biomiaPlayer) {

        Inventory inv = Bukkit.createInventory(null, 27, "\u00A7cL\u00fcnge");

        int i = 0;

        ItemStack plus = ItemCreator.itemCreate(Material.STAINED_GLASS, "\u00A7a+", (byte) 5);
        ItemStack minus = ItemCreator.itemCreate(Material.STAINED_GLASS, "\u00A7c-", (byte) 14);

        boolean b;

        while (i < inv.getSize()) {

            b = true;
            ItemStack is = null;

            switch (i) {
            case 11:
                is = ItemCreator.itemCreate(Material.WOOL, "\u00A7dJahre");
                break;
            case 12:
                is = ItemCreator.itemCreate(Material.WOOL, "\u00A7dMonate");
                break;
            case 13:
                is = ItemCreator.itemCreate(Material.WOOL, "\u00A7dTage");
                break;
            case 14:
                is = ItemCreator.itemCreate(Material.WOOL, "\u00A7dStunden");
                break;
            case 15:
                is = ItemCreator.itemCreate(Material.WOOL, "\u00A7dMinuten");
                break;
            default:
                b = false;
                if (i > 1 && i < 7) {
                    inv.setItem(i, plus);
                } else if (i > 19 && i < 25) {
                    inv.setItem(i, minus);
                } else if (i == 26) {
                    inv.setItem(i, ItemCreator.itemCreate(Material.SPECTRAL_ARROW, "\u00A7cFertig"));
                }
                break;
            }
            if (b) {
                ItemMeta meta = is.getItemMeta();
                meta.setLore(Collections.singletonList("\u00A7c" + 0));
                is.setItemMeta(meta);
                inv.setItem(i, is);
            }
            i++;
        }

        biomiaPlayer.getPlayer().openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (e.getWhoClicked() instanceof Player) {

            Player p = (Player) e.getWhoClicked();
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

            if (e.getInventory().equals(Main.reportMenu)) {
                if (e.getCurrentItem() != null)
                    if (e.getCurrentItem().hasItemMeta())
                        if (e.getCurrentItem().getItemMeta().getDisplayName().equals("\u00A7cBug")) {

                            ReportManager.waitingForBugReason.add(p);
                            p.sendMessage(
                                    "\u00A76Du hast einen Bug gefunden? Dann gib einfach ein was das Problem ist! Wenn es ein richtiger Bug ist, bekommst du sogar eine Belohnung!");
                            p.closeInventory();

                        } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("\u00A7cSpieler")) {
                            ReportManager.waitingForName.add(p);
                            p.sendMessage(
                                    "\u00A76Du willst einen Spieler reporten? Dann gib einfach seinen Namen ein!");
                            p.closeInventory();
                        }
            } else if (e.getClickedInventory().equals(Main.grund)) {
                if (e.getCurrentItem() != null) {
                    e.setCancelled(true);
                    if (waitForBanReason.containsKey(bp)) {
                        PlayerBan ban = waitForBanReason.get(bp);
                        ban.setReason(Grund.valueOf(e.getCurrentItem().getItemMeta().getDisplayName()
                                .substring(2, e.getCurrentItem().getItemMeta().getDisplayName().length()).replace(' ', '_')
                                .toUpperCase()).name());
                        waitForBanReason.remove(bp);
                        p.closeInventory();
                    } else {
                        Grund grund = Grund.valueOf(e.getCurrentItem().getItemMeta().getDisplayName()
                                .substring(2, e.getCurrentItem().getItemMeta().getDisplayName().length()).replace(' ', '_')
                                .toUpperCase());
                        for (PlayerReport report : ReportManager.unfinishedReports) {
                            if (report.getReporterBiomiaID() == Biomia.getBiomiaPlayer(p).getBiomiaPlayerID()) {
                                report.finish(grund.name());
                                p.closeInventory();
                                if (ReportSQL.addPlayerReport(report)) {
                                    p.sendMessage("\u00A7aDer Spieler " + report.getReporteterName()
                                            + " wurde erfolgreich reportet wegen: " + Grund.toText(grund));
                                }
                                return;
                            }
                        }
                    }
                }
            } else if (e.getClickedInventory().getName().equals("\u00A7cL\u00fcnge")) {
                e.setCancelled(true);
                if (!waitForSetTime.containsKey(bp))
                    return;

                if (e.getSlot() == 26) {
                    PlayerBan ban = waitForSetTime.get(bp);
                    ban.setTime(readSetTimeInventory(e.getClickedInventory()));
                    ban.finish();
                    p.closeInventory();
                    waitForSetTime.remove(Biomia.getBiomiaPlayer((Player) e.getWhoClicked()));
                    return;
                }

                boolean plus;
                boolean isShift = e.getClick().isShiftClick();
                if (e.getSlot() > 1 && e.getSlot() < 7) {
                    plus = true;
                } else if (e.getSlot() > 19 && e.getSlot() < 25) {
                    plus = false;
                } else {
                    return;
                }

                int slot = plus ? e.getSlot() + 9 : e.getSlot() - 9;

                ItemStack is = e.getClickedInventory().getItem(slot);
                if (is != null) {
                    String s = is.getItemMeta().getLore().get(0);
                    int amount = Integer.valueOf(s.substring(2, s.length()));

                    if (amount <= 0 && !plus) {
                        return;
                    } else if (amount <= 10 && !plus && isShift) {
                        return;
                    }

                    int i = plus ? 1 : -1;
                    if (isShift)
                        i *= 10;
                    i += amount;

                    if (i != 0)
                        is.setAmount(i);

                    ItemMeta meta = is.getItemMeta();
                    meta.setLore(Collections.singletonList("\u00A7c" + i));
                    is.setItemMeta(meta);
                }
            } else if (e.getClickedInventory().getName().contains("Permanent")) {
                e.setCancelled(true);
                if (!waitForIsPermBan.containsKey(bp))
                    return;
                PlayerBan ban = waitForIsPermBan.get(bp);
                switch (e.getSlot()) {
                case 12:
                    ban.setPerm(true);
                    p.closeInventory();
                    break;
                case 14:
                    ban.setPerm(false);
                    break;
                default:
                    break;
                }
            }
        }
    }


}
