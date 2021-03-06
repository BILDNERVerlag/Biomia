package de.biomia.spigot;

import de.biomia.spigot.achievements.Achievement;
import de.biomia.spigot.achievements.StatListener;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.commands.minigames.BWCommand;
import de.biomia.spigot.commands.minigames.MinigamesCommands;
import de.biomia.spigot.commands.minigames.SWCommand;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.general.cosmetics.Cosmetic;
import de.biomia.spigot.general.cosmetics.CosmeticGroup;
import de.biomia.spigot.general.cosmetics.gadgets.GadgetIniter;
import de.biomia.spigot.general.cosmetics.particles.ParticleIniter;
import de.biomia.spigot.general.reportsystem.ReportSQL;
import de.biomia.spigot.listeners.ChannelListener;
import de.biomia.spigot.listeners.servers.BauServerListener;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.WarteLobbyListener;
import de.biomia.spigot.minigames.general.chests.Items;
import de.biomia.spigot.minigames.versus.Versus;
import de.biomia.spigot.server.demoserver.Weltenlabor;
import de.biomia.spigot.server.freebuild.Freebuild;
import de.biomia.spigot.server.lobby.Lobby;
import de.biomia.spigot.server.quests.Quests;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.PlayerToServerConnector;
import de.biomia.spigot.tools.VoidWorldGenerator;
import de.biomia.universal.Messages;
import de.biomia.universal.MySQL;
import net.minecraft.server.v1_12_R1.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import static de.biomia.spigot.minigames.GameType.*;

public class Main extends JavaPlugin {

    private static Main plugin;
    private static CommandMap commandMap;
    private final static VoidWorldGenerator voidWorldGenerator = new VoidWorldGenerator();

    public static Main getPlugin() {
        return plugin;
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        switch (Biomia.getServerInstance().getServerType()) {
            case Quest:
            case TestFreebuild:
            case BauServer:
            case Freebuild:
            case TestQuest:
                return super.getDefaultWorldGenerator(worldName, id);
            default:
                return voidWorldGenerator;
        }
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

        BiomiaServerType groupName = BiomiaServerType.valueOf(((DedicatedServer) ((CraftServer) Bukkit.getServer()).getServer()).propertyManager.properties.getProperty("server-name").split("-")[0]);
        switch (groupName) {
            case TestLobby:
            case Lobby:
                Biomia.setServerInstance(new Lobby(groupName));
                GadgetIniter.init();
                ParticleIniter.init();
                Cosmetic.initGroup(new CosmeticGroup(Cosmetic.Group.HEADS, ItemCreator.itemCreate(Material.SKULL_ITEM, String.format("%sHeads", Messages.COLOR_MAIN))));
                Cosmetic.initGroup(new CosmeticGroup(Cosmetic.Group.PETS, ItemCreator.itemCreate(Material.MONSTER_EGG, String.format("%sPets", Messages.COLOR_SUB))));
                Cosmetic.initGroup(new CosmeticGroup(Cosmetic.Group.GADGETS, ItemCreator.itemCreate(Material.BREWING_STAND_ITEM, String.format("%sGadgets", Messages.COLOR_MAIN))));
                Cosmetic.initGroup(new CosmeticGroup(Cosmetic.Group.PARTICLES, ItemCreator.itemCreate(Material.BLAZE_POWDER, String.format("%sParticles", Messages.COLOR_SUB))));
                Cosmetic.initGroup(new CosmeticGroup(Cosmetic.Group.SUITS, ItemCreator.itemCreate(Material.GOLD_CHESTPLATE, String.format("%sSuits", Messages.COLOR_MAIN))));
                Bukkit.getOnlinePlayers().forEach(each -> Cosmetic.load(Biomia.getBiomiaPlayer(each)));
                break;
            case TestServer:
            case TestQuest:
                Biomia.setServerInstance(new de.biomia.spigot.quests.Quests());
                break;
            case Quest:
                Biomia.setServerInstance(new Quests(groupName));
                break;
            case TestBedWars:
            case BedWars:
                Biomia.setServerInstance(new BiomiaServer(groupName) {
                    @Override
                    protected void initCommands() {
                        super.initCommands();
                        registerCommand(new BWCommand());
                    }

                    @Override
                    protected void initListeners() {
                        super.initListeners();
                        Bukkit.getPluginManager().registerEvents(new WarteLobbyListener(false), Main.getPlugin());
                    }
                });
                new GameInstance(BED_WARS, MinigamesConfig.getMapName(), MinigamesConfig.getMapName(), MinigamesConfig.getTeamAmount(), MinigamesConfig.getTeamSize()).getGameMode().start();
                break;
            case TestSkyWars:
            case SkyWars:
                Biomia.setServerInstance(new BiomiaServer(groupName) {
                    @Override
                    protected void initCommands() {
                        super.initCommands();
                        registerCommand(new SWCommand());
                    }

                    @Override
                    protected void initListeners() {
                        super.initListeners();
                        Bukkit.getPluginManager().registerEvents(new WarteLobbyListener(false), Main.getPlugin());
                    }
                });
                Items.init();
                new GameInstance(SKY_WARS, MinigamesConfig.getMapName(), MinigamesConfig.getMapName(), MinigamesConfig.getTeamAmount(), MinigamesConfig.getTeamSize()).getGameMode().start();
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
            case Parrot:
                Biomia.setServerInstance(new BiomiaServer(groupName) {
                    @Override
                    protected void initListeners() {
                        super.initListeners();
                        Bukkit.getPluginManager().registerEvents(new WarteLobbyListener(false), Main.getPlugin());
                    }
                    @Override
                    protected void initCommands() {
                        super.initCommands();
                        registerCommand(new MinigamesCommands("addship"));
                    }
                });
                new GameInstance(PARROT, MinigamesConfig.getMapName(), MinigamesConfig.getMapName(), MinigamesConfig.getTeamAmount(), MinigamesConfig.getTeamSize()).getGameMode().start();
                break;
        }

        ReportSQL.getAllReports();
        Achievement.init();
        Bukkit.getPluginManager().registerEvents(new StatListener(), this);
        Biomia.getServerInstance().start();

    }

    @Override
    public void onDisable() {
        Biomia.getServerInstance().stop();
        this.saveConfig();
        MySQL.closeConnections();
    }
}