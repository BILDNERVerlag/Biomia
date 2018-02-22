package de.biomiaAPI.main;

import at.TimoCraft.TimoCloud.api.TimoCloudAPI;
import at.TimoCraft.TimoCloud.api.TimoCloudBukkitAPI;
import at.TimoCraft.TimoCloud.api.TimoCloudUniversalAPI;
import de.biomia.lobby.commands.LobbyComands;
import de.biomia.lobby.commands.SendToRandomServer;
import de.biomia.lobby.commands.WC;
import de.biomia.lobby.main.LobbyMain;
import de.biomia.quests.cmds.QuestCommands;
import de.biomia.quests.main.QuestMain;
import de.biomiaAPI.Quests.DialogMessage;
import de.biomiaAPI.achievements.BiomiaAchievement;
import de.biomiaAPI.achievements.StatListener;
import de.biomiaAPI.connect.Connect;
import de.biomiaAPI.cosmetics.Cosmetic;
import de.biomiaAPI.cosmetics.Cosmetic.Group;
import de.biomiaAPI.cosmetics.CosmeticGroup;
import de.biomiaAPI.cosmetics.GadgetItems.GadgetIniter;
import de.biomiaAPI.cosmetics.ParticleItems.ParticleIniter;
import de.biomiaAPI.itemcreator.ItemCreator;
import de.biomiaAPI.lastPosition.LastPositionListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin {

    //TODO Klassenname ändern

    public static Main plugin;
    public static final ArrayList<String> group = new ArrayList<>();
    public static final HashMap<String, String> prefixes = new HashMap<>();
    public static final ArrayList<String> allPlayersOnAllServer = new ArrayList<>();
    public static int QuestIds = 0;
    public static final HashMap<Integer, DialogMessage> questMessages = new HashMap<>();
    private static TimoCloudBukkitAPI bukkitTimoapi;
    private static String groupName;
    private static TimoCloudUniversalAPI universalTimoapi;

    public static Main getPlugin() {
        return plugin;
    }

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        setBukkitTimoapi(TimoCloudAPI.getBukkitInstance());
        setUniversalTimoapi(TimoCloudAPI.getUniversalInstance());

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new Connect());
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "RedisBungee");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "RedisBungee", new Connect());
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BiomiaChannel");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BiomiaChannel", new Connect());

        new BukkitRunnable() {
            @Override
            public void run() {
                Connect.getOnlinePlayers();
            }
        }.runTaskTimer(this, 0, 20 * 5);

        GadgetIniter.init();
        ParticleIniter.init();
        BiomiaAchievement.init();

        Bukkit.getPluginManager().registerEvents(new StatListener(), this);

        groupName = Bukkit.getServer().getName().split("-")[0];

        switch (groupName) {
            case "Lobby":
                Bukkit.getPluginManager().registerEvents(new LastPositionListener(), this);
                LobbyMain.initLobby();
                break;
            case "QuestServer":
                Bukkit.getPluginManager().registerEvents(new LastPositionListener(), this);
                new QuestMain().initQuests();
                break;
            default:
                break;
        }

        init();
    }

    @Override
    public void onDisable() {
        plugin.saveConfig();
    }

    private void init() {

        Cosmetic.initGroup(new CosmeticGroup(Group.HEADS, ItemCreator.itemCreate(Material.SKULL_ITEM, "\u00A7cHeads")));
        Cosmetic.initGroup(new CosmeticGroup(Group.PETS, ItemCreator.itemCreate(Material.MONSTER_EGG, "\u00A7bPets")));
        Cosmetic.initGroup(
                new CosmeticGroup(Group.GADGETS, ItemCreator.itemCreate(Material.BREWING_STAND_ITEM, "\u00A7dGadgets")));
        Cosmetic.initGroup(
                new CosmeticGroup(Group.PARTICLES, ItemCreator.itemCreate(Material.BLAZE_POWDER, "\u00A73Particles")));
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

        //Quests
        getCommand("q").setExecutor(new QuestCommands());
        getCommand("qr").setExecutor(new QuestCommands());
        getCommand("qlist").setExecutor(new QuestCommands());
        getCommand("tagebuch").setExecutor(new QuestCommands());
        getCommand("qinfo").setExecutor(new QuestCommands());
        getCommand("qalign").setExecutor(new QuestCommands());
        getCommand("qrestore").setExecutor(new QuestCommands());
        getCommand("aion").setExecutor(new QuestCommands());
        getCommand("aioff").setExecutor(new QuestCommands());
        getCommand("aitoggle").setExecutor(new QuestCommands());
        getCommand("qhelp").setExecutor(new QuestCommands());
        getCommand("qfilldiary").setExecutor(new QuestCommands());
        getCommand("qstats").setExecutor(new QuestCommands());
        getCommand("respawn").setExecutor(new QuestCommands());
        getCommand("qupdatebook").setExecutor(new QuestCommands());
        getCommand("qlog").setExecutor(new QuestCommands());
        getCommand("qtest").setExecutor(new QuestCommands());
        getCommand("qreset").setExecutor(new QuestCommands());

        //Lobby
        getCommand("lobbysettings").setExecutor(new LobbyComands());
        getCommand("randomServerGroup").setExecutor(new SendToRandomServer());
        getCommand("world").setExecutor(new WC());

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

    public static String getGroupName() {
        return groupName;
    }

}
