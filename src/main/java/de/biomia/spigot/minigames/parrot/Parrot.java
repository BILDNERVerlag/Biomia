package de.biomia.spigot.minigames.parrot;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.configs.ParrotConfig;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.messages.ParrotItemNames;
import de.biomia.spigot.minigames.*;
import de.biomia.spigot.minigames.general.shop.ItemType;
import de.biomia.spigot.minigames.general.shop.Shop;
import de.biomia.spigot.server.quests.QuestEvents.GiveItemEvent;
import de.biomia.spigot.server.quests.QuestEvents.TakeItemEvent;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.TeleportExecutor;
import de.biomia.spigot.tools.Teleporter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Parrot extends GameMode {

    private static final ItemStack bow = ItemCreator.itemCreate(Material.BOW, ParrotItemNames.explosionBow);

    private final ArrayList<ParrotCannonPoint> points = new ArrayList<>();
    private final ArrayList<Teleporter> shootingStands = new ArrayList<>();

    private final int goldSpawnDelay = 100;
    private BukkitTask goldSpawner = null;

    static {
        bow.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        ItemMeta meta = bow.getItemMeta();
        meta.setUnbreakable(true);
        bow.setItemMeta(meta);
    }

    public Parrot(GameInstance instance) {
        super(instance);

        TeleportExecutor goInside = new TeleportExecutor() {
            @Override
            public void execute(BiomiaPlayer bp, Teleporter teleporter) {
                new GiveItemEvent(Material.ARROW, 1).executeEvent(bp);
                new GiveItemEvent(bow).executeEvent(bp);
            }
        };
        TeleportExecutor goOutside = new TeleportExecutor() {
            @Override
            public void execute(BiomiaPlayer bp, Teleporter teleporter) {
                new TakeItemEvent(Material.BOW, ParrotItemNames.explosionBow, 1).executeEvent(bp);
                new TakeItemEvent(Material.ARROW, 1).executeEvent(bp);
            }
        };

        getStateManager().setInGameState(new GameStateManager.InGameState(this) {
            @Override
            public void start() {
                super.start();
                Shop.initParrot();
                getMode().getTeams().stream().map(team -> ((ParrotTeam) team).getShip()).forEach(ParrotShip::setPlayersToBossBar);
                startSpawning(instance);
            }
        });

        //blue ship
        shootingStands.add(new Teleporter(new Location(instance.getWorld(), -48, 100, -21), new Location(instance.getWorld(), -40, 103, -12), goInside));
        shootingStands.add(new Teleporter(new Location(instance.getWorld(), -48, 100, -21), new Location(instance.getWorld(), -40, 103, -12), goOutside).setInverted());

        shootingStands.add(new Teleporter(new Location(instance.getWorld(), -48, 97, -49), new Location(instance.getWorld(), -40, 100, -39), goInside));
        shootingStands.add(new Teleporter(new Location(instance.getWorld(), -48, 97, -49), new Location(instance.getWorld(), -40, 100, -39), goOutside).setInverted());

        shootingStands.add(new Teleporter(new Location(instance.getWorld(), -47, 101, 5), new Location(instance.getWorld(), -41, 104, 12), goInside));
        shootingStands.add(new Teleporter(new Location(instance.getWorld(), -47, 101, 5), new Location(instance.getWorld(), -41, 104, 12), goOutside).setInverted());

        //red ship
        shootingStands.add(new Teleporter(new Location(instance.getWorld(), 34, 104, -16), new Location(instance.getWorld(), 38, 101, -8), goInside));
        shootingStands.add(new Teleporter(new Location(instance.getWorld(), 34, 104, -16), new Location(instance.getWorld(), 38, 101, -8), goOutside).setInverted());

        shootingStands.add(new Teleporter(new Location(instance.getWorld(), 33, 103, 9), new Location(instance.getWorld(), 39, 100, 18), goInside));
        shootingStands.add(new Teleporter(new Location(instance.getWorld(), 33, 103, 9), new Location(instance.getWorld(), 39, 100, 18), goOutside).setInverted());

        shootingStands.add(new Teleporter(new Location(instance.getWorld(), 33, 100, 35), new Location(instance.getWorld(), 39, 97, 45), goInside));
        shootingStands.add(new Teleporter(new Location(instance.getWorld(), 33, 100, 35), new Location(instance.getWorld(), 39, 97, 45), goOutside).setInverted());
    }

    public void registerPoint(ParrotCannonPoint parrotCannonPoint) {
        points.add(parrotCannonPoint);
    }

    public ArrayList<ParrotCannonPoint> getPoints() {
        return points;
    }

    @Override
    public void stop() {
        shootingStands.forEach(Teleporter::removeTeleporter);
        shootingStands.clear();
        stopSpawning();
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

    //gold spawner code

    public void startSpawning(GameInstance instance) {

        final ItemStack gold = ItemCreator.itemCreate(Material.GOLD_INGOT, BedWarsItemNames.gold);
        goldSpawner = new BukkitRunnable() {

            int i = 0;
            World world = instance.getWorld();
            Location goldSpawnerRed = new Location(instance.getWorld(), -44, 69, -27);
            Location goldSpawnerBlue = new Location(instance.getWorld(), 36, 69, 23);

            @Override
            public void run() {
                if (i % goldSpawnDelay == 0) {
                    world.dropItem(goldSpawnerBlue, gold);
                    world.dropItem(goldSpawnerRed, gold);
                }
                i++;
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }

    public void stopSpawning() {
        goldSpawner.cancel();
    }

}