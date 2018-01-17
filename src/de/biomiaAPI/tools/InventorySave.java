package de.biomiaAPI.tools;

import de.biomiaAPI.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InventorySave {

    public static void saveInventory(Player p, String serverGroup) {

        String inv = toBase64(p.getInventory());
        String uuid = p.getUniqueId().toString();

        if (MySQL.executeQuery("SELECT * from InventorySaves where uuid = '" + uuid + "' AND servergroup = '" + serverGroup + "'", "inventory") != null) {

            MySQL.executeUpdate("UPDATE `InventorySaves` SET `uuid`= '" + uuid + "',`inventory`='" + inv + "',`servergroup`='" + serverGroup + "' WHERE uuid = '" + uuid + "' AND servergroup = '" + serverGroup + "'");
        } else {
            MySQL.executeUpdate("INSERT INTO `InventorySaves` (`uuid`, `inventory`, `servergroup`) VALUES ('" + uuid + "','" + inv + "','" + serverGroup + "')");
        }

    }

    public static void setInventory(Player p, String serverGroup) {

        String uuid = p.getUniqueId().toString();
        String s = MySQL.executeQuery("SELECT * from InventorySaves where uuid = '" + uuid + "' AND servergroup = '" + serverGroup + "'", "inventory");

        if (s != null) {

            Inventory inv = null;

            try {
                inv = fromBase64(s);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (inv != null) {
                p.getInventory().setContents(inv.getContents());
                p.updateInventory();
            }
        }
    }

    private static String toBase64(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Save every element in the list
            for (int i = 0; i <= 40; i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }
            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    private static Inventory fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, InventoryType.PLAYER);

            // Read the serialized inventory
            for (int i = 0; i <= 40; i++) {
                Object o = dataInput.readObject();
                if (o != null) {
                    inventory.setItem(i, (ItemStack) o);
                }
            }
            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
