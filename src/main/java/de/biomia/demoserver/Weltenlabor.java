package de.biomia.demoserver;

import de.biomia.demoserver.commands.BauCommand;
import de.biomia.demoserver.teleporter.Bauten;
import de.biomia.demoserver.teleporter.ScrollingInventory;
import de.biomia.general.configs.DemoConfig;
import de.biomia.demoserver.listeners.DemoListener;
import de.biomia.Main;
import de.biomia.api.messages.Scoreboards;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Weltenlabor {

	public static final ArrayList<Bauten> bauten = new ArrayList<>();
	public static ScrollingInventory si;

	public static void init() {

		si = new ScrollingInventory("\u00A7eW\u00e4hle dein Ziel!", 3);

		Bukkit.getPluginManager().registerEvents(new DemoListener(), Main.getPlugin());

		Main.getPlugin().getCommand("bauwerk").setExecutor(new BauCommand());

		for (Player p : Bukkit.getOnlinePlayers()) {
			Scoreboards.setTabList(p);
		}

		Main.getPlugin().getConfig().addDefault("lastID", 0);
		Main.getPlugin().saveDefaultConfig();
		DemoConfig.hookInPlugin();

	}

}
