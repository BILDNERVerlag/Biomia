package de.biomia.spigot.minigames.versus;

import de.biomia.spigot.Main;
import de.biomia.spigot.commands.minigames.versus.BWCommand;
import de.biomia.spigot.listeners.servers.VersusLobbyListener;
import de.biomia.spigot.minigames.versus.games.skywars.chests.Items;
import de.biomia.spigot.commands.minigames.versus.SWCommand;
import de.biomia.spigot.minigames.versus.games.skywars.kits.KitManager;
import de.biomia.spigot.commands.minigames.versus.VSCommands;
import org.bukkit.Bukkit;

public class VSMain {

    private static VSManager manager;

    public static void initVersus() {
        manager = new VSManager();
        loadListener();
        loadCommands();
        Items.init();
        KitManager.initKits();
    }

    private static void loadListener() {
        Bukkit.getPluginManager().registerEvents(new VersusLobbyListener(), Main.getPlugin());
    }

    public static VSManager getManager() {
        return manager;
    }

    private static void loadCommands() {

        Main.registerCommand(new VSCommands("accept"));
        Main.registerCommand(new VSCommands("decline"));
        Main.registerCommand(new VSCommands("request"));
        Main.registerCommand(new VSCommands("spawn"));

        Main.registerCommand(new BWCommand());
        Main.registerCommand(new SWCommand());
    }

}