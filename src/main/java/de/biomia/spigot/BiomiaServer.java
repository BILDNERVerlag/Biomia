package de.biomia.spigot;

import de.biomia.spigot.commands.general.*;
import de.biomia.spigot.listeners.CosmeticListener;
import de.biomia.spigot.listeners.ReportListener;
import org.bukkit.Bukkit;

import static de.biomia.spigot.Main.registerCommand;

public abstract class BiomiaServer {

    private final BiomiaServerType serverType;

    protected BiomiaServer(BiomiaServerType type) {
        this.serverType = type;
    }

    public BiomiaServerType getServerType() {
        return serverType;
    }

    public void start() {
        initListeners();
        initCommands();
    }

    public void stop() {
    }

    protected void initListeners() {
        Bukkit.getPluginManager().registerEvents(new ReportListener(), Main.getPlugin());
        Bukkit.getPluginManager().registerEvents(new CosmeticListener(), Main.getPlugin());
    }

    protected void initCommands() {
        registerCommand(new RandomServerGroupCommand());
        registerCommand(new CosmeticCommand());
        registerCommand(new BuildCommand());
        registerCommand(new CoinsCommand());
        registerCommand(new CreateTableCommand());
        registerCommand(new EatCommand());
        registerCommand(new FlyCommand());
        registerCommand(new GamemodeCommand());
        registerCommand(new HeadCommand());
        registerCommand(new HealCommand());
        registerCommand(new HologramCommand());
        registerCommand(new PermissionCommand());
        registerCommand(new RankCommand());
        registerCommand(new SpeedCommand());
        registerCommand(new StatCommand());
        registerCommand(new ReportCommand());
        registerCommand(new SeeReportsCommand());
        registerCommand(new StatCommand());
        registerCommand(new LogCommand());
        registerCommand(new TrollCommand("crash"));
        registerCommand(new TrollCommand("troll"));
        registerCommand(new EventCommands("addeggs"));
        registerCommand(new EventCommands("givereward"));
        registerCommand(new WarpCommand("setwarp"));
        registerCommand(new WarpCommand("warp"));
        registerCommand(new WarpCommand("delwarp"));
    }


}

