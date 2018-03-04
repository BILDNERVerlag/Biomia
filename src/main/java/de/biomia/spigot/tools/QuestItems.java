package de.biomia.spigot.tools;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.universal.MySQL;
import de.biomia.spigot.server.quests.QuestEvents.GiveItemEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class QuestItems {

    public static void addQuestItem(BiomiaPlayer biomiaPlayer, ItemStack itemStack) {
        MySQL.executeUpdate("Insert into QuestItems (`BiomiaPlayer`, `Item`) values ("
                + biomiaPlayer.getBiomiaPlayerID() + ", '" + Base64.toBase64(itemStack) + "')", MySQL.Databases.quests_db);
    }

    public static void giveQuestItems(BiomiaPlayer biomiaPlayer) {
        Connection con = MySQL.Connect(MySQL.Databases.quests_db);
        try {
            PreparedStatement ps = Objects.requireNonNull(con).prepareStatement("Select Item from QuestItems where BiomiaPlayer = ?");
            ps.setInt(1, biomiaPlayer.getBiomiaPlayerID());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String base64Item = rs.getString("Item");
                new GiveItemEvent((ItemStack) Base64.fromBase64(base64Item)).executeEvent(biomiaPlayer.getQuestPlayer());
                MySQL.executeUpdate("DELETE FROM `QuestItems` WHERE `BiomiaPlayer`= " + biomiaPlayer.getBiomiaPlayerID()
                        + " AND `Item` = '" + base64Item + "'", MySQL.Databases.quests_db);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
