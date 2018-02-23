package de.biomia.freebuild.main;

import de.biomia.freebuild.commands.BuildForAllCommand;
import de.biomia.freebuild.commands.RespawnCommand;
import de.biomia.freebuild.home.Home;
import de.biomia.freebuild.home.commands.*;
import de.biomia.freebuild.home.homes.HomeManager;
import de.biomiaAPI.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class FreebuildMain{

	static Home home;

	public static void initFreebuild() {
		home = new Home();
		home.onEnable();
		registerEvents();
		loadListeners();
	}
	
	public static void terminateFreebuild() {
		home.onDisable();
	}
	
	private static void registerEvents() {
		// GENERAL
		Main.getPlugin().getCommand("respawn").setExecutor(new RespawnCommand());
		Main.getPlugin().getCommand("buildforall").setExecutor(new BuildForAllCommand());
		//-----
		// HOME
		HomeManager homeManager = home.getHomeManager();
		Main.getPlugin().getCommand("delhome").setExecutor(new DeleteHomeCommand(homeManager));
		Main.getPlugin().getCommand("home").setExecutor(new HomeCommand(homeManager));
		Main.getPlugin().getCommand("homelist").setExecutor(new HomeListCommand(homeManager));
		Main.getPlugin().getCommand("otherhome").setExecutor(new OtherHomeCommand(homeManager));
		Main.getPlugin().getCommand("sethome").setExecutor(new SetHomeCommand(homeManager));
		Main.getPlugin().getCommand("shreload").setExecutor(new ReloadCommand());
	}
	
	private static void loadListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new FreebuildListener(), Main.getPlugin());
		home.loadListeners();
	}
	
}
