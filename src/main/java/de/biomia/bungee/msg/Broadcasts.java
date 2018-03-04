package de.biomia.bungee.msg;

import de.biomia.bungee.BungeeMain;
import de.biomia.universal.MySQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

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
                        TextComponent text = new TextComponent(ChatColor.translateAlternateColorCodes('&', rs.getString("message")));
                        BungeeCord.getInstance().getPlayers().forEach(each -> {
                            if (!each.hasPermission("biomia.ad")) {
                                each.sendMessage(text);
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
        BungeeMain.allThreads.add(thread);
    }
}
