package de.biomia.spigot.tools;

import de.biomia.universal.MySQL;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class ItemStackSaver {

    private final static MySQL.Databases database = MySQL.Databases.biomia_db;

    public static void saveItemStack(ItemStack is) {
        String name = (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) ? is.getItemMeta().getDisplayName() : null;
        String lore = (is.hasItemMeta()) ? String.join(" % ", is.getItemMeta().getLore()) : null;
        String material = is.getType().name();
        int amount = is.getAmount();
        int data = is.getData().getData();
        int durability = is.getDurability();
        String meta = Base64.toBase64(is.getItemMeta());
        String enchantments = null;
        Connection con = MySQL.Connect(database);
        if (con != null)
            try {
                PreparedStatement ps;
                ps = con.prepareStatement("INSERT INTO SavedItemStacks (`name`, `lore`, `type`, `amount`, `data`, `durability`, `meta`, `enchantment`) VALUES (?,?,?,?,?,?,?,?)");
                ps.setString(1, name);
                ps.setString(3, lore);
                ps.setString(4, material);
                ps.setInt(5, amount);
                ps.setInt(6, data);
                ps.setInt(7, durability);
                ps.setString(8, meta);
                ps.setString(9, enchantments);
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static ItemStack getItemStack(int ID) {
        Connection con = MySQL.Connect(database);
        ItemStack is = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT `name`, `lore`, `type`, `amount`, `data`, `durability`, `meta`, `enchantment` FROM VSSettings WHERE ID = " + ID);
            ResultSet rs = ps.executeQuery();
            String type = rs.getString("type");
            Material m;
            try {
                m = Material.valueOf(type);
            } catch (Exception e) {
                m = Material.BEDROCK;
            }
            is = ItemCreator.itemCreate(m);
            ItemMeta meta = (ItemMeta) Base64.fromBase64(rs.getString("meta"));
            if(meta == null) meta = is.getItemMeta();
            String displayName = rs.getString("name");
            if (displayName != null) meta.setDisplayName(displayName);
            String loreString = rs.getString("lore");
            if (loreString != null) meta.setLore(Arrays.asList(loreString.split(" % ")));
            is.setItemMeta(meta);
            is.getData().setData((byte) rs.getInt("data"));
            is.setDurability((short) rs.getInt("durability"));
            is.setAmount(rs.getInt("amount"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return is;
        }
    }

}
