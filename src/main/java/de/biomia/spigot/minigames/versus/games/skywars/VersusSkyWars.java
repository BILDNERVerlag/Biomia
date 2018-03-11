package de.biomia.spigot.minigames.versus.games.skywars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.configs.SkyWarsVersusConfig;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.minigames.versus.Versus;
import de.biomia.spigot.minigames.versus.games.skywars.chests.Chests;
import de.biomia.spigot.minigames.general.kits.KitManager;
import org.bukkit.Bukkit;

public class VersusSkyWars extends GameMode {

    private final Chests chests = new Chests(this);

    public VersusSkyWars(GameInstance instance) {
        super(instance);
        Bukkit.broadcastMessage("%%% starting SkyWarsInstance");
        new SkyWarsTeam(this, TeamColor.BLUE, SkyWarsVersusConfig.loadLocsFromConfig(instance.getMapDisplayName(), TeamColor.BLUE.getID(), instance.getWorld()));
        new SkyWarsTeam(this, TeamColor.RED, SkyWarsVersusConfig.loadLocsFromConfig(instance.getMapDisplayName(), TeamColor.RED.getID(), instance.getWorld()));
        handler = new SkyWarsHandler(this);
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
            bp.getPlayer().teleport(getTeam(bp).getHome());
            KitManager.getManager(bp).setKitInventory();
        }
    }

    @Override
    public void stop() {
        super.stop();
        ((Versus) Biomia.getSeverInstance()).getManager().getRequests().get(getInstance()).finish();
    }
}
