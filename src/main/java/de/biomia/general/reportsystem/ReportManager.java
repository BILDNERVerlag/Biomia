package de.biomia.general.reportsystem;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.biomia.BiomiaPlayer;
import de.biomia.general.reportsystem.listener.Channel;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportManager {

    public static final ArrayList<PlayerReport> plReports = new ArrayList<>();
    public static final ArrayList<PlayerReport> unfinishedReports = new ArrayList<>();
    public static final ArrayList<Player> waitingForBugReason = new ArrayList<>();
    public static final ArrayList<Player> waitingForName = new ArrayList<>();
    private static final HashMap<BiomiaPlayer, ScrolableReportInventory> currentReportsMenu = new HashMap<>();

    // gets accessed remotely via BungeeCord
    public static void sendReport(PlayerReport pr) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Report");
        out.writeInt(pr.getReporteterBiomiaID());
        out.writeInt(pr.getReporterBiomiaID());
        out.writeUTF(pr.getGrund());
        Channel.send(out);
    }

    public static void openScrollableInventory(BiomiaPlayer bp) {
        ScrolableReportInventory reportInventory = currentReportsMenu.computeIfAbsent(bp, inventory -> new ScrolableReportInventory(bp));
        reportInventory.openInventory();
    }

    public static void removeReports(ArrayList<PlayerReport> playerReports) {
        playerReports.forEach(playerReports::remove);
    }


    public static ArrayList<PlayerReport> getReports(int biomiaID) {
        final ArrayList<PlayerReport> reports = new ArrayList<>();
        plReports.forEach(each -> {
            if (each.getReporteterBiomiaID() == biomiaID) {
                reports.add(each);
            }
        });
        return reports;
    }

}
