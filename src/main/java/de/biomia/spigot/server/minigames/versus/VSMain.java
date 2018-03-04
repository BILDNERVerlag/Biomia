package de.biomia.spigot.server.minigames.versus;

import de.biomia.spigot.Main;
import de.biomia.spigot.commands.minigames.versus.BWCommand;
import de.biomia.spigot.listeners.servers.VersusLobbyListener;
import de.biomia.spigot.server.minigames.versus.sw.chests.Items;
import de.biomia.spigot.commands.minigames.versus.SWCommand;
import de.biomia.spigot.server.minigames.versus.sw.kits.KitManager;
import de.biomia.spigot.server.minigames.versus.vs.commands.VSCommands;
import de.biomia.spigot.server.minigames.versus.vs.main.VSManager;
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