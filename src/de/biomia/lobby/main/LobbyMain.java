package de.biomia.lobby.main;

import at.TimoCraft.TimoCloud.api.objects.ServerObject;
import de.biomia.lobby.commands.LobbyComands;
import de.biomia.lobby.commands.SendToRandomServer;
import de.biomia.lobby.commands.WC;
import de.biomia.lobby.events.*;
import de.biomia.lobby.scoreboard.ChatColors;
import de.biomia.lobby.scoreboard.ScoreboardClass;
import de.biomiaAPI.itemcreator.ItemCreator;
import de.biomiaAPI.lastPosition.LastPositionListener;
import de.biomiaAPI.main.Main;
import de.biomiaAPI.tools.Teleporter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Comparator;

import static de.biomiaAPI.main.Main.getPlugin;

public class LobbyMain {

    private static final ArrayList<Player> silentLobby = new ArrayList<>();
    private static final ArrayList<Player> inAir = new ArrayList<>();
    private static Inventory navigator;
    private static Inventory lobbySwitcher;

    public static ArrayList<Player> getSilentLobby() {
        return silentLobby;
    }

    public static ArrayList<Player> getInAir() {
        return inAir;
    }

    public static Inventory getNavigator() {
        return navigator;
    }

    public static Inventory getLobbySwitcher() {
        return lobbySwitcher;
    }

    public static void initLobby() {
        Bukkit.getServer().createWorld(new WorldCreator("BedwarsSignlobby"));
        Bukkit.getServer().createWorld(new WorldCreator("SkywarsSignlobby"));
        for (Player p : Bukkit.getOnlinePlayers()) {
            ScoreboardClass.sendScoreboard(p);
            p.setAllowFlight(true);
        }
        getPlugin().getCommand("lobbysettings").setExecutor(new LobbyComands());
        getPlugin().getCommand("randomServerGroup").setExecutor(new SendToRandomServer());
        getPlugin().getCommand("world").setExecutor(new WC());

        Bukkit.getPluginManager().registerEvents(new Click(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new Drop(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new Interact(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new Respawn(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new HungerFull(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new Join(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new NoDamage(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new ChatColors(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new ChatEvent(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new PlayerSwapHandItems(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new DisableBlockBreakAndDamageByPlayer(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new DoubleJump(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new de.biomia.lobby.events.Inventory(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new LastPositionListener(), getPlugin());

        new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 559, 76, 286), new Location(Bukkit.getWorld("LobbyBiomia"), 562, 77, 289), new Location(Bukkit.getWorld("LobbyBiomia"), 551.5, 80, 285.5, -90, 0), "BauServer");

        Location backTeleportationQuests = new Location(Bukkit.getWorld("LobbyBiomia"), 480.5, 123, 359.5, 90, 0);

        new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 446, 124, 359), new Location(Bukkit.getWorld("LobbyBiomia"), 447, 125, 361), backTeleportationQuests, "QuestServer");
        new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 459, 124, 377), new Location(Bukkit.getWorld("LobbyBiomia"), 460, 125, 378), backTeleportationQuests, "QuestServer");
        new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 459, 124, 341), new Location(Bukkit.getWorld("LobbyBiomia"), 460, 125, 342), backTeleportationQuests, "QuestServer");
        new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 552, 97, 290), new Location(Bukkit.getWorld("LobbyBiomia"), 553, 98, 291), new Location(Bukkit.getWorld("LobbyBiomia"), 556.5, 96, 292.5, -90, 0), "FreebuildServer");
        new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 552, 97, 294), new Location(Bukkit.getWorld("LobbyBiomia"), 553, 98, 295), new Location(Bukkit.getWorld("LobbyBiomia"), 556.5, 96, 292.5, -90, 0), "FarmServer");
        new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 459.5, 71, 264), new Location(Bukkit.getWorld("LobbyBiomia"), 460, 73, 269), new Location(Bukkit.getWorld("SkywarsSignlobby"), 370.5, 82, 264.5, 70, 0));
        new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 459.5, 71, 252), new Location(Bukkit.getWorld("LobbyBiomia"), 460, 73, 257), new Location(Bukkit.getWorld("BedwarsSignlobby"), 370.5, 82, 264.5, 70, 0));

        new Teleporter(new Location(Bukkit.getWorld("BedwarsSignlobby"), 0, -1000, 0), new Location(Bukkit.getWorld("BedwarsSignlobby"), 800, 1000, 1024), new Location(Bukkit.getWorld("BedwarsSignlobby"), 370.5, 82, 264.5, 70, 0)).setInverted();
        new Teleporter(new Location(Bukkit.getWorld("SkywarsSignlobby"), 0, -1000, 0), new Location(Bukkit.getWorld("SkywarsSignlobby"), 800, 1000, 1024), new Location(Bukkit.getWorld("SkywarsSignlobby"), 370.5, 82, 264.5, 70, 0)).setInverted();
        new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 360, -1000, 150), new Location(Bukkit.getWorld("SkywarsSignlobby"), 800, 1000, 700), new Location(Bukkit.getWorld("LobbyBiomia"), 534.5, 67.5, 193.5)).setInverted();


        navigator = Bukkit.createInventory(null, 27, "§cNavigator");
        // First Line
        navigator.setItem(2, ItemCreator.itemCreate(Material.WORKBENCH, "§6Bau Welt"));
        navigator.setItem(4, ItemCreator.itemCreate(Material.DRAGON_EGG, "§5Biomia | general"));
        navigator.setItem(6, ItemCreator.itemCreate(Material.THIN_GLASS, "§eDemo Welt", Short.valueOf("3")));
        // Second Line
        navigator.setItem(12, ItemCreator.itemCreate(Material.MAGMA_CREAM, "§cSpawn"));
        navigator.setItem(14, ItemCreator.itemCreate(Material.IRON_SWORD, "§6Freebuild Welt"));
        // Third Line
        navigator.setItem(20, ItemCreator.itemCreate(Material.BED, "§4BedWars"));
        navigator.setItem(22, ItemCreator.itemCreate(Material.CHEST, "§5Mysteriöse Box"));
        navigator.setItem(24, ItemCreator.itemCreate(Material.GRASS, "§bSkyWars"));

        lobbySwitcher = Bukkit.createInventory(null, 27, "§dLobby Switcher");

        new BukkitRunnable() {
            @Override
            public void run() {

                ServerObject serverObject = Main.getBukkitTimoapi().getThisServer();

                ArrayList<ServerObject> lobbyServer = new ArrayList<>(
                        serverObject.getGroup().getServers());
                lobbyServer.sort(Comparator.comparing(ServerObject::getName));

                int i = 0;

                for (ServerObject so : lobbyServer) {
                    int amount = so.getCurrentPlayers();
                    if (amount == 0)
                        amount = 1;
                    if (serverObject.getName().equalsIgnoreCase(so.getName()))
                        lobbySwitcher.setItem(i, ItemCreator
                                .setAmount(ItemCreator.itemCreate(Material.SUGAR, "§6" + so.getName()), amount));
                    else
                        lobbySwitcher.setItem(i, ItemCreator
                                .setAmount(ItemCreator.itemCreate(Material.SULPHUR, "§e" + so.getName()), amount));
                    i++;
                }
            }
        }.runTaskTimer(getPlugin(), 100, 200);
    }
}