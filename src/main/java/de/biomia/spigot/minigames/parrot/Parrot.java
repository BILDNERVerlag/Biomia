package de.biomia.spigot.minigames.parrot;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.configs.ParrotConfig;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.messages.ParrotItemNames;
import de.biomia.spigot.minigames.*;
import de.biomia.spigot.minigames.general.shop.Shop;
import de.biomia.spigot.server.quests.QuestEvents.GiveItemEvent;
import de.biomia.spigot.server.quests.QuestEvents.TakeItemEvent;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.TeleportExecutor;
import de.biomia.spigot.tools.Teleporter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBook;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Parrot extends GameMode {

    private static final ItemStack bow = ItemCreator.itemCreate(Material.BOW, ParrotItemNames.explosionBow);

    private final ArrayList<ParrotCannonPoint> points = new ArrayList<>();
    private final ArrayList<Teleporter> shootingStands = new ArrayList<>();

    private final int goldSpawnDelay = 30;
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
            public void execute(BiomiaPlayer bp) {
                new GiveItemEvent(Material.ARROW, 1).executeEvent(bp);
                new GiveItemEvent(bow).executeEvent(bp);
            }
        };
        TeleportExecutor goOutside = new TeleportExecutor() {
            @Override
            public void execute(BiomiaPlayer bp) {
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
                getMode().getTeams().forEach(gameTeam -> ((ParrotTeam) gameTeam).getShip().update());

                for (BiomiaPlayer bp : getMode().getInstance().getPlayers()) {
                    bp.getPlayer().getInventory().clear();
                    bp.getPlayer().getInventory().addItem(getBook());
                }
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

    public static ItemStack getBook() {
        ItemStack book = ItemCreator.itemCreate(Material.WRITTEN_BOOK, "Anleitung");
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        List<IChatBaseComponent> pages;

        // Referenz auf die Liste der Buchseiten holen
        try {
            pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta);
            pages.clear();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }

        // Bonusseite befuellen (Optionen, Stats, etc)
        TextComponent text = new TextComponent("§1§l§nPirateWars\n\n" +
                "Zerstöre das " +
                "gegnerische Schiff! Bring es auf 0% oder zerstöre alle Kanonen! " +
                "Du kannst bei den " +
                "Dorfbewohnern oben " +
                "Items kaufen und bei " +
                "den Kanonieren die " +
                "Kanonen einstellen " +
                "und verbessern.");

        IChatBaseComponent page = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(text));
        pages.add(page);

        TextComponent text2 = new TextComponent("§1§l§nGold\n\n" +
                "Du bekommst für " +
                "zerstörte Kanonen Gold und auch für " +
                "jeden zerstörten Block " +
                "gibt es eine Chance auf Gold. " +
                "Schau auch in der Schatzkammer! " +
                "Kanonen zerstörst du indem du " +
                "den Block mit Knopf abbaust.");

        IChatBaseComponent page2 = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(text2));
        pages.add(page2);

        // Buch-Itemstack updaten
        bookMeta.setAuthor("BIOMIA");
        book.setItemMeta(bookMeta);
        return book;
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

    private void startSpawning(GameInstance instance) {

        final ItemStack gold = ItemCreator.itemCreate(Material.GOLD_INGOT, BedWarsItemNames.gold);
        goldSpawner = new BukkitRunnable() {

            int i = 0;
            final World world = instance.getWorld();
            final Location goldSpawnerRed = new Location(instance.getWorld(), -44, 69, -27);
            final Location goldSpawnerBlue = new Location(instance.getWorld(), 36, 69, 23);

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

    private void stopSpawning() {
        goldSpawner.cancel();
    }

}