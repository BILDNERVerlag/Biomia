package de.biomia.spigot.minigames.versus.games.skywars;

import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.configs.SkyWarsConfig;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.minigames.general.chests.Chests;
import de.biomia.spigot.minigames.skywars.SkyWarsListener;
import de.biomia.spigot.minigames.versus.Versus;

import java.util.HashMap;
import java.util.UUID;

public class VersusSkyWars extends GameMode {

    @Override
    protected GameHandler initHandler() {
        return new SkyWarsListener(this);
    }

    @Override
    protected HashMap<TeamColor, UUID> initTeamJoiner() {
        return null;
    }

    private final Chests chests = new Chests(this);

    public VersusSkyWars(GameInstance instance) {
        super(instance);
    }

    public Chests getChests() {
        return chests;
    }

    @Override
    public void stop() {
        super.stop();
        Versus.getInstance().getManager().getRequests().get(getInstance()).finish();
    }

    @Override
    protected MinigamesConfig initConfig() {
        return new SkyWarsConfig(this);
    }
}
