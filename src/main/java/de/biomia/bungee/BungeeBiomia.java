package de.biomia.bungee;

import de.biomia.bungee.specialEvents.WinterEvent;
import de.biomia.universal.UniversalBiomiaPlayer;

import java.util.HashMap;
import java.util.UUID;

public class BungeeBiomia {

    private static final HashMap<Integer, OfflineBungeeBiomiaPlayer> offlineBungeeBiomiaPlayer = new HashMap<>();

    public static OfflineBungeeBiomiaPlayer getOfflineBiomiaPlayer(int biomiaID) {
        return offlineBungeeBiomiaPlayer.computeIfAbsent(biomiaID, biomiaplayer -> new OfflineBungeeBiomiaPlayer(biomiaID));
    }

    public static OfflineBungeeBiomiaPlayer getOfflineBiomiaPlayer(String name) {
        int biomiaPlayerID = UniversalBiomiaPlayer.getBiomiaPlayerID(name);
        return offlineBungeeBiomiaPlayer.computeIfAbsent(biomiaPlayerID, biomiaplayer -> new OfflineBungeeBiomiaPlayer(biomiaPlayerID, name));
    }

    public static OfflineBungeeBiomiaPlayer getOfflineBiomiaPlayer(UUID uuid) {
        int biomiaPlayerID = UniversalBiomiaPlayer.getBiomiaPlayerID(uuid);
        return offlineBungeeBiomiaPlayer.computeIfAbsent(biomiaPlayerID, biomiaplayer -> new OfflineBungeeBiomiaPlayer(biomiaPlayerID, uuid));
    }
}
