package de.biomia.spigot.minigames.bedwars;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.configs.BedWarsConfig;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.minigames.*;
import de.biomia.spigot.minigames.general.SpawnItems;
import de.biomia.spigot.minigames.general.shop.Shop;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class BedWars extends GameMode {

    @Override
    protected GameHandler initHandler() {
        return new BedWarsHandler(this);
    }

    @Override
    protected HashMap<TeamColor, UUID> initTeamJoiner() {
        if (getInstance().getType().isVersus())
            return null;
        HashMap<TeamColor, UUID> teamJoiner = new HashMap<>();
        teamJoiner.put(TeamColor.BLACK, UUID.fromString("92af2252-1787-4dc2-bb52-a9bd012865a2"));
        teamJoiner.put(TeamColor.ORANGE, UUID.fromString("d33e1331-3390-4c27-82e3-9095a3614610"));
        teamJoiner.put(TeamColor.BLUE, UUID.fromString("f75b9a9d-d122-4a70-b542-aeb69b59c61c"));
        teamJoiner.put(TeamColor.GREEN, UUID.fromString("ff626803-fa9e-47b0-ad18-d2334098376d"));
        teamJoiner.put(TeamColor.YELLOW, UUID.fromString("056ae035-7a07-470e-87c9-b4fe14c620d4"));
        teamJoiner.put(TeamColor.RED, UUID.fromString("0fdfcda5-24e9-4aae-89d7-ddfde06ae0ce"));
        teamJoiner.put(TeamColor.WHITE, UUID.fromString("23126ec9-9288-4d9e-96ea-9a9d93e1e96c"));
        teamJoiner.put(TeamColor.PURPLE, UUID.fromString("7d4309d7-344c-410e-80ef-2db2724a814b"));
        return teamJoiner;
    }

    public final HashMap<GameTeam, ArrayList<Block>> teamChestsLocs = new HashMap<>();
    public final HashMap<UUID, ArrayList<Player>> handlerMap = new HashMap<>();
    public final HashMap<BiomiaPlayer, Location> starts = new HashMap<>();

    public BedWars(GameInstance instance) {
        super(instance);
    }

    @Override
    public void start() {

        Shop.initBW();
        getStateManager().setInGameState(new GameStateManager.InGameState(this) {

            private SpawnItems items;

            @Override
            public void start() {
                super.start();

                for (BiomiaPlayer bp : getMode().getInstance().getPlayers()) {
                    bp.setInBuildmode(true);
                }
                items = new SpawnItems(((BedWarsConfig) getConfig()).loadSpawner(), getInstance().getWorld());
                items.startSpawning();
            }

            @Override
            public void stop() {
                items.stopSpawning();
                super.stop();
            }
        });
        super.start();
    }

    public GameTeam getTeamByTeamChests(Block block) {
        for (GameTeam team : getTeams()) {
            if (teamChestsLocs.containsKey(team)) {
                for (Block b : teamChestsLocs.get(team)) {
                    if (block.equals(b)) {
                        return team;
                    }
                }
            }
        }
        return null;
    }


    @Override
    protected MinigamesConfig initConfig() {
        return new BedWarsConfig(this);
    }
}
