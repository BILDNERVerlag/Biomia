package de.biomia.spigot;

import de.biomia.spigot.achievements.Achievements;
import de.biomia.spigot.achievements.StatListener;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.commands.minigames.BWCommand;
import de.biomia.spigot.commands.minigames.SWCommand;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.general.cosmetics.Cosmetic;
import de.biomia.spigot.general.cosmetics.Cosmetic.Group;
import de.biomia.spigot.general.cosmetics.CosmeticGroup;
import de.biomia.spigot.general.cosmetics.gadgets.GadgetIniter;
import de.biomia.spigot.general.cosmetics.particles.ParticleIniter;
import de.biomia.spigot.general.reportsystem.ReportSQL;
import de.biomia.spigot.listeners.ChannelListener;
import de.biomia.spigot.listeners.servers.BauServerListener;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.WaitingLobbyListener;
import de.biomia.spigot.minigames.general.chests.Items;
import de.biomia.spigot.minigames.versus.Versus;
import de.biomia.spigot.server.demoserver.Weltenlabor;
import de.biomia.spigot.server.freebuild.Freebuild;
import de.biomia.spigot.server.lobby.Lobby;
import de.biomia.spigot.server.quests.Quests;
import de.biomia.spigot.specialEvents.easterEvent.EasterEvent;
import de.biomia.spigot.specialEvents.schnitzelEvent.SchnitzelEvent;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.PlayerToServerConnector;
import de.biomia.universal.MySQL;
import net.minecraft.server.v1_12_R1.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static de.biomia.spigot.minigames.GameType.BED_WARS;
import static de.biomia.spigot.minigames.GameType.SKY_WARS;

public class Main extends JavaPlugin {

    private static EasterEvent event;
    private static Main plugin;
    private static CommandMap commandMap;

    public static EasterEvent getEvent() {
        return event;
    }

    public static Main getPlugin() {
        return plugin;
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

        BiomiaServerType groupName = BiomiaServerType.valueOf(((DedicatedServer) ((CraftServer) Bukkit.getServer()).getServer()).propertyManager.properties.getProperty("server-name").split("-")[0]);
        switch (groupName) {
        case TestLobby:
        case Lobby:
            Biomia.setServerInstance(new Lobby(groupName));
            break;
        case TestServer:
        case TestQuest:
        case Quest:
            Biomia.setServerInstance(new Quests(groupName));
            break;
        case TestBedWars:
        case BedWars:
            Biomia.setServerInstance(new BiomiaServer(groupName) {
                @Override
                public void start() {
                    super.start();
                    registerCommand(new BWCommand());
                    Bukkit.getPluginManager().registerEvents(new WaitingLobbyListener(false), Main.getPlugin());
                }
            });
            new GameInstance(BED_WARS, MinigamesConfig.getMapName(), MinigamesConfig.getTeamAmount(), MinigamesConfig.getTeamSize()).getGameMode().start();
            break;
        case TestSkyWars:
        case SkyWars:
            Biomia.setServerInstance(new BiomiaServer(groupName) {
                @Override
                public void start() {
                    super.start();
                    registerCommand(new SWCommand());
                    Bukkit.getPluginManager().registerEvents(new WaitingLobbyListener(false), Main.getPlugin());
                }
            });
            Items.init();
            new GameInstance(SKY_WARS, MinigamesConfig.getMapName(), MinigamesConfig.getTeamAmount(), MinigamesConfig.getTeamSize()).getGameMode().start();
            break;
        case Duell:
            Biomia.setServerInstance(new Versus());
            break;
        case Weltenlabor_1:
            Biomia.setServerInstance(new Weltenlabor());
            break;
        case TestFreebuild:
        case Freebuild:
            Biomia.setServerInstance(new Freebuild(groupName));
            break;
        case FreebuildFarm:
            break;
        case BauServer:
            Biomia.setServerInstance(new BiomiaServer(groupName) {
                @Override
                public void start() {
                    super.start();
                    Bukkit.getPluginManager().registerEvents(new BauServerListener(), Main.getPlugin());
                }
            });
            break;
        case Event_Schnitzeljagd:
            Biomia.setServerInstance(new SchnitzelEvent());
            break;
        }

        Biomia.getServerInstance().start();

        event = new EasterEvent();
    }

    @Override
    public void onDisable() {
        Biomia.getServerInstance().stop();
        this.saveConfig();
        event.removeAllEggs();
        MySQL.closeConnections();
    }
}