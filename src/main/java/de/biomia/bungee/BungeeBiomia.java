package de.biomia.bungee;

import de.biomia.universal.UniversalBiomiaPlayer;

import java.util.HashMap;
import java.util.UUID;

public class BungeeBiomia {

    private static final HashMap<Integer, OfflineBungeeBiomiaPlayer> offlineBungeeBiomiaPlayer = new HashMap<>();

    public static OfflineBungeeBiomiaPlayer getOfflineBiomiaPlayer(int biomiaID) {
        return offlineBungeeBiomiaPlayer.computeIfAbsent(biomiaID, biomiaplayer -> new OfflineBungeeBiomiaPlayer(biomiaID));
    }

    public static OfflineBungeeBiomiaPlayer getOfflineBiomiaPlayer(String name) {
        int biomiaID = UniversalBiomiaPlayer.getBiomiaPlayerID(name);
        return offlineBungeeBiomiaPlayer.computeIfAbsent(biomiaID, biomiaplayer -> new OfflineBungeeBiomiaPlayer(biomiaID, name));
    }

    public static OfflineBungeeBiomiaPlayer getOfflineBiomiaPlayer(UUID uuid) {
        int biomiaID = UniversalBiomiaPlayer.getBiomiaPlayerID(uuid);
        return offlineBungeeBiomiaPlayer.computeIfAbsent(biomiaID, biomiaplayer -> new OfflineBungeeBiomiaPlayer(biomiaID, uuid));
    }
}
