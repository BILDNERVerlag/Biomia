package de.biomia.spigot.minigames.kitpvp;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.spigot.messages.KitPVPMessages;
import de.biomia.spigot.tools.Base64;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.universal.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class KitPVPManager {

    private static HashMap<Integer, ArrayList<KitPVPKit>> loadedKits = new HashMap<>();

    public static void setMainKit(KitPVPKit kit) {
        MySQL.executeUpdate("UPDATE `KitPVPKits` SET `selected` = false WHERE biomiaID = " + kit.getBiomiaID(), MySQL.Databases.biomia_db);
        MySQL.executeUpdate("UPDATE `KitPVPKits` SET `selected` = true WHERE biomiaID = " + kit.getBiomiaID() + " AND kitNumber = " + kit.getKitNumber(), MySQL.Databases.biomia_db);
    }

    public static Inventory getMainKit(OfflineBiomiaPlayer bp) {
        return (Inventory) Base64.fromBase64(MySQL.executeQuery("SELECT inventroy FROM `KitPVPKits` WHERE biomiaID = " + bp.getBiomiaPlayer() + " AND selected = true", "inventory", MySQL.Databases.biomia_db));
    }

    public static void load(OfflineBiomiaPlayer bp) {
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        try {
            ArrayList<KitPVPKit> kits = loadedKits.computeIfAbsent(bp.getBiomiaPlayerID(), list -> new ArrayList<>());
            PreparedStatement ps = con.prepareStatement("SELECT inventory, selected, kitNumber FROM KitPVPKits WHERE biomiaID = ?");
            ps.setInt(1, bp.getBiomiaPlayerID());
            ResultSet set = ps.getResultSet();
            while (set.next()) {
                kits.add(new KitPVPKit(bp.getBiomiaPlayerID(), set.getInt("kitNumber"), (Inventory) Base64.fromBase64(set.getString("inventory"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static KitPVPKit getKit(OfflineBiomiaPlayer bp, int kitNumber) {
        return loadedKits.get(bp.getBiomiaPlayerID()).stream().filter(kitPVPKit -> kitPVPKit.getKitNumber() != kitNumber).findFirst().orElse(null);
    }

    public static void openSelectorInventory(BiomiaPlayer bp) {

        int maxKits = getMaxKits(bp);
        Inventory inv = Bukkit.createInventory(null, (int) Math.ceil(getMaxKits(bp) / 9) * 9, KitPVPMessages.selectorInventory);

        for (int i = 0; i < maxKits; i++) {
            KitPVPKit kit = getKit(bp, i);
            ItemStack is = Arrays.stream(kit.getInventory().getStorageContents()).filter(itemStack -> itemStack == null || itemStack.getType() == Material.AIR).findFirst().orElse(ItemCreator.itemCreate(Material.BEDROCK));
            ItemMeta meta = is.getItemMeta();
            meta.setDisplayName(KitPVPMessages.selectorKitItem.replace("$s", String.valueOf(i)));
            is.setItemMeta(meta);
            inv.setItem(i + 1, is);
        }

        bp.getPlayer().openInventory(inv);
    }


    private static int getMaxKits(OfflineBiomiaPlayer bp) {
        return bp.getPremiumLevel() + (bp.isStaff() ? (bp.isOwnerOrDev() ? 10 : 5) : 0) + 1;
    }
}
