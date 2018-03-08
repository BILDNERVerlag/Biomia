package de.biomia.spigot;

import de.biomia.spigot.achievements.Achievements;
import de.biomia.spigot.achievements.StatListener;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.general.cosmetics.Cosmetic;
import de.biomia.spigot.general.cosmetics.Cosmetic.Group;
import de.biomia.spigot.general.cosmetics.CosmeticGroup;
import de.biomia.spigot.general.cosmetics.gadgets.GadgetIniter;
import de.biomia.spigot.general.cosmetics.particles.ParticleIniter;
import de.biomia.spigot.general.reportsystem.ReportSQL;
import de.biomia.spigot.listeners.ChannelListener;
import de.biomia.spigot.listeners.servers.BauServerListener;
import de.biomia.spigot.minigames.bedwars.BedWars;
import de.biomia.spigot.minigames.skywars.SkyWars;
import de.biomia.spigot.minigames.versus.Versus;
import de.biomia.spigot.server.demoserver.Weltenlabor;
import de.biomia.spigot.server.freebuild.Freebuild;
import de.biomia.spigot.server.lobby.Lobby;
import de.biomia.spigot.server.quests.Quests;
import de.biomia.spigot.specialEvents.easterEvent.EasterEvent;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.PlayerToServerConnector;
import de.biomia.universal.MySQL;
import de.biomia.universal.UniversalBiomia;
import net.minecraft.server.v1_12_R1.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    /**
     * Change the TestServer here!
     */
    private final String actualTestGroup = "QuestServer";

    private static EasterEvent event;
    private static Main plugin;
    private static String groupName;
    private static CommandMap commandMap;

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

        ReportSQL.getAllReports();

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

        UniversalBiomia.RANK_NAMES_PREFIXES.put("Owner", "\u00A74Owner | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("Admin", "\u00A75Admin | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("SrBuilder", "\u00A72SrBuilder | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("JrBuilder", "\u00A7aJrBuilder | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("SrModerator", "\u00A79SrMod | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("Moderator", "\u00A73Mod | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("Supporter", "\u00A7bSup | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("Builder", "\u00A72Builder | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("YouTube", "\u00A74[\u00A70Y\u00A7fT\u00A74] | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("PremiumZehn", "\u00A76X | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("PremiumNeun", "\u00A76IX | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("PremiumAcht", "\u00A7eVIII | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("PremiumSieben", "\u00A7eVII | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("PremiumSechs", "\u00A7eVI | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("PremiumFuenf", "\u00A7eV | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("PremiumVier", "\u00A7eIV | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("PremiumDrei", "\u00A7eIII | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("PremiumZwei", "\u00A7eII | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("PremiumEins", "\u00A7eI | ");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("RegSpieler", "\u00A77");
        UniversalBiomia.RANK_NAMES_PREFIXES.put("UnregSpieler", "\u00A78");

        groupName = ((DedicatedServer) ((CraftServer) Bukkit.getServer()).getServer()).propertyManager.properties.getProperty("server-name").split("-")[0];

        groupName = groupName.equals("TestServer") ? actualTestGroup : groupName;
        switch (groupName) {
            case "Lobby":
                new Lobby().start();
                break;
            case "QuestServer":
                new Quests().start();
                break;
            case "BedWars":
                BedWars.init();
                break;
            case "SkyWars":
                SkyWars.init();
                break;
            case "DuellLobby":
                new Versus().start();
                break;
            case "Weltenlabor#1":
                new Weltenlabor().start();
                break;
            case "FreebuildServer":
                new Freebuild().start();
                break;
            case "FarmServer":
                //TODO Farmserver nach Fertigstellung hinzufügen
                break;
            case "BauServer":
                Bukkit.getPluginManager().registerEvents(new BauServerListener(), this);
                break;
        }

        event = new EasterEvent();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.saveConfig();
        MySQL.closeConnections();
        Biomia.getSeverInstance().stop();
    }
}