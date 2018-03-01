package de.biomia.plugin.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.biomia.Biomia;
import de.biomia.OfflineBiomiaPlayer;
import de.biomia.general.reportsystem.PlayerBan;
import de.biomia.general.reportsystem.PlayerReport;
import de.biomia.general.reportsystem.ReportManager;
import de.biomia.tools.RankManager;
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
            RankManager.setRank(p, rank);
        }

        if (subchannel.equals("BanReason")) {
            int id = in.readInt();
            OfflineBiomiaPlayer offlineBiomiaPlayer = Biomia.getOfflineBiomiaPlayer(id);
            if (offlineBiomiaPlayer.isOnline()) {
                new PlayerBan(offlineBiomiaPlayer.getBiomiaPlayer(), in.readInt());
            }
        }
    }
}
