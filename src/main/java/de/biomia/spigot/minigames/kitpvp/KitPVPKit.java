package de.biomia.spigot.minigames.kitpvp;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.tools.Base64;
import de.biomia.universal.MySQL;
import org.bukkit.inventory.ItemStack;

public class KitPVPKit {

    private final int biomiaID;
    private final int kitNumber;
    private ItemStack[] inventory;
    private boolean isMainKit;

    public KitPVPKit(int biomiaID, int kitNumber, ItemStack[] inventory, boolean isMainKit) {
        this.biomiaID = biomiaID;
        this.kitNumber = kitNumber;
        this.inventory = inventory;
        this.isMainKit = false;
        KitPVPManager.getLoadedKits().get(biomiaID).add(this);
        if (isMainKit)
        KitPVPManager.setMainKit(this);
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public int getBiomiaID() {
        return biomiaID;
    }

    public int getKitNumber() {
        return kitNumber;
    }

    public void save() {
        if (MySQL.executeQuerygetint("Select biomiaID from KitPVPKits WHERE kitNumber = " + kitNumber + " AND `biomiaID` = " + biomiaID, "biomiaID", MySQL.Databases.biomia_db) != -1)
            MySQL.executeUpdate("UPDATE `KitPVPKits` SET `inventory`= '" + Base64.toBase64(inventory) + "' WHERE kitNumber = " + kitNumber + " AND `biomiaID` = " + biomiaID, MySQL.Databases.biomia_db);
        else {
            MySQL.executeUpdate("INSERT INTO KitPVPKits (`biomiaID`, `kitNumber`, `inventory`, `selected`) VALUES (" + biomiaID + "," + kitNumber + ",'" + Base64.toBase64(inventory) + "'," + (kitNumber == 0) + ")", MySQL.Databases.biomia_db);
        }
    }

    public void setMain(boolean main) {
        this.isMainKit = main;
    }

    public boolean isMain() {
        return isMainKit;
    }

    public void setInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public void setToPlayerInventory(BiomiaPlayer bp) {
        bp.getPlayer().getInventory().setContents(getInventory());
    }
}
