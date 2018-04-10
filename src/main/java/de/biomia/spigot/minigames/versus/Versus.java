package de.biomia.spigot.minigames.versus;

import de.biomia.spigot.BiomiaServer;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.Main;
import de.biomia.spigot.commands.minigames.BWCommand;
import de.biomia.spigot.commands.minigames.SWCommand;
import de.biomia.spigot.commands.minigames.versus.VSCommands;
import de.biomia.spigot.minigames.WaitingLobbyListener;
import de.biomia.spigot.minigames.general.chests.Items;
import org.bukkit.Bukkit;

public class Versus extends BiomiaServer {

    private VSManager manager;

    private static Versus instance;

    public Versus() {
        super(BiomiaServerType.Duell);
    }

    public static Versus getInstance() {
        return instance;
    }

    @Override
    public void start() {
        super.start();
        instance = this;
        manager = new VSManager();
        Items.init();
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        Bukkit.getPluginManager().registerEvents(new WaitingLobbyListener(true), Main.getPlugin());
    }

    @Override
    protected void initCommands() {
        super.initCommands();
        Main.registerCommand(new VSCommands("accept"));
        Main.registerCommand(new VSCommands("decline"));
        Main.registerCommand(new VSCommands("request"));
        Main.registerCommand(new VSCommands("spawn"));

        Main.registerCommand(new SWCommand());
        Main.registerCommand(new BWCommand());
    }

    public VSManager getManager() {
        return manager;
    }

}