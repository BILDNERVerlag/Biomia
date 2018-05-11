package de.biomia.spigot.minigames.parrot;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.configs.ParrotConfig;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.tools.TeleportExecutor;
import de.biomia.spigot.tools.Teleporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Parrot extends GameMode {

    ArrayList<Teleporter> teleporters = new ArrayList<>();

    public Parrot(GameInstance instance) {
        super(instance);

        TeleportExecutor executor = new TeleportExecutor() {
            @Override
            public void execute(BiomiaPlayer bp) {/*TODO*/}
        };
    }

    @Override
    protected GameHandler initHandler() {
        return new ParrotHandler(this);
    }

    @Override
    protected HashMap<TeamColor, UUID> initTeamJoiner() {
        return null;
    }

    @Override
    protected ParrotConfig initConfig() {
        return new ParrotConfig(this);
    }

}