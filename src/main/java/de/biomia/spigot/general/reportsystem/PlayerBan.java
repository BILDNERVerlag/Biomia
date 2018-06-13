package de.biomia.spigot.general.reportsystem;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.listeners.ReportListener;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.universal.Grund;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class PlayerBan {

    public static final String permInvName = "§bPermanenter Ban?";
    private static Inventory isPermInv;

    private final int biomiaID;
    private final BiomiaPlayer bp;
    private int time;
    private String reason;

    public PlayerBan(BiomiaPlayer bp, int IDtoBan) {
        this.biomiaID = IDtoBan;
        this.bp = bp;
        ReportListener.waitForBanReason.put(bp, this);
        openBugReasonInv();
    }

    public int getToBanID() {
        return biomiaID;
    }

    public BiomiaPlayer getBiomiaPlayer() {
        return bp;
    }

    public void setPerm(boolean perm) {
        ReportListener.waitForIsPermBan.remove(bp);
        time = -1;
        if (perm) {
            finish();
        } else {
            ReportListener.waitForSetTime.put(bp, this);
            ReportListener.openSetTimeInventory(bp);
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
            ReportListener.waitForBanReason.remove(bp);
            ReportManager.waitForCustomReason.put(bp, this);
            bp.getPlayer().sendMessage(Messages.format("Bitte gib den Grund in den Chat ein!"));
        } else {
            this.reason = reason;
            ReportListener.waitForBanReason.remove(bp);
            ReportListener.waitForIsPermBan.put(bp, this);
            openIsPermInv();
        }
    }

    private void openIsPermInv() {
        if (isPermInv == null) {
            isPermInv = Bukkit.createInventory(null, 27, permInvName);
            isPermInv.setItem(12, ItemCreator.itemCreate(Material.STAINED_GLASS, "§aJa", (short) 5));
            isPermInv.setItem(14, ItemCreator.itemCreate(Material.STAINED_GLASS, "§cNein", (short) 14));
        }
        bp.getPlayer().openInventory(isPermInv);
    }

    private void openBugReasonInv() {
        bp.getPlayer().openInventory(ReportManager.grund);
    }
}
