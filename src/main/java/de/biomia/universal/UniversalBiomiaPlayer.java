package de.biomia.universal;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public abstract class UniversalBiomiaPlayer {

    private static final LuckPermsApi api = LuckPerms.getApi();
    private User permUser;

    private final int biomiaID;
    private String name;
    private UUID uuid;

    private UniversalBiomiaPlayer(int biomiaID, String name, UUID uuid) {
        if (biomiaID == -1) {
            new BiomiaIDCantBeMinusOneException().printStackTrace();
        }
        this.biomiaID = biomiaID;
        this.name = name;
        this.uuid = uuid;
        if (name != null)
            permUser = api.getUser(getName());
    }

    protected UniversalBiomiaPlayer(int biomiaID) {
        this(biomiaID, null, null);
    }

    protected UniversalBiomiaPlayer(int biomiaID, String name) {
        this(biomiaID, name, null);
    }

    protected UniversalBiomiaPlayer(int biomiaID, UUID uuid) {
        this(biomiaID, null, uuid);
    }

    public static int getBiomiaPlayerID(UUID uuid) {
        return MySQL.executeQuerygetint("Select id from BiomiaPlayer where uuid = '" + uuid.toString() + "'", "id", MySQL.Databases.biomia_db);
    }

    public static int getBiomiaPlayerID(String playerName) {
        return MySQL.executeQuerygetint("Select id from BiomiaPlayer where name = '" + playerName + "'", "id", MySQL.Databases.biomia_db);
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

    private void stopCoinBoost() {
        MySQL.executeUpdate("DELETE FROM `CoinBoost` WHERE BiomiaPlayer = " + biomiaID, MySQL.Databases.biomia_db);
    }

    protected final int getBoostInPercent() {
        int prozent = 100;
        try {
            PreparedStatement ps = MySQL.Connect(MySQL.Databases.biomia_db).prepareStatement("SELECT `percent`, `until` FROM `CoinBoost` WHERE BiomiaPlayer = ?");
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
            return prozent;
        }
        return prozent;
    }

    public void takeCoins(int coins) {
        int actualCoins = getCoins();
        if (actualCoins < coins && isOnline())
            sendMessage(String.format("%s%sDu hast nicht genug BC! Dir fehlen noch %s%d%s BC!", Messages.PREFIX, Messages.COLOR_MAIN, Messages.COLOR_SUB, actualCoins - coins, Messages.COLOR_MAIN));
        setCoins(actualCoins - coins);
    }

    public void addCoins(int coins, boolean enableBoost) {
        setCoins(getCoins() + coins);
        if (isOnline()) {
            sendMessage(String.format("%sDu erhältst %s%d%s BC!", Messages.COLOR_SUB, Messages.COLOR_AUX, coins, Messages.COLOR_SUB));
        }
    }

    protected abstract boolean isOnline();

    protected abstract void sendMessage(String message);

    // GETTERS AND SETTERS

    public final UUID getUUID() {
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

    // rank methods

    public final boolean isStaff() {
        return (getRank() != Ranks.YouTube && getRank() != Ranks.RegSpieler && getRank() != Ranks.UnregSpieler && !getRank().isPremium());
    }

    public final boolean isJrStaff() {
        return (getRank() == Ranks.JrBuilder || getRank() == Ranks.Supporter);
    }

    public final boolean isSrStaff() {
        return (isOwnerOrDev() || getRank() == Ranks.Admin || getRank() == Ranks.SrBuilder || getRank() == Ranks.SrModerator || getRank() == Ranks.TestAccount);

    }

    public final boolean isYouTuber() {
        return (getRank() == Ranks.YouTube);
    }

    public final boolean isOwnerOrDev() {
        return (getRank() == Ranks.Owner || getRank() == Ranks.Developer || getRank() == Ranks.TestAccount);
    }

    public boolean isPremium() {
        return getRank().isPremium();
    }

    public boolean isSupporter() {
        return (getRank() == Ranks.Supporter);
    }

    public boolean isModerator() {
        return (getRank() == Ranks.Moderator);
    }

    public int getPremiumLevel() {
        if (getRank().isPremium())
            return getRank().getLevel() - 1;
        else return 0;
    }

    public Ranks getRank() {
        String rank = permUser.getPrimaryGroup();
        if (rank.equals("default")) {
            setRank(Ranks.UnregSpieler);
            return Ranks.UnregSpieler;
        }
        for (Ranks ranks : Ranks.values()) {
            if (ranks.getName().equals(rank))
                return ranks;
        }
        return Ranks.UnregSpieler;
    }

    public void setRank(Ranks rank) {
        String oldRank = permUser.getPrimaryGroup();

        if (permUser.hasPermission(api.buildNode("group." + oldRank).build()).asBoolean())
            removePermission("group." + oldRank);
        addPermission("group." + rank.getName());
        permUser.setPrimaryGroup(rank.getName());

    }

    public void removePermission(String permission) {
        permUser.unsetPermission(LuckPerms.getApi().buildNode(permission).build());
        LuckPerms.getApi().getUserManager().saveUser(permUser);
    }

    public void addPermission(String permission) {
        permUser.setPermission(LuckPerms.getApi().buildNode(permission).build());
        LuckPerms.getApi().getUserManager().saveUser(permUser);
    }
}

