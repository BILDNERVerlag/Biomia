package de.biomia.spigot.tools;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.universal.Messages;
import de.biomia.universal.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.util.stream.Collectors.joining;

public class ItemStackSaver {

    private final static MySQL.Databases database = MySQL.Databases.biomia_db;
    private final static String tableName = "SavedItemStacks";
    private final static String noValue = "<leer>";

    public static void saveInventory(BiomiaPlayer bp) {
        for (ItemStack is : bp.getPlayer().getInventory().getContents()) {
            if (is != null && is.getType() != null && is.getType() != Material.AIR) {
                saveItemStack(is, bp);
            }
        }
    }

    public static int saveItemStack(ItemStack is, BiomiaPlayer bp) {
        if (is == null || is.getType() == Material.AIR) return -1;
        String name = (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) ? is.getItemMeta().getDisplayName() : noValue;
        String lore = (is.hasItemMeta() && is.getItemMeta().getLore() != null) ? String.join(" % ", is.getItemMeta().getLore()) : noValue;
        String type = is.getType().name();
        int amount = is.getAmount();
        int data = is.getData().getData();
        int durability = is.getDurability();
        String meta = (is.hasItemMeta()) ? Base64.toBase64(is.getItemMeta()) : noValue;
        String enchantments = is.getItemMeta().hasEnchants() ? is.getItemMeta().getEnchants().entrySet()
                .stream()
                .map(e -> e.getKey().getName() + ":" + e.getValue())
                .collect(joining(", "))
                : noValue;
        return bp == null
                ? saveItemStack(name, lore, type, amount, data, durability, meta, enchantments, -1, null)
                : saveItemStack(name, lore, type, amount, data, durability, meta, enchantments, bp.getBiomiaPlayerID(), Biomia.getServerInstance().getServerType().toString());
    }

    public static int saveItemStack(String name, String lore, String type, int amount, int data, int durability, String meta, String enchantments, int biomiaID, String server) {
        int retValue = -1;
        if (type == null || type.equals("AIR")) return retValue;
        Connection con = MySQL.Connect(database);
        if (name == null) name = noValue;
        if (lore == null) lore = noValue;
        if (meta == null) meta = noValue;
        if (enchantments == null) enchantments = noValue;
        if (biomiaID == 0) biomiaID = -1;
        if (server == null) server = noValue;
        if (con != null)
            try {
                PreparedStatement ps;
                ps = con.prepareStatement("INSERT INTO " + tableName + " (`name`, `lore`, `type`, `amount`, `data`, `durability`, `meta`, `enchants`, `biomiaID`, `server`) VALUES (?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                ps.setString(2, lore);
                ps.setString(3, type);
                ps.setInt(4, amount);
                ps.setInt(5, data);
                ps.setInt(6, durability);
                ps.setString(7, meta);
                ps.setString(8, enchantments);
                ps.setInt(9, biomiaID);
                ps.setString(10, server);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                retValue = rs.getInt(1);
                ps.close();
                return retValue;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return retValue;
    }

    public static ItemStack getItemStack(int ID) {
        Connection con = MySQL.Connect(database);
        ItemStack is = null;
        try {
            PreparedStatement ps = con.prepareStatement(String.format("SELECT `name`, `lore`, `type`, `amount`, `data`, `durability`, `meta`, `enchants` FROM %s WHERE ID = %d", tableName, ID));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String type = rs.getString("type");
                Material m;
                try {
                    m = Material.valueOf(type);
                } catch (Exception e) {
                    m = Material.BEDROCK;
                }
                is = ItemCreator.itemCreate(m);
                String metaString = rs.getString("meta");
                ItemMeta meta = null;
                if (!metaString.equals(noValue)) {
                    meta = (ItemMeta) Base64.fromBase64(metaString);
                }
                if (meta == null || metaString.equals(noValue)) {
                    meta = (is.hasItemMeta()) ? is.getItemMeta() : Bukkit.getServer().getItemFactory().getItemMeta(m);
                }
                String displayName = rs.getString("name");
                if (!displayName.equals(noValue)) {
                    meta.setDisplayName(displayName);
                }
                String loreString = rs.getString("lore");
                if (!loreString.equals(noValue)) {
                    meta.setLore(Arrays.asList(loreString.split(" % ")));
                }
                String enchantmentString = rs.getString("enchants");
                if (!enchantmentString.equals(noValue)) {
                    String[] enchantments = enchantmentString.split(", ");
                    for (String s : enchantments) {
                        String splitEnch[] = s.split(":");
                        Enchantment enchantment;
                        try {
                            enchantment = (Enchantment) Enchantment.class.getDeclaredField(splitEnch[0]).get(null);
                        } catch (Exception e) {
                            break;
                        }
                        int enchLevel = Integer.parseInt(splitEnch[1]);
                        meta.addEnchant(enchantment, enchLevel, true);
                    }
                }
                is.setItemMeta(meta);
                is.getData().setData((byte) rs.getInt("data"));
                is.setDurability((short) rs.getInt("durability"));
                is.setAmount(rs.getInt("amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return is;
    }

    public static ItemStack popItemStack(int ID) {
        ItemStack is = getItemStack(ID);
        if (is != null) {
            deleteItemStack(ID);
        }
        return is;
    }

    public static void loadInventory(BiomiaPlayer bp) {
        ArrayList<ItemStack> itemsToAdd = new ArrayList<>();

        Connection con = MySQL.Connect(database);
        ItemStack is = null;
        try {
            PreparedStatement ps = con.prepareStatement(String.format("SELECT `name`, `lore`, `type`, `amount`, `data`, `durability`, `meta`, `enchants` FROM %s WHERE biomiaID = %s AND server = '%s'", tableName, bp.getBiomiaPlayerID(), Biomia.getServerInstance().getServerType().name()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String type = rs.getString("type");
                Material m;
                try {
                    m = Material.valueOf(type);
                } catch (Exception e) {
                    m = Material.BEDROCK;
                }
                is = ItemCreator.itemCreate(m);
                String metaString = rs.getString("meta");
                ItemMeta meta = null;
                if (!metaString.equals(noValue)) {
                    meta = (ItemMeta) Base64.fromBase64(metaString);
                }
                if (meta == null || metaString.equals(noValue)) {
                    meta = (is.hasItemMeta()) ? is.getItemMeta() : Bukkit.getServer().getItemFactory().getItemMeta(m);
                }
                String displayName = rs.getString("name");
                if (!displayName.equals(noValue)) {
                    meta.setDisplayName(displayName);
                }
                String loreString = rs.getString("lore");
                if (!loreString.equals(noValue)) {
                    meta.setLore(Arrays.asList(loreString.split(" % ")));
                }
                String enchantmentString = rs.getString("enchants");
                if (!enchantmentString.equals(noValue)) {
                    String[] enchantments = enchantmentString.split(", ");
                    for (String s : enchantments) {
                        String splitEnch[] = s.split(":");
                        Enchantment enchantment;
                        try {
                            enchantment = (Enchantment) Enchantment.class.getDeclaredField(splitEnch[0]).get(null);
                        } catch (Exception e) {
                            break;
                        }
                        int enchLevel = Integer.parseInt(splitEnch[1]);
                        meta.addEnchant(enchantment, enchLevel, true);
                    }
                }
                is.setItemMeta(meta);
                is.getData().setData((byte) rs.getInt("data"));
                is.setDurability((short) rs.getInt("durability"));
                is.setAmount(rs.getInt("amount"));
                itemsToAdd.add(is);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MySQL.execute("DELETE FROM " + tableName + " WHERE biomiaID = " + bp.getBiomiaPlayerID() + " AND server = '" + Biomia.getServerInstance().getServerType().name() + "'", database);
        bp.getPlayer().getInventory().clear();

        for (ItemStack ist : itemsToAdd) {
            if (bp.getPlayer().getInventory().firstEmpty() != -1)
                bp.getPlayer().getInventory().addItem(ist);
            else {
                bp.getPlayer().getWorld().dropItemNaturally(bp.getPlayer().getLocation(), ist);
                bp.sendMessage(Messages.format("Achtung! Du hast %s erhalten, aber es war nicht genug Platz in deinem Inventar! §lEs liegt jetzt am Boden.", +ist.getAmount() + "x " + ist.getType().name()));
            }
        }

        ItemStack[] itemsToAddArray = new ItemStack[itemsToAdd.size()];
        bp.getPlayer().getInventory().addItem((ItemStack[]) itemsToAdd.toArray(itemsToAddArray));
    }

    public static int getBiomiaPlayerID(int ID) {
        return MySQL.executeQuerygetint(String.format("SELECT biomiaID FROM %s WHERE ID = %d", tableName, ID), "biomiaID", database);
    }

    public static int getServer(int ID) {
        return MySQL.executeQuerygetint(String.format("SELECT server FROM %s WHERE ID = %d", tableName, ID), "server", database);
    }

    public static void deleteItemStack(int ID) {
        MySQL.execute("DELETE FROM " + tableName + " WHERE ID = " + ID, database);
    }

}
