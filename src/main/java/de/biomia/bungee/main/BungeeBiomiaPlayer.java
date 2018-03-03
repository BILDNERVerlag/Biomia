package de.biomia.bungee.main;

import de.biomia.OfflineBiomiaPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeBiomiaPlayer extends OfflineBiomiaPlayer {

    private ProxiedPlayer proxiedPlayer;

    public BungeeBiomiaPlayer(ProxiedPlayer pp) {
        super(pp);
        proxiedPlayer = pp;
    }

    public ProxiedPlayer getProxiedPlayer() {
        return proxiedPlayer;
    }

}
