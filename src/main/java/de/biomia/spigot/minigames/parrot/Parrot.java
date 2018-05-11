package de.biomia.spigot.minigames.parrot;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.configs.ParrotConfig;
import de.biomia.spigot.messages.ParrotItemNames;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.server.quests.QuestEvents.GiveItemEvent;
import de.biomia.spigot.server.quests.QuestEvents.TakeItemEvent;
import de.biomia.spigot.tools.TeleportExecutor;
import de.biomia.spigot.tools.Teleporter;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.UUID;

public class Parrot extends GameMode {


    private final HashMap<Teleporter, Boolean> teleportersMap = new HashMap<>();

    public Parrot(GameInstance instance) {
        super(instance);

        TeleportExecutor goInside = new TeleportExecutor() {
            @Override
            public void execute(BiomiaPlayer bp, Teleporter teleporter) {
                new GiveItemEvent(Material.BOW, ParrotItemNames.explosionBow, 1).executeEvent(bp);
            }
        };
        TeleportExecutor goOutside = new TeleportExecutor() {
            @Override
            public void execute(BiomiaPlayer bp, Teleporter teleporter) {
                new TakeItemEvent(Material.BOW, ParrotItemNames.explosionBow, 1).executeEvent(bp);
            }
        };

        teleportersMap.put(new Teleporter(new Location(instance.getWorld(), -23, 75, -49), new Location(instance.getWorld(), -20, 77, -46), goInside), false);
        teleportersMap.put(new Teleporter(new Location(instance.getWorld(), -23, 75, -49), new Location(instance.getWorld(), -20, 77, -46), goOutside).setInverted(), false);

        teleportersMap.put(new Teleporter(new Location(instance.getWorld(), 68, 75, -49), new Location(instance.getWorld(), 65, 77, -46), goInside), false);
        teleportersMap.put(new Teleporter(new Location(instance.getWorld(), 68, 75, -49), new Location(instance.getWorld(), 65, 77, -46), goOutside).setInverted(), false);
    }

    @Override
    public void stop() {
        teleportersMap.keySet().forEach(Teleporter::removeTeleporter);
        super.stop();
    }

    @Override
    protected GameHandler initHandler() {
        return new ParrotHandler(this);
    }

    @Override
    protected HashMap<TeamColor, UUID> initTeamJoiner() {
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

    @Override
    protected ParrotConfig initConfig() {
        return new ParrotConfig(this);
    }

}