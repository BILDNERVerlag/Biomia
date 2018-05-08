package de.biomia.spigot;

import de.biomia.spigot.events.coins.CoinAddEvent;
import de.biomia.spigot.events.coins.CoinTakeEvent;
import de.biomia.universal.UniversalBiomiaPlayer;
import org.bukkit.Bukkit;

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

    // GETTERS AND SETTERS
    public final BiomiaPlayer getBiomiaPlayer() {
        return this instanceof BiomiaPlayer ? (BiomiaPlayer) this : Biomia.getBiomiaPlayer(org.bukkit.Bukkit.getPlayer(getName()));
    }

    public boolean isOnline() {
        return getBiomiaPlayer() != null;
    }

    public final void takeCoins(int coins) {
        CoinTakeEvent coinEvent = new CoinTakeEvent(this, coins);
        Bukkit.getServer().getPluginManager().callEvent(coinEvent);
        if (coinEvent.isCancelled())
            return;
        super.takeCoins(coins);
    }

    @Override
    public void addCoins(int coins, boolean enableBoost) {
        int prozent = 100;
        if (enableBoost)
            prozent = getBoostInPercent();
        if (prozent != 100) {
            double coinsDouble = (double) coins / 100 * prozent;
            coins = (int) coinsDouble;
        }
        CoinAddEvent coinEvent = new CoinAddEvent(this, coins);
        Bukkit.getServer().getPluginManager().callEvent(coinEvent);
        if (coinEvent.isCancelled())
            return;
        super.addCoins(coins, enableBoost);
    }
}
