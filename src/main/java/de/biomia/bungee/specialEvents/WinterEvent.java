package de.biomia.bungee.specialEvents;

import de.biomia.bungee.BungeeBiomia;
import de.biomia.bungee.BungeeMain;
import de.biomia.bungee.OfflineBungeeBiomiaPlayer;
import de.biomia.universal.MySQL;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class WinterEvent {

    final public static boolean isEnabled = false;


    static {
        Thread thread = new Thread(() -> {
            Calendar c = Calendar.getInstance();
            int lastday = 0;
            while (true) {
                try {
                    Thread.sleep(900000);
                    //noinspection MagicConstant
                    if (c.get(Calendar.MONTH) == Calendar.DECEMBER) {
                        if (lastday != 0 && lastday < c.get(Calendar.DAY_OF_MONTH)) {
                            switch (lastday) {
                                case 3: // 1. Advent
                                    randomWin(lastday);
                                    break;
                                case 10: // 2. Advent
                                    randomWin(lastday);
                                    break;
                                case 17: // 3. Advent
                                    randomWin(lastday);
                                    break;
                                case 24: // 4. Advent
                                    randomWin(lastday);
                                    break;
                                default:
                                    break;
                            }
                        }
                        lastday = c.get(Calendar.DAY_OF_MONTH);
                    } else {
                        lastday = 0;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        thread.start();
        BungeeMain.allThreads.add(thread);
    }

    public static void isWinner(OfflineBungeeBiomiaPlayer pp) {

        OfflineBungeeBiomiaPlayer bp = BungeeBiomia.getOfflineBiomiaPlayer(pp.getName());

        ArrayList<Integer> wintereventwinner = getPlayerFromWinner(pp.getBiomiaPlayerID());

        if (!wintereventwinner.isEmpty()) {
            for (int days : wintereventwinner) {
                pp.sendMessage(new TextComponent(
                        "\u00A7aGlückwunsch! Du hast am \u00A74" + days + ". \u00A7aDezember beim Gewinnspiel gewonnen!"));
                pp.sendMessage(new TextComponent(
                        "\u00A77Wir haben etwas für dich! Schicke eine e-mail an \u00A7cbusiness@biomia.de \u00A77mit folgendem Code:"));
                String code = "";
                switch (days) {
                    case 3:
                        code = "ADV3NT";
                        break;
                    case 10:
                        code = "A10NT";
                        break;
                    case 17:
                        code = "D17ENT";
                        break;
                    case 24:
                        code = "XMAS24";
                        break;
                    default:
                        break;
                }
                pp.sendMessage(new TextComponent("\u00A74" + code));
            }
            removePlayerFromWinner(bp.getBiomiaPlayerID());
        }
    }


    static void randomWin(int day) {
        int bioid = pickRandomWinner();
        addBiomiaPlayerToWinner(bioid, day);
    }

    private static int pickRandomWinner() {
        ArrayList<Integer> member = new ArrayList<>();
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        try {
            PreparedStatement ps = con.prepareStatement("Select BiomiaPlayer from WinterEvent");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                member.add(rs.getInt("BiomiaPlayer"));
            }
            rs.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return member.get(new Random().nextInt(member.size()));
    }

    private static void addBiomiaPlayerToWinner(int bpID, int day) {
        MySQL.executeUpdate("INSERT INTO `WinterEventWinner`(`BiomiaPlayer`, `day`, `received`) VALUES (" + bpID + "," + day + ", false)", MySQL.Databases.biomia_db);
    }

    private static void removePlayerFromWinner(int bpID) {
        MySQL.executeUpdate("UPDATE `WinterEventWinner` SET `received`= true WHERE BiomiaPlayer = " + bpID, MySQL.Databases.biomia_db);
    }

    private static ArrayList<Integer> getPlayerFromWinner(int bpID) {

        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        ArrayList<Integer> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM `WinterEventWinner` WHERE BiomiaPlayer = ? AND received = false");
            ps.setInt(1, bpID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getInt("day"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


}
