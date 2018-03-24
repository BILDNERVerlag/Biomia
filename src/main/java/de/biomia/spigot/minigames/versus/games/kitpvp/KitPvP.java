package de.biomia.spigot.minigames.versus.games.kitpvp;

import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.TeamColor;

import java.util.HashMap;
import java.util.UUID;

public class KitPvP extends GameMode {

    public KitPvP(GameInstance instance) {
        super(instance);
    }

    @Override
    public void start() {
    }

    @Override
    protected GameHandler initHandler() {
        return null;
    }

    @Override
    protected HashMap<TeamColor, UUID> initTeamJoiner() {
        return null;
    }

    @Override
    protected MinigamesConfig initConfig() {
        return null;
    }
}
