package de.biomia.api.main;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.TimoCloudBukkitAPI;
import cloud.timo.TimoCloud.api.TimoCloudUniversalAPI;
import de.biomia.api.Biomia;
import de.biomia.api.achievements.Achievements;
import de.biomia.api.achievements.StatListener;
import de.biomia.api.connect.Connect;
import de.biomia.api.cosmetics.Cosmetic;
import de.biomia.api.cosmetics.Cosmetic.Group;
import de.biomia.api.cosmetics.CosmeticGroup;
import de.biomia.api.cosmetics.GadgetItems.GadgetIniter;
import de.biomia.api.cosmetics.ParticleItems.ParticleIniter;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.api.mysql.MySQL;
import de.biomia.bw.main.BedWarsMain;
import de.biomia.demoserver.main.WeltenlaborMain;
import de.biomia.freebuild.main.FreebuildMain;
import de.biomia.lobby.main.LobbyMain;
import de.biomia.plugin.commands.*;
import de.biomia.plugin.listener.BiomiaListener;
import de.biomia.plugin.listener.ChannelListener;
import de.biomia.plugin.listener.CosmeticListener;
import de.biomia.plugin.reportsystem.ReportSQL;
import de.biomia.plugin.reportsystem.listener.ChatEvent;
import de.biomia.plugin.reportsystem.listener.ClickEvent;
import de.biomia.plugin.specialEvents.easterEvent.EasterEvent;
import de.biomia.quests.main.QuestMain;
import de.biomia.sw.main.SkyWarsMain;
import de.biomia.versus.vs.main.VSMain;
import net.minecraft.server.v1_12_R1.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin {

    //TODO Klassenname ändern
    public static Inventory menu;
    public static Inventory grund;

    public static Main plugin;

    public static final ArrayList<String> group = new ArrayList<>();
    public static final HashMap<String, String> prefixes = new HashMap<>();

    private static TimoCloudBukkitAPI bukkitTimoapi;
    private static TimoCloudUniversalAPI universalTimoapi;
    private static String groupName;
    private static EasterEvent event;

    public static EasterEvent getEvent() {
        return event;
    }

    public static Main getPlugin() {
        return plugin;
    }

    @Override
    public void onLoad() {
        plugin = this;
    }

    public static String getGroupName() {
        return groupName;
    }

    @Override
    public void onEnable() {
        setBukkitTimoapi(TimoCloudAPI.getBukkitInstance());
        setUniversalTimoapi(TimoCloudAPI.getUniversalInstance());

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new Connect());
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BiomiaChannel");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BiomiaChannel", new Connect());
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BiomiaChannel");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BiomiaChannel", new ChannelListener());

        plugin = this;


        registerListeners();
        registerCommands();
        initInventories();

        ReportSQL.getAllReports();

        //TODO: enable
        //event = new EasterEvent();

        for (Player p : Bukkit.getOnlinePlayers()) {
            de.biomia.api.cosmetics.Cosmetic.load(Biomia.getBiomiaPlayer(p));
        }

        GadgetIniter.init();
        ParticleIniter.init();
        Achievements.init();
        Bukkit.getPluginManager().registerEvents(new StatListener(), this);


        Cosmetic.initGroup(new CosmeticGroup(Group.HEADS, ItemCreator.itemCreate(Material.SKULL_ITEM, "\u00A7cHeads")));
        Cosmetic.initGroup(new CosmeticGroup(Group.PETS, ItemCreator.itemCreate(Material.MONSTER_EGG, "\u00A7bPets")));
        Cosmetic.initGroup(new CosmeticGroup(Group.GADGETS, ItemCreator.itemCreate(Material.BREWING_STAND_ITEM, "\u00A7dGadgets")));
        Cosmetic.initGroup(new CosmeticGroup(Group.PARTICLES, ItemCreator.itemCreate(Material.BLAZE_POWDER, "\u00A73Particles")));
        Cosmetic.initGroup(new CosmeticGroup(Group.SUITS, ItemCreator.itemCreate(Material.GOLD_CHESTPLATE, "\u00A75Suits")));

        group.add("Owner");
        group.add("Admin");
        group.add("SrModerator");
        group.add("SrBuilder");
        group.add("Moderator");
        group.add("Builder");
        group.add("Supporter");
        group.add("YouTube");
        group.add("JrBuilder");
        group.add("PremiumZehn");
        group.add("PremiumNeun");
        group.add("PremiumAcht");
        group.add("PremiumSieben");
        group.add("PremiumSechs");
        group.add("PremiumFuenf");
        group.add("PremiumVier");
        group.add("PremiumDrei");
        group.add("PremiumZwei");
        group.add("PremiumEins");
        group.add("RegSpieler");
        group.add("UnregSpieler");

        prefixes.put("Owner", "\u00A74Owner | ");
        prefixes.put("Admin", "\u00A75Admin | ");
        prefixes.put("SrBuilder", "\u00A72SrBuilder | ");
        prefixes.put("JrBuilder", "\u00A7aJrBuilder | ");
        prefixes.put("SrModerator", "\u00A79SrMod | ");
        prefixes.put("Moderator", "\u00A73Mod | ");
        prefixes.put("Supporter", "\u00A7bSup | ");
        prefixes.put("Builder", "\u00A72Builder | ");
        prefixes.put("YouTube", "\u00A74[\u00A70Y\u00A7fT\u00A74] | ");
        prefixes.put("PremiumZehn", "\u00A76X | ");
        prefixes.put("PremiumNeun", "\u00A76IX | ");
        prefixes.put("PremiumAcht", "\u00A7eVIII | ");
        prefixes.put("PremiumSieben", "\u00A7eVII | ");
        prefixes.put("PremiumSechs", "\u00A7eVI | ");
        prefixes.put("PremiumFuenf", "\u00A7eV | ");
        prefixes.put("PremiumVier", "\u00A7eIV | ");
        prefixes.put("PremiumDrei", "\u00A7eIII | ");
        prefixes.put("PremiumZwei", "\u00A7eII | ");
        prefixes.put("PremiumEins", "\u00A7eI | ");
        prefixes.put("RegSpieler", "\u00A77");
        prefixes.put("UnregSpieler", "\u00A78");

//        groupName = TimoCloudAPI.getBukkitInstance().getThisServer().getName();
        groupName = ((DedicatedServer) ((CraftServer) Bukkit.getServer()).getServer()).propertyManager.properties.getProperty("server-name").split("-")[0];

        switch (groupName) {
            case "Lobby":
                LobbyMain.initLobby();
                break;
            case "TestServer":
            case "QuestServer":
                QuestMain.initQuests();
                break;
            case "BedWars":
                BedWarsMain.initBedWars();
                break;
            case "SkyWars":
                SkyWarsMain.initSkyWars();
                break;
            case "DuellLobby":
                VSMain.initVersus();
                break;
            case "Weltenlabor#1":
                WeltenlaborMain.initWeltenlabor();
                break;
            case "FreebuildServer":
                FreebuildMain.initFreebuild();
                break;
            case "FarmServer":
                //TODO
                break;
            default:
                break;
        }
    }

    public static TimoCloudUniversalAPI getUniversalTimoapi() {
        return universalTimoapi;
    }

    private static void setUniversalTimoapi(TimoCloudUniversalAPI universalTimoapi) {
        Main.universalTimoapi = universalTimoapi;
    }

    public static TimoCloudBukkitAPI getBukkitTimoapi() {
        return bukkitTimoapi;
    }

    private static void setBukkitTimoapi(TimoCloudBukkitAPI bukkitTimoapi) {
        Main.bukkitTimoapi = bukkitTimoapi;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.saveConfig();
        MySQL.closeConnections();
        switch (getGroupName()) {
            case "Lobby":
            case "BedWars":
            case "SkyWars":
            case "DuellLobby":
                //none
                break;
            case "TestServer":
            case "QuestServer":
                QuestMain.terminateQuests();
                break;
            case "FreebuildServer":
                FreebuildMain.terminateFreebuild();
                break;
            case "FarmServer":
                //TODO
                break;
            case "Weltenlabor#1":
                WeltenlaborMain.initWeltenlabor();
                break;
            default:
                break;
        }
    }

    private void initInventories() {

        menu = Bukkit.createInventory(null, 9, "\u00A7eREPORT MEN\u00fc");
        ItemStack bug = ItemCreator.itemCreate(Material.BARRIER, "\u00A7cBug");
        ItemStack spieler = ItemCreator.headWithSkin("DerJulsn", "\u00A7cSpieler");
        menu.setItem(3, bug);
        menu.setItem(5, spieler);

        grund = Bukkit.createInventory(null, 18, "\u00A7eGRUND");
        grund.setItem(2, ItemCreator.itemCreate(Material.ELYTRA, "\u00A7cFlyHack"));
        grund.setItem(3, ItemCreator.itemCreate(Material.DIAMOND, "\u00A7cNoSlowdown"));
        grund.setItem(4, ItemCreator.itemCreate(Material.IRON_SWORD, "\u00A7cKillaura"));
        grund.setItem(5, ItemCreator.itemCreate(Material.LEATHER_BOOTS, "\u00A7cSpeedHack"));
        grund.setItem(6, ItemCreator.itemCreate(Material.PAPER, "\u00A7cSonstiger Hack"));
        grund.setItem(11, ItemCreator.itemCreate(Material.TNT, "\u00A7cGriefing"));
        grund.setItem(12, ItemCreator.itemCreate(Material.BARRIER, "\u00A7cSpamming"));
        grund.setItem(13, ItemCreator.itemCreate(Material.RAW_FISH, "\u00A7cTrolling"));
        grund.setItem(14, ItemCreator.itemCreate(Material.BONE, "\u00A7cBeleidigung"));
        grund.setItem(15, ItemCreator.itemCreate(Material.BOOK, "\u00A7cAnderer Grund"));
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new BiomiaListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ClickEvent(), this);
        Bukkit.getPluginManager().registerEvents(new CosmeticListener(), this);
    }

    private void registerCommands() {
        getCommand("permission").setExecutor(new PermissionCommand());
        getCommand("rank").setExecutor(new RankCommand());
        getCommand("hologram").setExecutor(new HologramCommand());
        getCommand("troll").setExecutor(new Troll());
        getCommand("crash").setExecutor(new Troll());
        getCommand("gm").setExecutor(new Gamemode());
        getCommand("report").setExecutor(new ReportCommand());
        getCommand("seereports").setExecutor(new ReportCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("speed").setExecutor(new SpeedCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("eat").setExecutor(new EatCommand());
        getCommand("memory").setExecutor(new Memory());
        getCommand("cosmetic").setExecutor(new de.biomia.plugin.commands.Cosmetic());
        getCommand("head").setExecutor(new Head());
        getCommand("stat").setExecutor(new StatCommand());
        getCommand("ct").setExecutor(new CreateTableCommand());
        getCommand("givereward").setExecutor(new EventCommands());
        getCommand("addeggs").setExecutor(new EventCommands());
    }
}
