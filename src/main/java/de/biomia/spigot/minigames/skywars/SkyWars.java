package de.biomia.spigot.minigames.skywars;

import de.biomia.spigot.Main;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.configs.SkyWarsConfig;
import de.biomia.spigot.minigames.*;
import de.biomia.spigot.minigames.general.chests.Chests;
import de.biomia.spigot.minigames.general.kits.KitManager;

import java.util.HashMap;
import java.util.UUID;

import static de.biomia.spigot.configs.Config.saveConfig;

public class SkyWars extends GameMode {

    private final Chests chests;

    @Override
    protected GameHandler initHandler() {
        return new SkyWarsListener(this);
    }

    @Override
    protected HashMap<TeamColor, UUID> initTeamJoiner() {
        if (getInstance().getType().isVersus())
            return null;
        HashMap<TeamColor, UUID> teamJoiner = new HashMap<>();
        teamJoiner.put(TeamColor.BLACK, UUID.fromString("a86b152d-9cdf-4265-97e4-eb03369024db"));
        teamJoiner.put(TeamColor.ORANGE, UUID.fromString("c1de65f4-17c9-4b78-8186-0832cc259480"));
        teamJoiner.put(TeamColor.BLUE, UUID.fromString("0e21910c-93a5-464f-a130-aeb3d10fba6f"));
        teamJoiner.put(TeamColor.GREEN, UUID.fromString("2db0b7da-44eb-4fb4-b63a-67973ad9871c"));
        teamJoiner.put(TeamColor.YELLOW, UUID.fromString("27879288-7413-456b-980d-946c077d245f"));
        teamJoiner.put(TeamColor.RED, UUID.fromString("d02a1e13-9d2f-4951-90d5-2cfeb86447e4"));
        teamJoiner.put(TeamColor.WHITE, UUID.fromString("40b9abcf-6707-4f5f-88d7-06e46c595027"));
        teamJoiner.put(TeamColor.PURPLE, UUID.fromString("a3e3ea4f-fef4-47e5-aca2-575a8a7a5b68"));
        return teamJoiner;
    }

    public SkyWars(GameInstance instance) {
        super(instance);
        chests = new Chests(this);
    }

    @Override
    public void start() {
        super.start();

        Main.getPlugin().saveDefaultConfig();
        saveConfig();

        getStateManager().setInGameState(new GameStateManager.InGameState(this) {
            @Override
            public void start() {
                getMode().getInstance().getPlayers().forEach(bp -> KitManager.getManager(bp).setKitInventory());
                super.start();
            }
        });
    }

    public Chests getChests() {
        return chests;
    }

    @Override
    protected MinigamesConfig initConfig() {
        return new SkyWarsConfig(this);
    }

}
