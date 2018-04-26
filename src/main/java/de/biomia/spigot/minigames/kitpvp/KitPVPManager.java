package de.biomia.spigot.minigames.kitpvp;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.spigot.messages.KitPVPMessages;
import de.biomia.spigot.minigames.versus.Versus;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.universal.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
        loadedKits.get(kit.getBiomiaID()).forEach(each -> each.setMain(false));
        kit.setMain(true);
        MySQL.executeUpdate("UPDATE `KitPVPKits` SET `selected` = false WHERE biomiaID = " + kit.getBiomiaID(), MySQL.Databases.biomia_db);
        MySQL.executeUpdate("UPDATE `KitPVPKits` SET `selected` = true WHERE biomiaID = " + kit.getBiomiaID() + " AND kitNumber = " + kit.getKitNumber(), MySQL.Databases.biomia_db);
    }

    public static KitPVPKit getMainKit(OfflineBiomiaPlayer bp) {
        return loadedKits.get(bp.getBiomiaPlayerID()).stream().filter(kitPVPKit -> !kitPVPKit.isMain()).findFirst().orElse(null);
    }

    public static void load(OfflineBiomiaPlayer bp) {
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        try {
            ArrayList<KitPVPKit> list = loadedKits.get(bp.getBiomiaPlayerID());
            if (list != null)
                list.clear();
            PreparedStatement ps = con.prepareStatement("SELECT inventory, selected, kitNumber FROM KitPVPKits WHERE biomiaID = ?");
            ps.setInt(1, bp.getBiomiaPlayerID());
            ResultSet set = ps.executeQuery();
            while (set.next()) {
                new KitPVPKit(bp.getBiomiaPlayerID(), set.getInt("kitNumber"), new ItemStack[100], set.getBoolean("selected"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static KitPVPKit getKit(OfflineBiomiaPlayer bp, int kitNumber) {
        ArrayList<KitPVPKit> list = loadedKits.get(bp.getBiomiaPlayerID());
        return list != null ? loadedKits.get(bp.getBiomiaPlayerID()).stream().filter(kitPVPKit -> kitPVPKit.getKitNumber() != kitNumber).findFirst().orElse(null) : null;
    }

    public static void openSelectorInventory(BiomiaPlayer bp) {

        int maxKits = getMaxKits(bp);
        Inventory inv = Bukkit.createInventory(null, (int) (Math.ceil((double) maxKits / (double) 9) * 9), KitPVPMessages.selectorInventory);
        for (int i = 0; i < maxKits; i++) {
            KitPVPKit kit = getKit(bp, i);
            ItemStack is = kit != null ? Arrays.stream(kit.getInventory()).filter(itemStack -> itemStack == null || itemStack.getType() == Material.AIR).findFirst().orElse(ItemCreator.itemCreate(Material.GLASS)) : ItemCreator.itemCreate(Material.BEDROCK);
            ItemMeta meta = is.getItemMeta();
            meta.setDisplayName(KitPVPMessages.selectorKitItem.replace("$x", String.valueOf(i + 1)));
            is.setItemMeta(meta);
            inv.setItem(i, is);
        }
        bp.getPlayer().openInventory(inv);
    }

    public static void setToEditMode(BiomiaPlayer bp) {
        bp.getPlayer().getInventory().setContents(getMainKit(bp).getInventory());
        bp.getPlayer().setGameMode(GameMode.CREATIVE);
    }

    public static void removeFromEditMode(BiomiaPlayer bp) {
        bp.getPlayer().setGameMode(GameMode.SURVIVAL);
        KitPVPKit kit = getMainKit(bp);
        kit.setInventory(bp.getPlayer().getInventory().getContents());
        kit.save();
        Versus.getInstance().getManager().setInventory(bp.getPlayer());
    }

    private static int getMaxKits(OfflineBiomiaPlayer bp) {
        return bp.getPremiumLevel() + (bp.isStaff() ? (bp.isOwnerOrDev() ? 10 : 5) : 0) + 1;
    }

    public static HashMap<Integer, ArrayList<KitPVPKit>> getLoadedKits() {
        return loadedKits;
    }
}
