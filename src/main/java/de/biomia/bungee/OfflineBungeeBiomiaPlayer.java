package de.biomia.bungee;

import de.biomia.universal.MySQL;
import de.biomia.universal.UniversalBiomiaPlayer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class OfflineBungeeBiomiaPlayer extends UniversalBiomiaPlayer {

    public OfflineBungeeBiomiaPlayer(int biomiaID) {
        super(biomiaID);
    }

    public OfflineBungeeBiomiaPlayer(int biomiaID, String name) {
        super(biomiaID, name);
    }

    public OfflineBungeeBiomiaPlayer(int biomiaID, UUID uuid) {
        super(biomiaID, uuid);
    }

    public void sendMessage(TextComponent component) {
        if (isOnline())
            getProxiedPlayer().sendMessage(component);
    }

    public void sendMessage(String string) {
        if (isOnline())
            getProxiedPlayer().sendMessage(new TextComponent(string));
    }

    // GETTERS AND SETTERS
    public final ProxiedPlayer getProxiedPlayer() {
        return BungeeCord.getInstance().getPlayer(getName());
    }

    public boolean isOnline() {
        return getProxiedPlayer() != null;
    }

    public final void takeCoins(int coins) {

        //TODO add BungeeEvent for Stats (Coins)

        int actualCoins = getCoins();

        if (actualCoins < coins && isOnline()) {
            sendMessage("Du hast nicht genug BC! Dir fehlen noch " + (actualCoins - coins) + " BC!");
            return;
        }

        setCoins(actualCoins - coins);

    }

    public void addCoins(int coins, boolean enableBoost) {

        //TODO add BungeeEvent for Stats (Coins)

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

        setCoins(getCoins() + coins);
        if (isOnline()) {
            sendMessage("\u00A77Du erh\u00e4ltst \u00A7f" + coins + "\u00A77 BC!");
        }
    }
}