package de.biomiaAPI.main;

import java.util.ArrayList;
import java.util.HashMap;

import de.biomiaAPI.achievements.BiomiaAchievement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import at.TimoCraft.TimoCloud.api.TimoCloudAPI;
import at.TimoCraft.TimoCloud.api.TimoCloudBukkitAPI;
import at.TimoCraft.TimoCloud.api.TimoCloudUniversalAPI;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.Quests.DialogMessage;
import de.biomiaAPI.connect.Connect;
import de.biomiaAPI.cosmetics.Cosmetic;
import de.biomiaAPI.cosmetics.Cosmetic.Group;
import de.biomiaAPI.cosmetics.CosmeticGroup;
import de.biomiaAPI.cosmetics.GadgetItems.GadgetIniter;
import de.biomiaAPI.cosmetics.ParticleItems.ParticleIniter;
import de.biomiaAPI.itemcreator.ItemCreator;
import de.biomiaAPI.msg.Messages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

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

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        setBukkitTimoapi(TimoCloudAPI.getBukkitInstance());
        setUniversalTimoapi(TimoCloudAPI.getUniversalInstance());
        groupName = bukkitTimoapi.getThisServer().getGroupName();


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
        init();

        /*
         * WERBUNG Sendet in einem gewissen Zeitabstand eine bestimmte Werbenachricht,
         * an alle Spieler, die nicht Premium sind
         */

        new BukkitRunnable() {

            int i = 0;

            @Override
            public void run() {

                TextComponent message;

                switch (i) {
                    case 0:
                        message = new TextComponent(Messages.prefix + "\u00A76Besuch uns auf www.biomia.de !");
                        message.setClickEvent(
                                new ClickEvent(ClickEvent.Action.OPEN_URL, "https://biomia.bildnerverlag.de/"));
                        break;
                    case 1:
                        message = new TextComponent(Messages.prefix
                                + "\u00A76Schau doch mal auf unserem \u00A76TeamSpeak-Server vorbei! \u00A76ts.biomia.de !");
                        break;
                    case 2:
                        message = new TextComponent(Messages.prefix + "\u00A76Besuch uns auf instagram.com/biomiaofficial !");
                        message.setClickEvent(
                                new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.instagram.com/biomiaofficial/"));
                        break;
                    case 3:
                        message = new TextComponent(Messages.prefix + "\u00A76Folge uns auf twitter.com/biomiaofficial !");
                        message.setClickEvent(
                                new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitter.com/biomiaofficial"));
                        break;
                    default:
                        message = new TextComponent(
                                Messages.prefix + "\u00A76Folge uns auf Facebook! \u00A76facebook.com/biomiaofficial/");
                        message.setClickEvent(
                                new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.facebook.com/biomiaofficial/"));
                        break;
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!Biomia.getBiomiaPlayer(p).isPremium() && Biomia.getBiomiaPlayer(p).isStaff()) {
                        p.spigot().sendMessage(message);
                    }
                }

                if (i < 3)
                    i++;
                else
                    i = 0;
            }
        }.runTaskTimer(this, 0L, 20L * 60 * 5); /* Der Long ist die Anzahl der Ticks, ergo im moment 5 min */
    }

    @Override
    public void onDisable() {
        plugin.saveConfig();
    }

    private static void init() {

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
