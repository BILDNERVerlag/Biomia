package de.biomia.spigot.tools;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.universal.MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

public class SkyWarsKitManager {

    public static void addKit(BiomiaPlayer biomiaPlayer, int kitID, int... months) {
        MySQL.executeUpdate(String.format("Insert into SkyWarsKits (`biomiaID`, `kitID`, `available`) values (%d, %d, '%s')", biomiaPlayer.getBiomiaPlayerID(), kitID, Arrays.toString(months)), MySQL.Databases.biomia_db);
    }

    public static boolean addKit(BiomiaPlayer biomiaPlayer, int kitID) {
        return MySQL.executeUpdate(String.format("Insert into SkyWarsKits (`biomiaID`, `kitID`) values (%d, %d)", biomiaPlayer.getBiomiaPlayerID(), kitID), MySQL.Databases.biomia_db) != -1;
    }

    public static ArrayList<Integer> getAvailableKits(BiomiaPlayer biomiaPlayer) {
        ArrayList<Integer> availableKits = new ArrayList<>();
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        try {
            PreparedStatement ps = Objects.requireNonNull(con)
                    .prepareStatement("Select kitID, available from SkyWarsKits where BiomiaPlayer = ?");
            ps.setInt(1, biomiaPlayer.getBiomiaPlayerID());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String available = rs.getString("available");
                Calendar c = Calendar.getInstance();

                if (available == null || Arrays.asList(available.substring(1, available.length() - 1).split(", "))
                        .contains(c.get(Calendar.MONTH) + "")) {
                    availableKits.add(rs.getInt("kitID"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableKits;
    }

    public static int getLastSelectedKit(BiomiaPlayer bp) {
        return MySQL.executeQuerygetint(String.format("SELECT kitID FROM `LSSkyWarsKit` WHERE biomiaID = %d", bp.getBiomiaPlayerID()), "kitID", MySQL.Databases.biomia_db);
    }

    public static void setLastSelectedKit(BiomiaPlayer bp, int kitID) {
        if (MySQL.executeQuery(String.format("SELECT * FROM `LSSkyWarsKit` WHERE biomiaID = %d", bp.getBiomiaPlayerID()), "kitID", MySQL.Databases.biomia_db) != null)
            MySQL.executeUpdate(String.format("UPDATE LSSkyWarsKit SET `kitID` = %d WHERE biomiaID = %d", kitID, bp.getBiomiaPlayerID()), MySQL.Databases.biomia_db);
        else
            MySQL.executeUpdate(String.format("INSERT INTO `LSSkyWarsKit` (biomiaID,kitID) Values(%d, %d)", bp.getBiomiaPlayerID(), kitID), MySQL.Databases.biomia_db);
    }


}
