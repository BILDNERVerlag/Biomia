package de.biomia;

import de.biomia.data.MySQL;

import java.util.UUID;

public abstract class UniversalBiomiaPlayer {

    private final int biomiaPlayerID;
    private String name;
    private UUID uuid;

    public static boolean isPlayerRegistered(UUID uuid) {
        return getBiomiaPlayerID(uuid) != -1;
    }

    public static int getBiomiaPlayerID(UUID uuid) {
        return MySQL.executeQuerygetint("Select id from BiomiaPlayer where uuid = '" + uuid.toString() + "'", "id", MySQL.Databases.biomia_db);
    }

    UniversalBiomiaPlayer(int biomiaPlayerID) {
        this.biomiaPlayerID = biomiaPlayerID;
    }

    UniversalBiomiaPlayer(int biomiaPlayerID, String name) {
        this.biomiaPlayerID = biomiaPlayerID;
        this.name = name;
    }

    UniversalBiomiaPlayer(int biomiaPlayerID, UUID uuid) {
        this.biomiaPlayerID = biomiaPlayerID;
        this.uuid = uuid;
    }

    // ID and UUID METHODS

    static int getBiomiaPlayerID(String playerName) {
        return MySQL.executeQuerygetint("Select id from BiomiaPlayer where name = '" + playerName + "'", "id", MySQL.Databases.biomia_db);
    }

    static String getName(int biomiaID) {
        return MySQL.executeQuery("Select name from BiomiaPlayer where id = " + biomiaID, "name", MySQL.Databases.biomia_db);
    }

    private static UUID getUUID(int biomiaID) {
        String s = MySQL.executeQuery("Select uuid from BiomiaPlayer where id = " + biomiaID, "uuid", MySQL.Databases.biomia_db);
        return s != null ? UUID.fromString(s) : null;
    }

    public final void giveBoost(int percent, int timeinseconds) {
        stopCoinBoost();
        MySQL.executeUpdate("INSERT INTO `CoinBoost`(`BiomiaPlayer`, `percent`, `until`) VALUES (" + biomiaPlayerID
                + "," + percent + "," + timeinseconds + (System.currentTimeMillis() / 1000) + ")", MySQL.Databases.biomia_db);
    }

    void stopCoinBoost() {
        MySQL.executeUpdate("DELETE FROM `CoinBoost` WHERE BiomiaPlayer = " + biomiaPlayerID, MySQL.Databases.biomia_db);
    }

    // GETTERS AND SETTERS

    final UUID getUUID() {
        return uuid != null ? uuid : (uuid = getUUID(biomiaPlayerID));
    }

    public final int getBiomiaPlayerID() {
        return biomiaPlayerID;
    }

    public final String getName() {
        return name != null ? name : (name = getName(biomiaPlayerID));
    }

    public final int getCoins() {
        return MySQL.executeQuerygetint("SELECT * FROM `BiomiaCoins` where ID = " + biomiaPlayerID, "coins", MySQL.Databases.biomia_db);
    }

    public final void setCoins(int coins) {
        MySQL.executeUpdate("UPDATE `BiomiaCoins` SET `coins` = " + coins + " WHERE `ID` = " + biomiaPlayerID, MySQL.Databases.biomia_db);
    }

    public abstract boolean isOnline();

    public abstract void takeCoins(int coins);

    public abstract void addCoins(int coins, boolean enableBoost);

    public abstract void sendMessage(String string);

}
