package de.biomia.spigot.server.lobby;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.objects.ServerObject;
import de.biomia.spigot.Main;
import de.biomia.spigot.commands.general.RandomServerGroupCommand;
import de.biomia.spigot.listeners.LobbyInventoryManager;
import de.biomia.spigot.listeners.servers.LobbyListener;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.LastPositionListener;
import de.biomia.spigot.tools.Teleporter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Comparator;

import static de.biomia.spigot.Main.getPlugin;

public class Lobby {

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

    public static void init() {
        Bukkit.getServer().createWorld(new WorldCreator("BedwarsSignlobby"));
        Bukkit.getServer().createWorld(new WorldCreator("SkywarsSignlobby"));
        for (Player p : Bukkit.getOnlinePlayers()) {
            LobbyScoreboard.sendScoreboard(p);
            p.setAllowFlight(true);
        }

        Bukkit.setDefaultGameMode(GameMode.ADVENTURE);

        Main.registerCommand(new RandomServerGroupCommand());

        Bukkit.getPluginManager().registerEvents(new LobbyListener(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new LobbyInventoryManager(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new LastPositionListener(), getPlugin());

        // BauWelt Portal
        new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 559, 76, 286), new Location(Bukkit.getWorld("LobbyBiomia"), 562, 77, 289), new Location(Bukkit.getWorld("LobbyBiomia"), 551.5, 80, 285.5, -90, 0), "BauServer");

        Location backTeleportationQuests = new Location(Bukkit.getWorld("LobbyBiomia"), 480.5, 123, 359.5, 90, 0);

        // Quest Portals
        new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 446, 124, 359), new Location(Bukkit.getWorld("LobbyBiomia"), 447, 125, 361), backTeleportationQuests, "QuestServer");
        new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 459, 124, 377), new Location(Bukkit.getWorld("LobbyBiomia"), 460, 125, 378), backTeleportationQuests, "QuestServer");
        new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 459, 124, 341), new Location(Bukkit.getWorld("LobbyBiomia"), 460, 125, 342), backTeleportationQuests, "QuestServer");

        // FreeBuild Portal
        new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 552, 97, 290), new Location(Bukkit.getWorld("LobbyBiomia"), 553, 98, 291), new Location(Bukkit.getWorld("LobbyBiomia"), 556.5, 96, 292.5, -90, 0), "FreebuildServer");
        // FarmWelt Portal
        // new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 552, 97, 294), new Location(Bukkit.getWorld("LobbyBiomia"), 553, 98, 295), new Location(Bukkit.getWorld("LobbyBiomia"), 556.5, 96, 292.5, -90, 0), "FarmServer");

        // SkyWars Portal
        new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 459.5, 71, 264), new Location(Bukkit.getWorld("LobbyBiomia"), 460, 73, 269), new Location(Bukkit.getWorld("SkywarsSignlobby"), 370.5, 82, 264.5, 70, 0));
        // BedWars Portal
        new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 459.5, 71, 252), new Location(Bukkit.getWorld("LobbyBiomia"), 460, 73, 257), new Location(Bukkit.getWorld("BedwarsSignlobby"), 370.5, 82, 264.5, 70, 0));

        //GRENZEN
        new Teleporter(new Location(Bukkit.getWorld("BedwarsSignlobby"), 0, -1000, 0), new Location(Bukkit.getWorld("BedwarsSignlobby"), 800, 1000, 1024), new Location(Bukkit.getWorld("BedwarsSignlobby"), 370.5, 82, 264.5, 70, 0)).setInverted();
        new Teleporter(new Location(Bukkit.getWorld("SkywarsSignlobby"), 0, -1000, 0), new Location(Bukkit.getWorld("SkywarsSignlobby"), 800, 1000, 1024), new Location(Bukkit.getWorld("SkywarsSignlobby"), 370.5, 82, 264.5, 70, 0)).setInverted();

        new Teleporter(new Location(Bukkit.getWorld("LobbyBiomia"), 360, -1000, 150), new Location(Bukkit.getWorld("LobbyBiomia"), 800, 1000, 700), new Location(Bukkit.getWorld("LobbyBiomia"), 534.5, 67.5, 193.5)).setInverted();


        navigator = Bukkit.createInventory(null, 27, "\u00A7cNavigator");
        // First Line
        navigator.setItem(2, ItemCreator.itemCreate(Material.WORKBENCH, "\u00A76Bau Welt"));
        navigator.setItem(4, ItemCreator.itemCreate(Material.DRAGON_EGG, "\u00A75Quests"));
        navigator.setItem(6, ItemCreator.itemCreate(Material.THIN_GLASS, "\u00A7eDemo Welt", Short.valueOf("3")));
        // Second Line
        navigator.setItem(12, ItemCreator.itemCreate(Material.MAGMA_CREAM, "\u00A7cSpawn"));
        navigator.setItem(14, ItemCreator.itemCreate(Material.IRON_SWORD, "\u00A76Freebuild Welt"));
        // Third Line
        navigator.setItem(20, ItemCreator.itemCreate(Material.BED, "\u00A74BedWars"));
        navigator.setItem(22, ItemCreator.itemCreate(Material.CHEST, "\u00A75Mysteri\u00F6se Box"));
        navigator.setItem(24, ItemCreator.itemCreate(Material.GRASS, "\u00A7bSkyWars"));

        lobbySwitcher = Bukkit.createInventory(null, 27, "\u00A7dLobby Switcher");

        new BukkitRunnable() {
            @Override
            public void run() {

                ServerObject serverObject = TimoCloudAPI.getBukkitInstance().getThisServer();

                ArrayList<ServerObject> lobbyServer = new ArrayList<>(
                        serverObject.getGroup().getServers());
                lobbyServer.sort(Comparator.comparing(ServerObject::getName));

                int i = 0;

                for (ServerObject so : lobbyServer) {
                    int amount = so.getOnlinePlayerCount();
                    if (amount == 0)
                        amount = 1;
                    if (serverObject.getName().equalsIgnoreCase(so.getName()))
                        lobbySwitcher.setItem(i, ItemCreator
                                .setAmount(ItemCreator.itemCreate(Material.SUGAR, "\u00A76" + so.getName()), amount));
                    else
                        lobbySwitcher.setItem(i, ItemCreator
                                .setAmount(ItemCreator.itemCreate(Material.SULPHUR, "\u00A7e" + so.getName()), amount));
                    i++;
                }
            }
        }.runTaskTimer(getPlugin(), 100, 200);
    }
}