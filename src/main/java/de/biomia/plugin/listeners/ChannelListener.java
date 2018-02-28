package de.biomia.plugin.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.biomia.plugin.reportsystem.PlayerBan;
import de.biomia.plugin.reportsystem.PlayerReport;
import de.biomia.plugin.reportsystem.ReportManager;
import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.pex.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class ChannelListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if (subchannel.equals("Report")) {
            int reporter = in.readInt();
            int reporteter = in.readInt();
            String grund = in.readUTF();

            PlayerReport report = new PlayerReport(reporter, reporteter, grund);
            ReportManager.plReports.add(report);
        }

        if (subchannel.equals("RankUp")) {
            String pl = in.readUTF();
            String rank = in.readUTF();

            Player p = Bukkit.getPlayer(pl);
            Rank.setRank(p, rank);
        }

        if (subchannel.equals("BanReason")) {
            int id = in.readInt();
            String name = BiomiaPlayer.getName(id);

            Player p = Bukkit.getPlayer(name);
            if (p != null) {
                int banID = in.readInt();
                new PlayerBan(Biomia.getBiomiaPlayer(p), banID);
            }
        }
    }
}
