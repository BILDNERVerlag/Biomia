package de.biomia.api;

import de.biomia.api.mysql.MySQL;
import de.biomia.api.pex.Rank;

import java.util.UUID;

class OfflineBiomiaPlayer {

    private final int biomiaPlayerID;
    private String name;
    private UUID uuid;

    public OfflineBiomiaPlayer(int biomiaPlayerID) {
        this.biomiaPlayerID = biomiaPlayerID;
    }

    public int getID() {
        return biomiaPlayerID;
    }

    public UUID getUuid() {
        return uuid != null ? uuid : (uuid = BiomiaPlayer.getUUID(biomiaPlayerID));
    }

    private String getName() {
        return name != null ? name : (name = BiomiaPlayer.getName(biomiaPlayerID));
    }

//    public int getCoins() {
//        return Coins.getCoins(this);
//    }
//
//    public void setCoins(int coins) {
//        Coins.setCoins(coins, this);
//    }
//
//    public void takeCoins(int coins) {
//        Coins.takeCoins(coins, this);
//    }

    private void stopCoinBoost() {
        MySQL.executeUpdate("DELETE FROM `CoinBoost` WHERE BiomiaPlayer = " + biomiaPlayerID, MySQL.Databases.biomia_db);
    }

    public void giveBoost(int percent, int timeinseconds) {
        stopCoinBoost();
        MySQL.executeUpdate("INSERT INTO `CoinBoost`(`BiomiaPlayer`, `percent`, `until`) VALUES (" + biomiaPlayerID
                + "," + percent + "," + timeinseconds + (System.currentTimeMillis() / 1000) + ")", MySQL.Databases.biomia_db);
    }

//    public void addCoins(int coins, boolean enableBoost) {
//        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
//        int prozent = 100;
//        if (enableBoost)
//            try {
//                assert con != null;
//                PreparedStatement ps = con
//                        .prepareStatement("SELECT `percent`, `until` FROM `CoinBoost` WHERE BiomiaPlayer = ?");
//                ps.setInt(1, getBiomiaPlayerID());
//                ResultSet rs = ps.executeQuery();
//                if (rs.next()) {
//                    long until = rs.getLong("until");
//                    if (System.currentTimeMillis() / 1000 > until) {
//                        prozent = rs.getInt("percent");
//                    } else {
//                        stopCoinBoost();
//                    }
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                return;
//            }
//        if (prozent != 100) {
//            double coinsDouble = (double) coins / 100 * prozent;
//            coins = (int) coinsDouble;
//        }
//        Coins.addCoins(coins, this);
//    }

    public boolean isPremium() {
        return Rank.isPremium(getName());
    }

    public boolean isStaff() {
        String rank = Rank.getRank(getName());
        rank = rank.toLowerCase();
        return (rank.contains("Owner") || rank.contains("Admin") || rank.contains("Moderator") || rank.contains("Builder") || rank.contains("Supporter"));
    }

    public boolean isOwner() {
        String rank = Rank.getRank(getName());
        return rank.contains("Owner");
    }

    public boolean isYouTuber() {

        String rank = Rank.getRank(getName());

        return rank.contains("YouTube");

    }

    public int getPremiumLevel() {
        return Rank.getPremiumLevel(getName());
    }
}
