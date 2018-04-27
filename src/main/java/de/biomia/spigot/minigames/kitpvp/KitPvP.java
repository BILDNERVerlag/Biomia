package de.biomia.spigot.minigames.kitpvp;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.minigames.*;

import java.util.HashMap;
import java.util.UUID;

public class KitPvP extends GameMode {

    public KitPvP(GameInstance instance) {
        super(instance);
    }

    private KitPVPKit pvpKit;

    public void setLeader(BiomiaPlayer bp) {
        this.pvpKit = KitPVPManager.getMainKit(bp);
    }

    @Override
    public void start() {
        getStateManager().setInGameState(new GameStateManager.InGameState(this) {
            @Override
            public void start() {
                super.start();
                getMode().getInstance().getPlayers().forEach(each -> pvpKit.setToPlayerInventory());
            }
        });
    }

    @Override
    protected GameHandler initHandler() {
        return new KitPVPHandler(this);
    }

    @Override
    protected HashMap<TeamColor, UUID> initTeamJoiner() {
        return null;
    }

    @Override
    protected MinigamesConfig initConfig() {
        return new MinigamesConfig(this);
    }
}
