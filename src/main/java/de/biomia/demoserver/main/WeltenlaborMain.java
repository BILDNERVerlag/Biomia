package de.biomia.demoserver.main;

import de.biomia.demoserver.cmds.Bau;
import de.biomia.demoserver.config.Bauten;
import de.biomia.demoserver.config.Config;
import de.biomia.demoserver.listeners.BiomiaListener;
import de.biomia.api.main.Main;
import de.biomia.api.msg.Scoreboards;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class WeltenlaborMain {

	public static ArrayList<Bauten> bauten = new ArrayList<>();
	public static ScrollingInventory si;

	public static void initWeltenlabor() {

		si = new ScrollingInventory("§eW\u00e4hle dein Ziel!", 3);
		
		Bukkit.getPluginManager().registerEvents(new BiomiaListener(), Main.getPlugin());

		Main.getPlugin().getCommand("bau").setExecutor(new Bau());

		for (Player p : Bukkit.getOnlinePlayers()) {
			Scoreboards.setTabList(p);
		}

		Main.getPlugin().getConfig().addDefault("lastID", 0);
		Main.getPlugin().saveDefaultConfig();
		Config.hookInPlugin();

	}

}
