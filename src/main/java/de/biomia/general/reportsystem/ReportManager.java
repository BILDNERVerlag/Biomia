package de.biomia.general.reportsystem;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.biomia.BiomiaPlayer;
import de.biomia.general.reportsystem.listener.Channel;
import de.biomia.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportManager {

    public static Inventory reportMenu;
    public static Inventory grund;


    public static final ArrayList<PlayerReport> plReports = new ArrayList<>();

    public static final ArrayList<PlayerReport> unfinishedReports = new ArrayList<>();
    public static final ArrayList<Player> waitingForBugReason = new ArrayList<>();
    public static final ArrayList<Player> waitingForName = new ArrayList<>();
    private static final HashMap<BiomiaPlayer, ScrolableReportInventory> currentReportsMenu = new HashMap<>();

    static {
        ReportManager.reportMenu = Bukkit.createInventory(null, 9, "\u00A7eREPORT MEN\u00fc");
        ItemStack bug = ItemCreator.itemCreate(Material.BARRIER, "\u00A7cBug");
        ItemStack spieler = ItemCreator.headWithSkin("DerJulsn", "\u00A7cSpieler");
        ReportManager.reportMenu.setItem(3, bug);
        ReportManager.reportMenu.setItem(5, spieler);

        ReportManager.grund = Bukkit.createInventory(null, 18, "\u00A7eGRUND");
        ReportManager.grund.setItem(2, ItemCreator.itemCreate(Material.ELYTRA, "\u00A7cFlyHack"));
        ReportManager.grund.setItem(3, ItemCreator.itemCreate(Material.DIAMOND, "\u00A7cNoSlowdown"));
        ReportManager.grund.setItem(4, ItemCreator.itemCreate(Material.IRON_SWORD, "\u00A7cKillaura"));
        ReportManager.grund.setItem(5, ItemCreator.itemCreate(Material.LEATHER_BOOTS, "\u00A7cSpeedHack"));
        ReportManager.grund.setItem(6, ItemCreator.itemCreate(Material.PAPER, "\u00A7cSonstiger Hack"));
        ReportManager.grund.setItem(11, ItemCreator.itemCreate(Material.TNT, "\u00A7cGriefing"));
        ReportManager.grund.setItem(12, ItemCreator.itemCreate(Material.BARRIER, "\u00A7cSpamming"));
        ReportManager.grund.setItem(13, ItemCreator.itemCreate(Material.RAW_FISH, "\u00A7cTrolling"));
        ReportManager.grund.setItem(14, ItemCreator.itemCreate(Material.BONE, "\u00A7cBeleidigung"));
        ReportManager.grund.setItem(15, ItemCreator.itemCreate(Material.BOOK, "\u00A7cAnderer Grund"));
    }

    // gets accessed remotely via BungeeCord
    public static void sendReport(PlayerReport pr) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Report");
        out.writeInt(pr.getReporteterBiomiaPlayer().getBiomiaPlayerID());
        out.writeInt(pr.getReporterBiomiaPlayer().getBiomiaPlayerID());
        out.writeUTF(pr.getGrund());
        Channel.send(out);
    }

    public static void openScrollableInventory(BiomiaPlayer bp) {
        ScrolableReportInventory reportInventory = currentReportsMenu.computeIfAbsent(bp, inventory -> new ScrolableReportInventory(bp));
        reportInventory.openInventory();
    }

    public static void removeReports(ArrayList<PlayerReport> playerReports) {
        ReportSQL.removeBugReport(playerReports.get(0).getReporteterBiomiaPlayer().getBiomiaPlayerID());
        playerReports.forEach(playerReports::remove);
    }

    public static ArrayList<PlayerReport> getReports(int biomiaID) {
        final ArrayList<PlayerReport> reports = new ArrayList<>();
        plReports.forEach(each -> {
            if (each.getReporteterBiomiaPlayer().getBiomiaPlayerID() == biomiaID) {
                reports.add(each);
            }
        });
        return reports;
    }

}
