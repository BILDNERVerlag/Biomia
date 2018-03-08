package de.biomia.spigot.minigames.versus;

import de.biomia.spigot.Main;
import de.biomia.spigot.commands.minigames.versus.BWCommand;
import de.biomia.spigot.general.BiomiaServer;
import de.biomia.spigot.listeners.servers.VersusLobbyListener;
import de.biomia.spigot.minigames.versus.games.skywars.chests.Items;
import de.biomia.spigot.commands.minigames.versus.SWCommand;
import de.biomia.spigot.minigames.versus.games.skywars.kits.KitManager;
import de.biomia.spigot.commands.minigames.versus.VSCommands;
import org.bukkit.Bukkit;

public class Versus extends BiomiaServer {

    private VSManager manager;

    private static Versus instance;

    public static Versus getInstance() {
        return instance;
    }

    @Override
    public void start() {
        super.start();
        instance = this;
        manager = new VSManager();
        Items.init();
        KitManager.initKits();
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        Bukkit.getPluginManager().registerEvents(new VersusLobbyListener(), Main.getPlugin());
    }

    @Override
    protected void initCommands() {
        super.initCommands();
        Main.registerCommand(new VSCommands("accept"));
        Main.registerCommand(new VSCommands("decline"));
        Main.registerCommand(new VSCommands("request"));
        Main.registerCommand(new VSCommands("spawn"));

        Main.registerCommand(new BWCommand());
        Main.registerCommand(new SWCommand());
    }

    public VSManager getManager() {
        return manager;
    }

}