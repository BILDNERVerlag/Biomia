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
import org.bukkit.Bukkit;
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
        Bukkit.getPluginManager().registerEvents(new Move(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new LastPositionListener(), getPlugin());

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