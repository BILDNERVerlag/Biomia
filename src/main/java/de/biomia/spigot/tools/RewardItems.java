package de.biomia.spigot.tools;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.spigot.server.quests.QuestEvents.GiveItemEvent;
import de.biomia.universal.MySQL;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class RewardItems {

    public static void addItem(OfflineBiomiaPlayer biomiaPlayer, ItemStack itemStack, BiomiaServerType serverType) {
        String tablename = "%notable";
        if (serverType.equals(BiomiaServerType.Quest)) {
            tablename = "QuestItems";
        } else if (serverType.equals(BiomiaServerType.Freebuild)) {
            tablename = "FreebuildItems";
        }
        MySQL.executeUpdate(String.format("Insert into %s (`BiomiaPlayer`, `Item`) values (%d, '%s')", tablename, biomiaPlayer.getBiomiaPlayerID(), Base64.toBase64(itemStack)), MySQL.Databases.quests_db);
    }

    public static void giveItems(BiomiaPlayer biomiaPlayer, BiomiaServerType serverType) {
        String tablename = "%notable";
        if (serverType.equals(BiomiaServerType.Quest)) {
            tablename = "QuestItems";
        } else if (serverType.equals(BiomiaServerType.Freebuild)) {
            tablename = "FreebuildItems";
        }
        Connection con = MySQL.Connect(MySQL.Databases.quests_db);
        try {
            PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(String.format("Select Item from %s where BiomiaPlayer = ?", tablename));
            ps.setInt(1, biomiaPlayer.getBiomiaPlayerID());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String base64Item = rs.getString("Item");
                new GiveItemEvent((ItemStack) Base64.fromBase64(base64Item)).executeEvent(biomiaPlayer);
                MySQL.executeUpdate(String.format("DELETE FROM `%s` WHERE `BiomiaPlayer`= %d AND `Item` = '%s'", tablename, biomiaPlayer.getBiomiaPlayerID(), base64Item), MySQL.Databases.quests_db);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
