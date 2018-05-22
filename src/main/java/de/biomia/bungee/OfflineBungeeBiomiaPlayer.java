package de.biomia.bungee;

import de.biomia.universal.UniversalBiomiaPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
        return ProxyServer.getInstance().getPlayer(getName());
    }

    public boolean isOnline() {
        return getProxiedPlayer() != null;
    }

    public final void takeCoins(int coins) {
        //TODO add BungeeEvent for Stats (Coins)
        super.takeCoins(coins);
    }

    public void addCoins(int coins, boolean enableBoost) {
        //TODO add BungeeEvent for Stats (Coins)
        super.addCoins(coins, enableBoost);
    }
}