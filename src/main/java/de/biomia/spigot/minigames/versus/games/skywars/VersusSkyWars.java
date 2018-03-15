package de.biomia.spigot.minigames.versus.games.skywars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.configs.SkyWarsConfig;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.general.kits.KitManager;
import de.biomia.spigot.minigames.versus.Versus;
import de.biomia.spigot.minigames.versus.games.skywars.chests.Chests;
import org.bukkit.Bukkit;

public class VersusSkyWars extends GameMode {

    @Override
    protected GameHandler initHandler() {
        return new SkyWarsHandler(this);
    }

    private final Chests chests = new Chests(this);

    public VersusSkyWars(GameInstance instance) {
        super(instance);
        Bukkit.broadcastMessage("%%% starting SkyWarsInstance");
        Bukkit.broadcastMessage("%%% SkyWars constructor ends");
    }

    public Chests getChests() {
        return chests;
    }

    @Override
    public void start() {
        splitPlayersInTwoTeams();
        Bukkit.broadcastMessage("%%% SkyWars.start()");
        for (BiomiaPlayer bp : getInstance().getPlayers()) {
            bp.setGetDamage(true);
            bp.setDamageEntitys(true);
            bp.setBuild(true);
            bp.getPlayer().getInventory().clear();
            bp.getPlayer().teleport(bp.getTeam().getHome());
            KitManager.getManager(bp).setKitInventory();
        }
    }

    @Override
    public void stop() {
        super.stop();
        ((Versus) Biomia.getServerInstance()).getManager().getRequests().get(getInstance()).finish();
    }

    @Override
    protected MinigamesConfig initConfig() {
        return new SkyWarsConfig();
    }
}
