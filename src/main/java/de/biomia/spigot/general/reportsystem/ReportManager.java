package de.biomia.spigot.general.reportsystem;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ReportManager {

    public static final String reportMenuName = "§eREPORT MENÜ";
    public static final String grundName = "§eGRUND";
    private static final Inventory reportMenu = Bukkit.createInventory(null, 9, reportMenuName);
    public static final Inventory grund = Bukkit.createInventory(null, 18, grundName);
    static {
        reportMenu.setItem(3, ItemCreator.itemCreate(Material.BARRIER, "§cBug"));
        reportMenu.setItem(5, ItemCreator.headWithSkin("DerJulsn", "§cSpieler"));

        grund.setItem(2, ItemCreator.itemCreate(Material.ELYTRA, "§cFlyHack"));
        grund.setItem(3, ItemCreator.itemCreate(Material.DIAMOND, "§cNoSlowdown"));
        grund.setItem(4, ItemCreator.itemCreate(Material.IRON_SWORD, "§cKillaura"));
        grund.setItem(5, ItemCreator.itemCreate(Material.LEATHER_BOOTS, "§cSpeedHack"));
        grund.setItem(6, ItemCreator.itemCreate(Material.PAPER, "§cSonstiger Hack"));
        grund.setItem(11, ItemCreator.itemCreate(Material.TNT, "§cGriefing"));
        grund.setItem(12, ItemCreator.itemCreate(Material.BARRIER, "§cSpamming"));
        grund.setItem(13, ItemCreator.itemCreate(Material.RAW_FISH, "§cTrolling"));
        grund.setItem(14, ItemCreator.itemCreate(Material.BONE, "§cBeleidigung"));
        grund.setItem(15, ItemCreator.itemCreate(Material.BOOK, "§cAnderer Grund"));
    }

    public static final ArrayList<PlayerReport> plReports = new ArrayList<>();
    public static final ArrayList<PlayerReport> unfinishedReports = new ArrayList<>();
    public static final ArrayList<Player> waitingForBugReason = new ArrayList<>();
    public static final ArrayList<Player> waitingForName = new ArrayList<>();

    public static final HashMap<BiomiaPlayer, PlayerBan> waitForCustomReason = new HashMap<>();


    private static final HashMap<BiomiaPlayer, ScrolableReportInventory> currentReportsMenu = new HashMap<>();

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
        currentReportsMenu.computeIfAbsent(bp, inventory -> new ScrolableReportInventory(bp)).openInventory();
    }

    public static void removeReports(ArrayList<PlayerReport> playerReports) {
        ReportSQL.removeBugReport(playerReports.get(0).getReporteterBiomiaPlayer().getBiomiaPlayerID());
        playerReports.forEach(playerReports::remove);
    }

    public static ArrayList<PlayerReport> getReports(int biomiaID) {
        return plReports.stream().filter(each -> each.getReporteterBiomiaPlayer().getBiomiaPlayerID() == biomiaID)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void openReportMenu(Player player) {
        player.openInventory(reportMenu);
    }
}
