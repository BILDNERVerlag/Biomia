package de.biomiaAPI;

import de.biomiaAPI.Quests.QuestPlayer;
import de.biomiaAPI.coins.Coins;
import de.biomiaAPI.main.Main;
import de.biomiaAPI.mysql.MySQL;
import de.biomiaAPI.pex.Rank;
import de.biomiaAPI.tools.UUIDFetcher;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.spigot.api.party.PartyManager;
import de.simonsator.partyandfriends.spigot.api.party.PlayerParty;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BiomiaPlayer {

    private final Player p;
    private boolean build = false;
    private boolean trollmode = false;
    private boolean getDamage = true;
    private boolean damageEntitys = true;
    private final PAFPlayer spigotPafpl;
    private final int biomiaPlayerID;
    private QuestPlayer questPlayer;

    public BiomiaPlayer(Player p) {
        biomiaPlayerID = getBiomiaPlayerID(p);
        this.p = p;
        spigotPafpl = PAFPlayerManager.getInstance().getPlayer(p.getUniqueId());
    }

    public static int getID(String playerName) {
        return MySQL.executeQuerygetint("Select id from BiomiaPlayer where name = '" + playerName + "'", "id", MySQL.Databases.biomia_db);
    }

    public static UUID getUUID(int biomiaID) {
        String s = MySQL.executeQuery("Select uuid from BiomiaPlayer where id = " + biomiaID, "uuid", MySQL.Databases.biomia_db);
        if (s != null)
            return UUID.fromString(s);
        else return null;
    }

    public static int getID(UUID uuid) {
        return MySQL.executeQuerygetint("Select id from BiomiaPlayer where uuid = '" + uuid.toString() + "'", "id", MySQL.Databases.biomia_db);
    }

    public static String getName(int biomiaID) {
        return MySQL.executeQuery("Select name from BiomiaPlayer where id = " + biomiaID, "name", MySQL.Databases.biomia_db);
    }

    private int getBiomiaPlayerID(Player p) {
        return MySQL.executeQuerygetint("Select id from BiomiaPlayer where uuid = '" + p.getUniqueId().toString() + "'",
                "id", MySQL.Databases.biomia_db);
    }

    public Player getPlayer() {
        return p;
    }

    public QuestPlayer getQuestPlayer() {
        return questPlayer != null ? questPlayer : (questPlayer = new QuestPlayer(this));
    }

    public boolean canBuild() {
        return build;
    }

    public void setBuild(boolean build) {
        this.build = build;
    }

    public boolean canGetDamage() {
        return getDamage;
    }

    public void setGetDamage(boolean getDamage) {
        this.getDamage = getDamage;
    }

    public boolean canDamageEntitys() {
        return damageEntitys;
    }

    public void setDamageEntitys(boolean damageEntitys) {
        this.damageEntitys = damageEntitys;
    }

    public int getCoins() {
        return Coins.getCoins(this);
    }

    public void takeCoins(int coins) {
        Coins.takeCoins(coins, this);
    }

    private void stopCoinBoost() {
        MySQL.executeUpdate("DELETE FROM `CoinBoost` WHERE BiomiaPlayer = " + biomiaPlayerID, MySQL.Databases.biomia_db);
    }

    public void giveBoost(int percent, int timeinseconds) {
        stopCoinBoost();
        MySQL.executeUpdate("INSERT INTO `CoinBoost`(`BiomiaPlayer`, `percent`, `until`) VALUES (" + biomiaPlayerID
                + "," + percent + "," + timeinseconds + (System.currentTimeMillis() / 1000) + ")", MySQL.Databases.biomia_db);
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

    public void setCoins(int coins) {
        Coins.setCoins(coins, this);
    }

    public List<PAFPlayer> getFriends() {
        return spigotPafpl.getFriends();
    }

    public List<PAFPlayer> getOnlineFriends() {
        List<PAFPlayer> onlineFriends = new ArrayList<>();
        for (PAFPlayer pafplayer : getFriends()) {
            if (!Main.allPlayersOnAllServer.isEmpty()) {
                if (Main.allPlayersOnAllServer.contains(pafplayer.getName()))
                    onlineFriends.add(pafplayer);
            }
        }
        return onlineFriends;
    }

    public boolean isPremium() {
        return Rank.isPremium(p);

    }

    public boolean isStaff() {
        String rank = Rank.getRank(p);
        rank = rank.toLowerCase();
        return (rank.contains("Owner") || rank.contains("Admin") || rank.contains("Moderator") || rank.contains("Builder") || rank.contains("Supporter"));
    }

    public boolean isOwner() {
        String rank = Rank.getRank(p);
        return rank.contains("Owner");
    }

    public boolean isYouTuber() {

        String rank = Rank.getRank(p);

        return rank.contains("YouTube");

    }

    public int getPremiumLevel() {
        return Rank.getPremiumLevel(p);
    }

    public PlayerParty getParty() {
        return PartyManager.getInstance().getParty(spigotPafpl);
    }

    public boolean isPartyLeader() {
        return isInParty() && spigotPafpl.equals(getParty().getLeader());
    }

    public boolean isInParty() {
        return getParty() != null;
    }

    public boolean isInTrollmode() {
        return trollmode;
    }

    public void setTrollmode(boolean trollmode) {
        this.trollmode = trollmode;
    }

    public int getBiomiaPlayerID() {
        return biomiaPlayerID;
    }
}
