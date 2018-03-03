package de.biomia.bungee.cmds;

import de.biomia.Biomia;
import de.biomia.OfflineBiomiaPlayer;
import de.biomia.bungee.Main;
import de.biomia.data.MySQL;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemovePlayerReport extends Command {

    public RemovePlayerReport(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender.hasPermission("biomia.removereport")) {
            if (args.length == 1) {
                try {
                    PreparedStatement statement = MySQL.Connect(MySQL.Databases.biomia_db).prepareStatement("SELECT `Reporter` FROM `PlayerReports` where Reporteter = ?");

                    int reporteterID = Biomia.getOfflineBiomiaPlayer(args[0]).getBiomiaPlayerID();

                    statement.setInt(1, reporteterID);
                    ResultSet rs = statement.executeQuery();

                    boolean b = false;

                    while (rs.next()) {
                        b = true;
                        int reporter = rs.getInt("Reporter");
                        OfflineBiomiaPlayer p = Biomia.getOfflineBiomiaPlayer(reporter);
                        p.addCoins(Main.playerReportRewardMoney, false);
                        if (p.isOnline()) {
                            p.getBungeeBiomiaPlayer().getProxiedPlayer().sendMessage(new TextComponent("�cDanke f�r den Report! Der Spieler wurde gebannt!"));
                        }
                    }
                    if (b) {
                        sender.sendMessage(new TextComponent("�cDer Spieler wurde nicht Reportet!"));
                    } else {
                        sender.sendMessage(new TextComponent("�cDer Report wurde entfernt und die Reporter belohnt!"));
                        MySQL.executeUpdate("DELETE FROM `PlayerReports` WHERE `Reporteter` = " + reporteterID, MySQL.Databases.biomia_db);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else sender.sendMessage(new TextComponent("�cBitte nutze �b/removereport <Spieler>"));
        }
    }
}
