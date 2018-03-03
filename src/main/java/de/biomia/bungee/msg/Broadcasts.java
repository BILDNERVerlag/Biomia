package de.biomia.bungee.msg;

import de.biomia.bungee.Main;
import de.biomia.data.MySQL;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Broadcasts {

    private static int delayInSeconds = 60 * 5;

    public static void startBroadcast() {

        Thread thread = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    PreparedStatement ps = MySQL.Connect(MySQL.Databases.biomia_db).prepareStatement("SELECT * FROM `Broadcasts`");
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        TextComponent text = new TextComponent(ChatColor.translateAlternateColorCodes('&', rs.getString("message")));
                        Main.getBiomiaPlayers().forEach(each -> {
                            if (!each.getProxiedPlayer().hasPermission("biomia.ad")) {
                                each.getProxiedPlayer().sendMessage(text);
                            }
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
        Main.allThreads.add(thread);
    }
}
