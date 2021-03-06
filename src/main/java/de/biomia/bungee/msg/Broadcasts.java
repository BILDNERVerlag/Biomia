package de.biomia.bungee.msg;

import de.biomia.bungee.BungeeBiomia;
import de.biomia.bungee.BungeeMain;
import de.biomia.bungee.OfflineBungeeBiomiaPlayer;
import de.biomia.universal.Messages;
import de.biomia.universal.MySQL;
import net.md_5.bungee.api.ProxyServer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Broadcasts {

    private static final int delayInSeconds = 60 * 5;

    public static void startBroadcast() {

        Thread thread = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    PreparedStatement ps = MySQL.Connect(MySQL.Databases.biomia_db).prepareStatement("SELECT * FROM `Broadcasts`");
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String text = Messages.format(rs.getString("message"));
                        ProxyServer.getInstance().getPlayers().forEach(each -> {
                            OfflineBungeeBiomiaPlayer bp = BungeeBiomia.getOfflineBiomiaPlayer(each.getName());
                            if (!bp.isStaff() && !bp.isYouTuber())
                                bp.sendMessage(text);
                        });
                        Thread.sleep(delayInSeconds * 1000);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                } catch (InterruptedException ignore) {
                    return;
                }
            }
        });
        thread.start();
        BungeeMain.allThreads.add(thread);
    }
}
