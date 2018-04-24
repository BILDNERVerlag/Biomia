package de.biomia.spigot.minigames.versus;

import de.biomia.bungee.events.BreakException;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.versus.settings.VSRequest;

import java.util.ArrayList;

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

        ArrayList<BiomiaPlayer> allBackWard = new ArrayList<>();

        for (int i = bps.size() - 1; i >= 0; i--)
            allBackWard.add(bps.get(i));

        try {
            allBackWard.forEach(all -> allBackWard.stream().filter(all::equals).forEach(all1 -> {
                VSRequest request = new VSRequest(all, all1);
                if (request.hasSameSettings()) {
                    request.startServer();
                    remove(all);
                    remove(all1);
                    throw new BreakException();
                } else {
                    request.remove();
                }
            }));
        } catch (BreakException ignored) {
            findPair();
        }
    }
}