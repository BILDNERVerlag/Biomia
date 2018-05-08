package de.biomia.spigot.minigames.versus;

import de.biomia.bungee.events.BreakException;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.versus.settings.VSRequest;

import java.util.ArrayList;
import java.util.Collections;

class WarteschlangenManager {

    private static final ArrayList<BiomiaPlayer> bps = new ArrayList<>();

    public static void add(BiomiaPlayer bp) {
        bps.add(bp);
        findPair();
    }

    public static void remove(BiomiaPlayer bp) {
        bps.remove(bp);
    }

    private static void findPair() {

        ArrayList<BiomiaPlayer> allBackWard = new ArrayList<>(bps);
        Collections.reverse(allBackWard);

        try {
            allBackWard.forEach(all -> allBackWard.stream().filter(all1 -> !all1.equals(all)).forEach(all1 -> {
                VSRequest request = new VSRequest(all, all1);
                if (request.hasSameSettings()) {
                    request.startServer();
                    remove(all);
                    remove(all1);
                    findPair();
                    throw new BreakException();
                } else {
                    request.remove();
                }
            }));
        } catch (BreakException ignored) {
        }
    }

    public static boolean contains(BiomiaPlayer bp) {
        return bps.contains(bp);
    }
}