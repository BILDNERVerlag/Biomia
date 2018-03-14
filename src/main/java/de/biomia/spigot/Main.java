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
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.bedwars.var.Variables;
import de.biomia.spigot.minigames.versus.Versus;
import de.biomia.spigot.server.demoserver.Weltenlabor;
import de.biomia.spigot.server.freebuild.Freebuild;
import de.biomia.spigot.server.lobby.Lobby;
import de.biomia.spigot.server.quests.Quests;
import de.biomia.spigot.specialEvents.easterEvent.EasterEvent;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.PlayerToServerConnector;
import de.biomia.universal.MySQL;
import net.minecraft.server.v1_12_R1.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static de.biomia.spigot.minigames.GameType.BED_WARS;
import static de.biomia.spigot.minigames.GameType.SKY_WARS;

public class Main extends JavaPlugin {

    /**
     * Change the TestServer here!
     */
    private static final String actualTestGroup = "BedWars";

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

        groupName = ((DedicatedServer) ((CraftServer) Bukkit.getServer()).getServer()).propertyManager.properties.getProperty("server-name").split("-")[0];

        groupName = groupName.equals("TestServer") ? actualTestGroup : groupName;
        switch (groupName) {
            case "TestLobby":
            case "Lobby":
                Biomia.setServerInstance(new Lobby());
                Biomia.getServerInstance().start();
                break;
            case "TestQuest":
            case "Quest":
            case "QuestServer":
                Biomia.setServerInstance(new Quests());
                Biomia.getServerInstance().start();
                break;
            case "TestBedWars":
            case "BedWars":
                //TODO BedWars und SkyWars aus Config auslesen
                new GameInstance(BED_WARS, new WorldCreator(Variables.name).createWorld(), Variables.name, 4, 4).getGameMode().start();
                break;
            case "TestSkyWars":
            case "SkyWars":
                new GameInstance(SKY_WARS, new WorldCreator(Variables.name).createWorld(), Variables.name, 4, 4).getGameMode().start();
                break;
            case "TestDuellLobby":
            case "DuellLobby":
                Biomia.setServerInstance(new Versus());
                Biomia.getServerInstance().start();
                break;
            case "Weltenlabor#1":
                Biomia.setServerInstance(new Weltenlabor());
                Biomia.getServerInstance().start();
                break;
            case "TestFreebuild":
            case "Freebuild":
            case "FreebuildServer":
                Biomia.setServerInstance(new Freebuild());
                Biomia.getServerInstance().start();
                break;
            case "Freebuild-Farm":
            case "FarmServer":
                //TODO Farmserver nach Fertigstellung hinzufuegen
                break;
            case "BauServer":
                Bukkit.getPluginManager().registerEvents(new BauServerListener(), this);
                break;
                //TODO: Group Names ändern
        }

        event = new EasterEvent();
    }

    @Override
    public void onDisable() {
        this.saveConfig();
        Biomia.getServerInstance().stop();
        MySQL.closeConnections();
    }
}