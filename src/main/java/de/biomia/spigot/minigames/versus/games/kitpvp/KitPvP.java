package de.biomia.spigot.minigames.versus.games.kitpvp;

import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;

public class KitPvP extends GameMode {

    public KitPvP(GameInstance instance) {
        super(instance);
    }

    @Override
    public void start() {
    }

    @Override
    protected MinigamesConfig initConfig() {
        return null;
    }
}
