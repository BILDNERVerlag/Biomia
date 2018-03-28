package de.biomia.spigot.minigames.versus.games.bedwars;

import de.biomia.spigot.configs.BedWarsConfig;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.minigames.bedwars.BedWarsListener;
import de.biomia.spigot.minigames.bedwars.BedWarsTeam;
import de.biomia.spigot.minigames.versus.Versus;

import java.util.HashMap;
import java.util.UUID;

public class VersusBedWars extends GameMode {

    @Override
    protected GameHandler initHandler() {
        return new BedWarsListener(this);
    }

    @Override
    protected HashMap<TeamColor, UUID> initTeamJoiner() {
        return null;
    }

    private final BedWarsShopListener shop = new BedWarsShopListener(this);

    public VersusBedWars(GameInstance instance) {
        super(instance);
        new BedWarsTeam(TeamColor.BLUE, this);
        new BedWarsTeam(TeamColor.RED, this);
    }

    @Override
    public void stop() {
        super.stop();
        shop.unregister();
        Versus.getInstance().getManager().getRequests().get(getInstance()).finish();
    }

    @Override
    protected MinigamesConfig initConfig() {
        return new BedWarsConfig(this);
    }


}
