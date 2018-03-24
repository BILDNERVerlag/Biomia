package de.biomia.universal;

import java.util.UUID;

public abstract class UniversalBiomiaPlayer {

    private final int biomiaID;
    private String name;
    private UUID uuid;

    public static boolean isPlayerRegistered(UUID uuid) {
        return getBiomiaPlayerID(uuid) != -1;
    }

    public static int getBiomiaPlayerID(UUID uuid) {
        return MySQL.executeQuerygetint("Select id from BiomiaPlayer where uuid = '" + uuid.toString() + "'", "id", MySQL.Databases.biomia_db);
    }

    public static int getBiomiaPlayerID(String playerName) {
        return MySQL.executeQuerygetint("Select id from BiomiaPlayer where name = '" + playerName + "'", "id", MySQL.Databases.biomia_db);
    }

    protected UniversalBiomiaPlayer(int biomiaID) {
        this.biomiaID = biomiaID;
    }

    protected UniversalBiomiaPlayer(int biomiaID, String name) {
        this.biomiaID = biomiaID;
        this.name = name;
    }

    protected UniversalBiomiaPlayer(int biomiaID, UUID uuid) {
        this.biomiaID = biomiaID;
        this.uuid = uuid;
    }

    // ID and UUID METHODS

    public static String getName(int biomiaID) {
        return MySQL.executeQuery("Select name from BiomiaPlayer where id = " + biomiaID, "name", MySQL.Databases.biomia_db);
    }

    private static UUID getUUID(int biomiaID) {
        String s = MySQL.executeQuery("Select uuid from BiomiaPlayer where id = " + biomiaID, "uuid", MySQL.Databases.biomia_db);
        return s != null ? UUID.fromString(s) : null;
    }

    public final void giveBoost(int percent, int timeinseconds) {
        stopCoinBoost();
        MySQL.executeUpdate("INSERT INTO `CoinBoost`(`BiomiaPlayer`, `percent`, `until`) VALUES (" + biomiaID
                + "," + percent + "," + timeinseconds + (System.currentTimeMillis() / 1000) + ")", MySQL.Databases.biomia_db);
    }

    protected void stopCoinBoost() {
        MySQL.executeUpdate("DELETE FROM `CoinBoost` WHERE BiomiaPlayer = " + biomiaID, MySQL.Databases.biomia_db);
    }

    // GETTERS AND SETTERS

    protected final UUID getUUID() {
        return uuid != null ? uuid : (uuid = getUUID(biomiaID));
    }

    public final int getBiomiaPlayerID() {
        return biomiaID;
    }

    public final String getName() {
        return name != null ? name : (name = getName(biomiaID));
    }

    public final int getCoins() {
        return MySQL.executeQuerygetint("SELECT * FROM `BiomiaCoins` where ID = " + biomiaID, "coins", MySQL.Databases.biomia_db);
    }

    public final void setCoins(int coins) {
        MySQL.executeUpdate("UPDATE `BiomiaCoins` SET `coins` = " + coins + " WHERE `ID` = " + biomiaID, MySQL.Databases.biomia_db);
    }

    public abstract boolean isOnline();

    public abstract void takeCoins(int coins);

    public abstract void addCoins(int coins, boolean enableBoost);

    public abstract void sendMessage(String string);

}
