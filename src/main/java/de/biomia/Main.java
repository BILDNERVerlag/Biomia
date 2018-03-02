package de.biomia;

import de.biomia.achievements.Achievements;
import de.biomia.achievements.StatListener;
import de.biomia.commands.BiomiaCommand;
import de.biomia.commands.general.*;
import de.biomia.data.MySQL;
import de.biomia.general.cosmetics.Cosmetic;
import de.biomia.general.cosmetics.Cosmetic.Group;
import de.biomia.general.cosmetics.CosmeticGroup;
import de.biomia.general.cosmetics.gadgets.GadgetIniter;
import de.biomia.general.cosmetics.particles.ParticleIniter;
import de.biomia.general.reportsystem.ReportSQL;
import de.biomia.listeners.ReportListener;
import de.biomia.listeners.ChannelListener;
import de.biomia.listeners.CosmeticListener;
import de.biomia.listeners.servers.BauServerListener;
import de.biomia.server.demoserver.Weltenlabor;
import de.biomia.server.freebuild.Freebuild;
import de.biomia.server.lobby.Lobby;
import de.biomia.server.minigames.bedwars.BedWars;
import de.biomia.server.minigames.skywars.SkyWars;
import de.biomia.server.minigames.versus.VSMain;
import de.biomia.server.quests.Quests;
import de.biomia.specialEvents.easterEvent.EasterEvent;
import de.biomia.tools.ItemCreator;
import de.biomia.tools.PlayerToServerConnector;
import net.minecraft.server.v1_12_R1.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Main extends JavaPlugin {

    public static final HashMap<String, String> RANK_NAMES_PREFIXES = new HashMap<>();
    private static final EasterEvent event = null;
    private static Main plugin;
    private static String groupName;
    private static CommandMap commandMap;

    //TODO serverTypesClassInterfacesInstanceForCommandsAndPermissions

    public static EasterEvent getEvent() {
        return event;
    }

    public static Main getPlugin() {
        return plugin;
    }

    public static String getGroupName() {
        return groupName;
    }

    public static void registerCommand(BiomiaCommand biomiaCommand) {
        try {
            if (commandMap == null)
                commandMap = ((CraftServer) plugin.getServer()).getCommandMap();
            commandMap.register("biomia", biomiaCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PlayerToServerConnector());
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BiomiaChannel");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BiomiaChannel", new ChannelListener());

        registerListeners();
        registerCommands();

        ReportSQL.getAllReports();

        //TODO: Osterevent nach Fertigstellung freischalten
        //event = new EasterEvent();

        for (Player p : Bukkit.getOnlinePlayers()) {
            Cosmetic.load(Biomia.getBiomiaPlayer(p));
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

        RANK_NAMES_PREFIXES.put("Owner", "\u00A74Owner | ");
        RANK_NAMES_PREFIXES.put("Admin", "\u00A75Admin | ");
        RANK_NAMES_PREFIXES.put("SrBuilder", "\u00A72SrBuilder | ");
        RANK_NAMES_PREFIXES.put("JrBuilder", "\u00A7aJrBuilder | ");
        RANK_NAMES_PREFIXES.put("SrModerator", "\u00A79SrMod | ");
        RANK_NAMES_PREFIXES.put("Moderator", "\u00A73Mod | ");
        RANK_NAMES_PREFIXES.put("Supporter", "\u00A7bSup | ");
        RANK_NAMES_PREFIXES.put("Builder", "\u00A72Builder | ");
        RANK_NAMES_PREFIXES.put("YouTube", "\u00A74[\u00A70Y\u00A7fT\u00A74] | ");
        RANK_NAMES_PREFIXES.put("PremiumZehn", "\u00A76X | ");
        RANK_NAMES_PREFIXES.put("PremiumNeun", "\u00A76IX | ");
        RANK_NAMES_PREFIXES.put("PremiumAcht", "\u00A7eVIII | ");
        RANK_NAMES_PREFIXES.put("PremiumSieben", "\u00A7eVII | ");
        RANK_NAMES_PREFIXES.put("PremiumSechs", "\u00A7eVI | ");
        RANK_NAMES_PREFIXES.put("PremiumFuenf", "\u00A7eV | ");
        RANK_NAMES_PREFIXES.put("PremiumVier", "\u00A7eIV | ");
        RANK_NAMES_PREFIXES.put("PremiumDrei", "\u00A7eIII | ");
        RANK_NAMES_PREFIXES.put("PremiumZwei", "\u00A7eII | ");
        RANK_NAMES_PREFIXES.put("PremiumEins", "\u00A7eI | ");
        RANK_NAMES_PREFIXES.put("RegSpieler", "\u00A77");
        RANK_NAMES_PREFIXES.put("UnregSpieler", "\u00A78");

        /*
         * Change the TestServer here!
         */
        String actualTestGroup = "QuestServer";

        groupName = ((DedicatedServer) ((CraftServer) Bukkit.getServer()).getServer()).propertyManager.properties.getProperty("server-name").split("-")[0];

        groupName = groupName.equals("TestServer") ? actualTestGroup : groupName;
        switch (groupName) {
            case "Lobby":
                Lobby.init();
                break;
            case "QuestServer":
                Quests.initQuests();
                break;
            case "BedWars":
                BedWars.init();
                break;
            case "SkyWars":
                SkyWars.init();
                break;
            case "DuellLobby":
                VSMain.initVersus();
                break;
            case "Weltenlabor#1":
                Weltenlabor.init();
                break;
            case "FreebuildServer":
                Freebuild.init();
                break;
            case "FarmServer":
                //TODO Farmserver nach Fertigstellung hinzufügen
                break;
        case "BauServer":
            Bukkit.getPluginManager().registerEvents(new BauServerListener(), this);
            break;
            default:
                break;
        }
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new ReportListener(), this);
        Bukkit.getPluginManager().registerEvents(new CosmeticListener(), this);
    }

    private void registerCommands() {
        registerCommand(new RandomServerGroupCommand());
        registerCommand(new CosmeticCommand());
        registerCommand(new BuildCommand());
        registerCommand(new CoinsCommand());
        registerCommand(new CreateTableCommand());
        registerCommand(new EatCommand());
        registerCommand(new FlyCommand());
        registerCommand(new GamemodeCommand());
        registerCommand(new HeadCommand());
        registerCommand(new HealCommand());
        registerCommand(new HologramCommand());
        registerCommand(new PermissionCommand());
        registerCommand(new RankCommand());
        registerCommand(new SpeedCommand());
        registerCommand(new StatCommand());
        registerCommand(new ReportCommand());
        registerCommand(new SeeReportsCommand());
        registerCommand(new StatCommand());
        registerCommand(new TrollCommand("crash"));
        registerCommand(new TrollCommand("troll"));
        registerCommand(new EventCommands("addeggs"));
        registerCommand(new EventCommands("givereward"));
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
                break;
            case "QuestServer":
                Quests.terminateQuests();
                break;
            case "FreebuildServer":
                Freebuild.terminate();
                break;
            case "FarmServer":
                //TODO Farmserver nach Fertigstellung hinzufügen
                break;
            case "Weltenlabor#1":
                Weltenlabor.init();
                break;
            default:
                break;
        }
    }
}