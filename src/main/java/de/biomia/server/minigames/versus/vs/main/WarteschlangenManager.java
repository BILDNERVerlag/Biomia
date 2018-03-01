package de.biomia.server.minigames.versus.vs.main;

import de.biomia.BiomiaPlayer;
import de.biomia.server.minigames.versus.vs.settings.VSRequest;

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

        for (int i = bps.size() - 1; i > 0; i--)
            allBackWard.add(bps.get(i));

        for (BiomiaPlayer all : allBackWard)
            for (BiomiaPlayer all1 : allBackWard)
                if (!all.equals(all1)) {
                    VSRequest request = new VSRequest(all, all1);
                    if (request.hasSameSettings()) {
                        request.startServer();
                        remove(all);
                        remove(all1);
                        findPair();
                        return;
                    }
                }
    }
}