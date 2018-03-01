package de.biomia;

import de.biomia.dataManager.MySQL;
import de.biomia.events.general.CoinAddEvent;
import de.biomia.events.general.CoinTakeEvent;
import de.biomia.tools.RankManager;
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
    private BiomiaPlayer biomiaPlayer;

    OfflineBiomiaPlayer(int biomiaPlayerID) {
        this.biomiaPlayerID = biomiaPlayerID;
    }

    OfflineBiomiaPlayer(int biomiaPlayerID, String name) {
        this.biomiaPlayerID = biomiaPlayerID;
        this.name = name;
    }

    OfflineBiomiaPlayer(int biomiaPlayerID, UUID uuid) {
        this.biomiaPlayerID = biomiaPlayerID;
        this.uuid = uuid;
    }

    OfflineBiomiaPlayer(Player p) {
        this.name = p.getName();
        this.biomiaPlayerID = getBiomiaPlayerID(name);
        this.player = p;
        this.uuid = p.getUniqueId();
    }

    // ID and UUID RELATED METHODS
    @Deprecated
    public static int getBiomiaPlayerID(String playerName) {
        return MySQL.executeQuerygetint("Select id from BiomiaPlayer where name = '" + playerName + "'", "id", MySQL.Databases.biomia_db);
    }

    static int getBiomiaPlayerID(UUID uuid) {
        return MySQL.executeQuerygetint("Select id from BiomiaPlayer where uuid = '" + uuid.toString() + "'", "id", MySQL.Databases.biomia_db);
    }

    private static UUID getUUID(int biomiaID) {
        String s = MySQL.executeQuery("Select uuid from BiomiaPlayer where id = " + biomiaID, "uuid", MySQL.Databases.biomia_db);
        return s != null ? UUID.fromString(s) : null;
    }

    @Deprecated
    public static String getName(int biomiaID) {
        return MySQL.executeQuery("Select name from BiomiaPlayer where id = " + biomiaID, "name", MySQL.Databases.biomia_db);
    }

    //RANK METHODS
    public final boolean isPremium() {
        return RankManager.isPremium(getName());
    }

    public final boolean isStaff() {
        String rank = RankManager.getRank(getName());
        rank = rank.toLowerCase();
        return (rank.contains("Owner") || rank.contains("Admin") || rank.contains("Moderator") || rank.contains("Builder") || rank.contains("Supporter"));
    }

    public final boolean isOwner() {
        String rank = RankManager.getRank(getName());
        return rank.contains("Owner");
    }

    public final boolean isYouTuber() {
        String rank = RankManager.getRank(getName());
        return rank.contains("YouTube");
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
        CoinAddEvent coinEvent = new CoinAddEvent(this, coins);
        Bukkit.getServer().getPluginManager().callEvent(coinEvent);
        if (!coinEvent.isCancelled()) {
            setCoins(getCoins() + coins);
            this.getPlayer().sendMessage("\u00A77Du erh\u00e4ltst \u00A7f" + coins + "\u00A77 BC!");
        }
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

    /**
     * Returns the Player, if they are online. Returns null otherwise.
     */
    public Player getPlayer() {
        return player != null ? player : (player = Bukkit.getPlayer(getName()));
    }

    public BiomiaPlayer getBiomiaPlayer() {
        return Biomia.getBiomiaPlayer(getPlayer());
    }

    public boolean isOnline() {
        return getPlayer() != null;
    }

    public final UUID getUUID() {
        return uuid != null ? uuid : (uuid = getUUID(biomiaPlayerID));
    }

    public final int getBiomiaPlayerID() {
        return biomiaPlayerID;
    }

    public final int getPremiumLevel() {
        return RankManager.getPremiumLevel(getName());
    }

    public final String getName() {
        return player != null ? player.getName() : name != null ? name : (name = getName(biomiaPlayerID));
    }

    public final int getCoins() {
        return MySQL.executeQuerygetint("SELECT * FROM `BiomiaCoins` where ID = " + getBiomiaPlayerID(), "coins", MySQL.Databases.biomia_db);
    }

    public final void setCoins(int coins) {
        MySQL.executeUpdate("UPDATE `BiomiaCoins` SET `coins` = " + coins + " WHERE `ID` = " + getBiomiaPlayerID(), MySQL.Databases.biomia_db);
    }

    public final void takeCoins(int coins) {

        int actualCoins = getCoins();

        if (actualCoins < coins && isOnline()) {
            getPlayer().sendMessage("Du hast nicht genug BC! Dir fehlen noch " + (actualCoins - coins) + " BC!");
        }
        CoinTakeEvent coinEvent = new CoinTakeEvent(this, coins);
        Bukkit.getServer().getPluginManager().callEvent(coinEvent);
        if (!coinEvent.isCancelled() && !(actualCoins < coins))
            setCoins(actualCoins - coins);

    }
}
