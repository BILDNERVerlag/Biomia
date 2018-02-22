package de.biomia.lobby.main;

import at.TimoCraft.TimoCloud.api.objects.ServerObject;
import de.biomia.lobby.events.*;
import de.biomia.lobby.scoreboard.ChatColors;
import de.biomia.lobby.scoreboard.ScoreboardClass;
import de.biomiaAPI.itemcreator.ItemCreator;
import de.biomiaAPI.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;

public class LobbyMain {

    private static ArrayList<Player> silentLobby = new ArrayList<>();
    private static ArrayList<Player> inAir = new ArrayList<>();

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
        init();
        setEvents();
        for (Player p : Bukkit.getOnlinePlayers()) {
            ScoreboardClass.sendScoreboard(p);
            p.setAllowFlight(true);
        }
    }

    private static void setEvents() {

        PluginManager pm = Bukkit.getPluginManager();
        Main p = Main.getPlugin();

        pm.registerEvents(new Click(), p);
        pm.registerEvents(new Drop(), p);
        pm.registerEvents(new Interact(), p);
        pm.registerEvents(new Respawn(), p);
        pm.registerEvents(new HungerFull(), p);
        pm.registerEvents(new Join(), p);
        pm.registerEvents(new NoDamage(), p);
        pm.registerEvents(new ChatColors(), p);
        pm.registerEvents(new ChatEvent(), p);
        pm.registerEvents(new PlayerSwapHandItems(), p);
        pm.registerEvents(new DisableBlockBreakAndDamageByPlayer(), p);
        pm.registerEvents(new DoubleJump(), p);
        pm.registerEvents(new de.biomia.lobby.events.Inventory(), p);
        pm.registerEvents(new Move(), p);
    }

    private static void init() {

        navigator = Bukkit.createInventory(null, 27, "§cNavigator");
        // First Line
        navigator.setItem(2, ItemCreator.itemCreate(Material.WORKBENCH, "§6Bau Welt"));
        navigator.setItem(4, ItemCreator.itemCreate(Material.DRAGON_EGG, "§5Biomia | Quests"));
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

                ArrayList<ServerObject> lobbyServer = new ArrayList<>(
                        de.biomiaAPI.main.Main.getUniversalTimoapi().getGroup("Lobby").getServers());
                lobbyServer.sort(Comparator.comparing(ServerObject::getName));

                int i = 0;

                ServerObject serverObject = de.biomiaAPI.main.Main.getBukkitTimoapi().getThisServer();

                if (serverObject == null) {
                    Bukkit.broadcastMessage("object == null");
                    return;
                }

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
        }.runTaskTimer(de.biomiaAPI.main.Main.getPlugin(), 100, 200);
    }
}