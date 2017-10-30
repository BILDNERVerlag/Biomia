package de.biomiaAPI.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import at.TimoCraft.TimoCloud.api.TimoCloudAPI;
import at.TimoCraft.TimoCloud.api.TimoCloudBukkitAPI;
import at.TimoCraft.TimoCloud.api.TimoCloudUniversalAPI;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.Quests.DialogMessage;
import de.biomiaAPI.connect.Connect;
import de.biomiaAPI.msg.Messages;
import de.biomiaAPI.mysql.MySQL;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Main extends JavaPlugin {

	public static Main plugin;
	public static ArrayList<String> group = new ArrayList<>();
	public static HashMap<String, String> prefixes = new HashMap<>();
	public static ArrayList<String> allPlayersOnAllServer = new ArrayList<>();
	public static int QuestIds = 0;
	public static HashMap<Integer, DialogMessage> questMessages = new HashMap<>();
	private static TimoCloudBukkitAPI bukkitTimoapi;
	private static TimoCloudUniversalAPI universalTimoapi;

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

		MySQL.execute(
				"CREATE TABLE IF NOT EXISTS `Quests` ( `id` SMALLINT NOT NULL AUTO_INCREMENT , `name` VARCHAR(50) NOT NULL , `band` SMALLINT(2) NOT NULL , PRIMARY KEY (`id`))");
		MySQL.execute(
				"CREATE TABLE IF NOT EXISTS `Quests_erledigt` ( `uuid` VARCHAR(36) NOT NULL , `name` VARCHAR(50) NOT NULL , `end_time` BIGINT NOT NULL )");
		MySQL.execute(
				"CREATE TABLE IF NOT EXISTS `Quests_aktuell` ( `uuid` VARCHAR(36) NOT NULL , `name` VARCHAR(50) NOT NULL , `state` VARCHAR(20) NOT NULL )");
		MySQL.execute(
				"CREATE TABLE IF NOT EXISTS InventorySaves ( `uuid` VARCHAR(36) NOT NULL , `inventory` TEXT NOT NULL , `servergroup` VARCHAR(50) NOT NULL ) ENGINE = InnoDB;");
		MySQL.execute(
				"CREATE TABLE IF NOT EXISTS BiomiaPlayer ( `id` BIGINT NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(36) NOT NULL , `name` VARCHAR(16) NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
		MySQL.execute(
				"CREATE TABLE IF NOT EXISTS CoinBoost ( `BiomiaPlayer` INT NOT NULL , `percent` INT NOT NULL , `until` INT NOT NULL, PRIMARY KEY (`BiomiaPlayer`)) ENGINE = InnoDB;");

		init();

		/*
		 * WERBUNG Sendet in einem gewissen Zeitabstand eine bestimmte Werbenachricht,
		 * an alle Spieler, die nicht Premium sind
		 */

		new BukkitRunnable() {

			int i = 0;

			@Override
			public void run() {

				TextComponent message = null;

				switch (i) {
				case 0:
					message = new TextComponent(Messages.prefix + "§6Besuch uns auf www.biomia.de !");
					message.setClickEvent(
							new ClickEvent(ClickEvent.Action.OPEN_URL, "https://biomia.bildnerverlag.de/"));
					break;
				case 1:
					message = new TextComponent(
							Messages.prefix + "§6Besuch uns auf unserem TeamSpeak-Server! ts.biomia.de !");
					break;
				case 2:
					message = new TextComponent(Messages.prefix + "§6Besuch uns auf instagram.com/biomiaofficial !");
					message.setClickEvent(
							new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.instagram.com/biomiaofficial/"));
					break;
				case 3:
					message = new TextComponent(Messages.prefix + "§6Folge uns auf twitter.com/biomiaofficial !");
					message.setClickEvent(
							new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitter.com/biomiaofficial"));
					break;
				default:
					message = new TextComponent(
							Messages.prefix + "§6Folge uns auf Facebook! facebook.com/biomiaofficial/");
					message.setClickEvent(
							new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.facebook.com/biomiaofficial/"));
					break;
				}
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (!Biomia.getBiomiaPlayer(p).isPremium()) {
						p.spigot().sendMessage(message);
					}
				}

				if (i < 3)
					i++;
				else
					i = 0;
			}
		}.runTaskTimer(this, 0L, 20L * 60 * 3); /* Der Long ist die Anzahl der Ticks, ergo im moment 3 min */
	}

	@Override
	public void onDisable() {
		plugin.saveConfig();
	}

	public static void init() {

		group.add("Owner");
		group.add("Admin");
		group.add("SrModerator");
		group.add("SrBuilder");
		group.add("Moderator");
		group.add("Builder");
		group.add("YouTube");
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

		prefixes.put("Owner", "§4Owner | ");
		prefixes.put("Admin", "§5Admin | ");
		prefixes.put("SrBuilder", "§2SrBuilder | ");
		prefixes.put("SrModerator", "§bSrMod | ");
		prefixes.put("Moderator", "§bMod | ");
		prefixes.put("Builder", "§2Builder | ");
		prefixes.put("YouTube", "§9YT | ");
		prefixes.put("PremiumZehn", "§6X | ");
		prefixes.put("PremiumNeun", "§6IX | ");
		prefixes.put("PremiumAcht", "§eVIII | ");
		prefixes.put("PremiumSieben", "§eVII | ");
		prefixes.put("PremiumSechs", "§eVI | ");
		prefixes.put("PremiumFuenf", "§eV | ");
		prefixes.put("PremiumVier", "§eIV | ");
		prefixes.put("PremiumDrei", "§eIII | ");
		prefixes.put("PremiumZwei", "§eII | ");
		prefixes.put("PremiumEins", "§eI | ");
		prefixes.put("RegSpieler", "§7");
		prefixes.put("UnregSpieler", "§8");

	}

	public static TimoCloudUniversalAPI getUniversalTimoapi() {
		return universalTimoapi;
	}

	public static void setUniversalTimoapi(TimoCloudUniversalAPI universalTimoapi) {
		Main.universalTimoapi = universalTimoapi;
	}

	public static TimoCloudBukkitAPI getBukkitTimoapi() {
		return bukkitTimoapi;
	}

	public static void setBukkitTimoapi(TimoCloudBukkitAPI bukkitTimoapi) {
		Main.bukkitTimoapi = bukkitTimoapi;
	}

}
