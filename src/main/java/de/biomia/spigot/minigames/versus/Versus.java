package de.biomia.spigot.minigames.versus;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.BiomiaServer;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.Main;
import de.biomia.spigot.commands.minigames.BWCommand;
import de.biomia.spigot.commands.minigames.KitPVPCommand;
import de.biomia.spigot.commands.minigames.SWCommand;
import de.biomia.spigot.commands.minigames.versus.VSCommands;
import de.biomia.spigot.minigames.WarteLobbyListener;
import de.biomia.spigot.minigames.general.chests.Items;
import de.biomia.spigot.minigames.kitpvp.KitPVPManager;
import de.biomia.spigot.tools.BackToLobby;
import de.biomia.spigot.tools.Hologram;
import de.biomia.spigot.tools.TeleportExecutor;
import de.biomia.spigot.tools.Teleporter;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

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
        World duell = Bukkit.getWorld("Spawn");
        //sets gamemode when entering temple
        new Teleporter(new Location(duell, -51, 113, -42.5), new Location(duell, -13, 200, -6), new TeleportExecutor() {
            @Override
            public void execute(BiomiaPlayer bp) {
                KitPVPManager.setToEditMode(bp);
            }
        });
        new Teleporter(new Location(duell, -51, 113, -42.5), new Location(duell, -13, 200, -6), new TeleportExecutor() {
            @Override
            public void execute(BiomiaPlayer bp) {
                KitPVPManager.removeFromEditMode(bp);
                bp.getPlayer().sendMessage(String.format("%sDein Kit wurde gespeichert.", Messages.COLOR_MAIN));
            }
        }).setInverted();
        //teleporters
        new Teleporter(new Location(duell, -41, 83.5, -101), new Location(duell, -38, 86, -100), new Location(duell, -36.5, 105, -40.5, -90, 0));
        new Teleporter(new Location(duell, -38, 105, -42), new Location(duell, -37, 105, -39), new Location(duell, -39.5, 84, -101.5, -180, 0));
        Villager v = (Villager) duell.spawnEntity(new Location(duell, -30.5, 84, -101.5, -180, 0), EntityType.VILLAGER);
        v.setCustomName("§aWarteschlange");
        v.setCustomNameVisible(true);
        v.setProfession(Villager.Profession.FARMER);
        v.setAI(false);
        Hologram.newHologram(new Location(duell, -39.5, 84, -100.5), new String[]{"§aZum Kit", "§aanpassen"});
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        Bukkit.getPluginManager().registerEvents(new WarteLobbyListener(true), Main.getPlugin());
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
        Main.registerCommand(new KitPVPCommand());
    }

    public VSManager getManager() {
        return manager;
    }

}