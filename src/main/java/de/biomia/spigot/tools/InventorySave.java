package de.biomia.spigot.tools;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.universal.MySQL;
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

    public static void saveInventory(Player p, BiomiaServerType serverGroup) {

        String inv = toBase64(p.getInventory());
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

        if (getInventory(bp, serverGroup) != null) {
            MySQL.executeUpdate(String.format("UPDATE `InventorySaves` SET `biomiaID`= %d,`inventory`='%s',`servergroup`='%s' WHERE biomiaID = %d AND servergroup = '%s'", bp.getBiomiaPlayerID(), inv, serverGroup, bp.getBiomiaPlayerID(), serverGroup.name()), MySQL.Databases.biomia_db);
        } else {
            MySQL.executeUpdate(String.format("INSERT INTO `InventorySaves` (`biomiaID`, `inventory`, `servergroup`) VALUES (%d,'%s','%s')", bp.getBiomiaPlayerID(), inv, serverGroup.name()), MySQL.Databases.biomia_db);
        }

    }

    private static String getInventory(BiomiaPlayer bp, BiomiaServerType serverType) {
        return MySQL.executeQuery(String.format("SELECT * from InventorySaves where biomiaID = %d AND servergroup = '%s'", bp.getBiomiaPlayerID(), serverType.name()), "inventory", MySQL.Databases.biomia_db);
    }

    public static void setInventory(Player p, BiomiaServerType serverGroup) {

        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        String s = getInventory(bp, serverGroup);

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
