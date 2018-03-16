package de.biomia.bungee.cmds;

import de.biomia.bungee.BungeeBiomia;
import de.biomia.bungee.OfflineBungeeBiomiaPlayer;
import de.biomia.bungee.BungeeMain;
import de.biomia.universal.MySQL;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemoveReportCommand extends Command {

    public RemoveReportCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender.hasPermission("biomia.removereport")) {
            if (args.length == 1) {
                try {
                    PreparedStatement statement = MySQL.Connect(MySQL.Databases.biomia_db).prepareStatement("SELECT `Reporter` FROM `PlayerReports` where Reporteter = ?");

                    int reporteterID = BungeeBiomia.getOfflineBiomiaPlayer(args[0]).getBiomiaPlayerID();

                    statement.setInt(1, reporteterID);
                    ResultSet rs = statement.executeQuery();

                    boolean b = false;

                    while (rs.next()) {
                        b = true;
                        int reporter = rs.getInt("Reporter");
                        OfflineBungeeBiomiaPlayer p = BungeeBiomia.getOfflineBiomiaPlayer(reporter);
                        p.addCoins(BungeeMain.playerReportRewardMoney, false);
                        if (p.isOnline()) {
                            p.sendMessage("\u00A7cDanke f\u00fcr den Report! Der Spieler wurde \u00A7bgebannt\u00A7c!");
                        }
                    }
                    if (b) {
                        sender.sendMessage(new TextComponent("\u00A7cDer Spieler wurde nicht reportet!"));
                    } else {
                        sender.sendMessage(new TextComponent("\u00A7cDer Report wurde entfernt und die Reporter belohnt!"));
                        MySQL.executeUpdate("DELETE FROM `PlayerReports` WHERE `Reporteter` = " + reporteterID, MySQL.Databases.biomia_db);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else
                sender.sendMessage(new TextComponent("\u00A7cBitte nutze \u00A77/\u00A7bremovereport \u00A77<\u00A7bSpieler\u00A77>"));
        }
    }
}
