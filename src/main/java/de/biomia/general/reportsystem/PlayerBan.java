package de.biomia.general.reportsystem;

import de.biomia.BiomiaPlayer;
import de.biomia.general.reportsystem.listeners.Channel;
import de.biomia.general.reportsystem.listeners.ClickEvent;
import de.biomia.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class PlayerBan {

    private static Inventory isPermInv;

    private final int biomiaID;
    private final BiomiaPlayer bp;
    private int time;
    private String reason;

    public PlayerBan(BiomiaPlayer bp, int IDtoBan) {
        this.biomiaID = IDtoBan;
        this.bp = bp;
        ClickEvent.waitForBanReason.put(bp, this);
        openBugReasonInv();
    }

    public int getToBanID() {
        return biomiaID;
    }

    public BiomiaPlayer getBiomiaPlayer() {
        return bp;
    }

    public void setPerm(boolean perm) {
        ClickEvent.waitForIsPermBan.remove(bp);
        time = -1;
        if (perm) {
            finish();
        } else {
            ClickEvent.waitForSetTime.put(bp, this);
            ClickEvent.openSetTimeInventory(bp);
        }
    }

    public void finish() {
        Channel.send(this);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int timeInSeconds) {
        this.time = timeInSeconds;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        if (reason.equals(Grund.ANDERER_GRUND.name())) {
            ReportManager.waitForCostumReason.put(bp, this);
            bp.getPlayer().sendMessage("\u00A7bBitte gib den Grund in den Chat ein!");
        } else {
            this.reason = reason;
            ClickEvent.waitForIsPermBan.put(bp, this);
            openIsPermInv();
        }
    }

    private void openIsPermInv() {
        if (isPermInv == null) {
            isPermInv = Bukkit.createInventory(null, 27, "\u00A7bPermanenter Ban?");
            isPermInv.setItem(12, ItemCreator.itemCreate(Material.STAINED_GLASS, "\u00A7aJa", (short) 5));
            isPermInv.setItem(14, ItemCreator.itemCreate(Material.STAINED_GLASS, "\u00A7cNein", (short) 14));
        }
        bp.getPlayer().openInventory(isPermInv);
    }

    private void openBugReasonInv() {
        bp.getPlayer().openInventory(ReportManager.grund);
    }
}
