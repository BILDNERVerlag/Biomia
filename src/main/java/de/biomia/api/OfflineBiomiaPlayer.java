package de.biomia.api;

import de.biomia.api.coins.Coins;
import de.biomia.api.mysql.MySQL;
import de.biomia.api.pex.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class OfflineBiomiaPlayer {

    private final int biomiaPlayerID;
    private String name;
    private Player player;
    private UUID uuid;

    public OfflineBiomiaPlayer(int biomiaPlayerID) {
        this.biomiaPlayerID = biomiaPlayerID;
    }

    public OfflineBiomiaPlayer(Player p) {
        this.name = p.getName();
        this.biomiaPlayerID = getBiomiaPlayerID(name);
    }

    // ID and UUID RELATED METHODS
    @Deprecated
    public static int getBiomiaPlayerID(String playerName) {
        return MySQL.executeQuerygetint("Select id from BiomiaPlayer where name = '" + playerName + "'", "id", MySQL.Databases.biomia_db);
    }

    @Deprecated
    public static int getBiomiaPlayerID(UUID uuid) {
        return MySQL.executeQuerygetint("Select id from BiomiaPlayer where uuid = '" + uuid.toString() + "'", "id", MySQL.Databases.biomia_db);
    }

    @Deprecated
    public static UUID getUUID(int biomiaID) {
        String s = MySQL.executeQuery("Select uuid from BiomiaPlayer where id = " + biomiaID, "uuid", MySQL.Databases.biomia_db);
        if (s != null)
            return UUID.fromString(s);
        else return null;
    }

    @Deprecated
    public static String getName(int biomiaID) {
        return MySQL.executeQuery("Select name from BiomiaPlayer where id = " + biomiaID, "name", MySQL.Databases.biomia_db);
    }

    //RANK METHODS
    public final boolean isPremium() {
        return Rank.isPremium(getName());
    }

    public final boolean isStaff() {
        String rank = Rank.getRank(getName());
        rank = rank.toLowerCase();
        return (rank.contains("Owner") || rank.contains("Admin") || rank.contains("Moderator") || rank.contains("Builder") || rank.contains("Supporter"));
    }

    public final boolean isOwner() {
        String rank = Rank.getRank(getName());
        return rank.contains("Owner");
    }

    public final boolean isYouTuber() {

        String rank = Rank.getRank(getName());

        return rank.contains("YouTube");

    }

    // BIOMIA-COIN METHODS
    public void setCoins(int coins) {
        Coins.setCoins(coins, this);
    }

    public int getCoins() {
        return Coins.getCoins(this);
    }

    public void addCoins(int coins, boolean enableBoost) {
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        int prozent = 100;
        if (enableBoost)
            try {
                assert con != null;
                PreparedStatement ps = con
                        .prepareStatement("SELECT `percent`, `until` FROM `CoinBoost` WHERE BiomiaPlayer = ?");
                ps.setInt(1, getBiomiaPlayerID());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    long until = rs.getLong("until");
                    if (System.currentTimeMillis() / 1000 > until) {
                        prozent = rs.getInt("percent");
                    } else {
                        stopCoinBoost();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        if (prozent != 100) {
            double coinsDouble = (double) coins / 100 * prozent;
            coins = (int) coinsDouble;
        }
        Coins.addCoins(coins, this);
    }

    public void takeCoins(int coins) {
        Coins.takeCoins(coins, this);
    }

    public final void giveBoost(int percent, int timeinseconds) {
        stopCoinBoost();
        MySQL.executeUpdate("INSERT INTO `CoinBoost`(`BiomiaPlayer`, `percent`, `until`) VALUES (" + biomiaPlayerID
                + "," + percent + "," + timeinseconds + (System.currentTimeMillis() / 1000) + ")", MySQL.Databases.biomia_db);
    }

    private void stopCoinBoost() {
        MySQL.executeUpdate("DELETE FROM `CoinBoost` WHERE BiomiaPlayer = " + biomiaPlayerID, MySQL.Databases.biomia_db);
    }

    // GETTERS AND SETTERS
    public final int getBiomiaPlayerID() {
        return biomiaPlayerID;
    }

    public final UUID getUUID() {
        return uuid != null ? uuid : (uuid = BiomiaPlayer.getUUID(biomiaPlayerID));
    }

    public final int getPremiumLevel() {
        return Rank.getPremiumLevel(getName());
    }

    public boolean isOnline() {
        return getPlayer() != null;
    }

    /**
     * Returns the Player, if they are online. Returns null otherwise.
     */
    public Player getPlayer() {
        return player != null ? player : (player = Bukkit.getPlayer(getName()));
    }

    private String getName() {
        return name != null ? name : (name = BiomiaPlayer.getName(biomiaPlayerID));
    }

}
