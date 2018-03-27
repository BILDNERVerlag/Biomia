package de.biomia.spigot;

import de.biomia.universal.MySQL;
import de.biomia.spigot.events.coins.CoinAddEvent;
import de.biomia.spigot.events.coins.CoinTakeEvent;
import de.biomia.universal.UniversalBiomiaPlayer;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OfflineBiomiaPlayer extends UniversalBiomiaPlayer {

    OfflineBiomiaPlayer(int biomiaID) {
        super(biomiaID);
    }

    OfflineBiomiaPlayer(int biomiaID, String name) {
        super(biomiaID, name);
    }

    public void sendMessage(String string) {
        if (isOnline())
            getBiomiaPlayer().getPlayer().sendMessage(string);
    }

    //RANK METHODS
    public final boolean isPremium() {
        return de.biomia.spigot.tools.RankManager.isPremium(getName());
    }

    public final boolean isStaff() {
        String rank = de.biomia.spigot.tools.RankManager.getRank(getName());
        return rank.contains("Owner") || rank.contains("Admin") || rank.contains("Builder") || rank.contains("Sup");
    }

    public final boolean isYouTuber() {
        String rank = de.biomia.spigot.tools.RankManager.getRank(getName());
        return rank.contains("YouTube");
    }

    public final boolean isOwner() {
        String rank = de.biomia.spigot.tools.RankManager.getRank(getName());
        return rank.contains("Owner");
    }

    // GETTERS AND SETTERS
    public final BiomiaPlayer getBiomiaPlayer() {
        return this instanceof BiomiaPlayer ? (BiomiaPlayer) this : Biomia.getBiomiaPlayer(org.bukkit.Bukkit.getPlayer(getName()));
    }

    public boolean isOnline() {
        return getBiomiaPlayer() != null;
    }

    public final void takeCoins(int coins) {

        int actualCoins = getCoins();

        if (actualCoins < coins && isOnline()) {
            getBiomiaPlayer().getPlayer().sendMessage("Du hast nicht genug BC! Dir fehlen noch " + (actualCoins - coins) + " BC!");
            return;
        }

        CoinTakeEvent coinEvent = new CoinTakeEvent(this, coins);
        Bukkit.getServer().getPluginManager().callEvent(coinEvent);
        if (coinEvent.isCancelled() && actualCoins < coins)
            return;

        setCoins(actualCoins - coins);

    }

    public void addCoins(int coins, boolean enableBoost) {
        int prozent = 100;
        if (enableBoost)
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
                return;
            }
        if (prozent != 100) {
            double coinsDouble = (double) coins / 100 * prozent;
            coins = (int) coinsDouble;
        }


        CoinAddEvent coinEvent = new CoinAddEvent(this, coins);
        Bukkit.getServer().getPluginManager().callEvent(coinEvent);
        if (coinEvent.isCancelled())
            return;

        setCoins(getCoins() + coins);
        if (isOnline()) {
            sendMessage("\u00A77Du erh\u00e4ltst \u00A7f" + coins + "\u00A77 BC!");
        }
    }
}
