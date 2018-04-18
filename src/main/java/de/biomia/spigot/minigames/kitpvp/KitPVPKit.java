package de.biomia.spigot.minigames.kitpvp;

import de.biomia.spigot.tools.Base64;
import de.biomia.universal.MySQL;
import org.bukkit.inventory.Inventory;

public class KitPVPKit {

    private final int biomiaID;
    private final int kitNumber;
    private Inventory inventory;

    public KitPVPKit(int biomiaID, int kitNumber, Inventory inventory) {
        this.kitNumber = kitNumber;
        this.biomiaID = biomiaID;
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getBiomiaID() {
        return biomiaID;
    }

    public int getKitNumber() {
        return kitNumber;
    }

    public void save() {
        if (MySQL.executeQuerygetint("Select KitPVPKits WHERE kitNumber = " + kitNumber + " AND `biomiaID` = " + biomiaID, "biomiaID", MySQL.Databases.biomia_db) != -1)
            MySQL.executeUpdate("UPDATE `KitPVPKits` SET `inventory`= " + Base64.toBase64(inventory) + " WHERE kitNumber = " + kitNumber + " AND `biomiaID` = " + biomiaID, MySQL.Databases.biomia_db);
        else {
            MySQL.executeUpdate("INSERT INTO KitPVPKits (`biomiaID`, `kitNumber`, `inventory`, `selected`) VALUES (" + biomiaID + "," + kitNumber + ",'" + Base64.toBase64(inventory) + "'," + (kitNumber == 0) + ")", MySQL.Databases.biomia_db);
        }
    }
}
