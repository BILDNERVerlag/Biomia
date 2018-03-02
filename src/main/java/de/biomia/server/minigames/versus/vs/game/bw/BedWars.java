package de.biomia.server.minigames.versus.vs.game.bw;

import de.biomia.BiomiaPlayer;
import de.biomia.data.configs.BedWarsVersusConfig;
import de.biomia.server.minigames.versus.bw.listeners.SpawnItems;
import de.biomia.server.minigames.versus.bw.shop.BedWarsShopListener;
import de.biomia.server.minigames.versus.bw.var.BedWarsScoreboard;
import de.biomia.server.minigames.versus.vs.game.GameInstance;
import de.biomia.server.minigames.versus.vs.game.GameMode;
import de.biomia.server.minigames.versus.vs.game.TeamColor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class BedWars extends GameMode {

    private final BedWarsScoreboard bedWarsScoreboard;
    private final BedWarsShopListener shop = new BedWarsShopListener(this);
    private final SpawnItems spawnItems = new SpawnItems();

    public BedWars(GameInstance instance) {
        super(instance);

        bedWarsScoreboard = new BedWarsScoreboard(this);

        HashMap<Integer, ArrayList<BiomiaPlayer>> teamPlayers = splitPlayers(getPlayers());
        Location loc1 = BedWarsVersusConfig.getLocation(getInstance().getMapID(), 1, getInstance().getWorld());
        Location loc2 = BedWarsVersusConfig.getLocation(getInstance().getMapID(), 2, getInstance().getWorld());
        team1 = new BedWarsTeam(this, TeamColor.BLUE, teamPlayers.get(1), loc1);
        team2 = new BedWarsTeam(this, TeamColor.RED, teamPlayers.get(2), loc2);
        handler = new BedWarsHandler(this);
    }

    public BedWarsScoreboard getBedWarsScoreboard() {
        return bedWarsScoreboard;
    }

    @Override
    public void start() {
        spawnItems.startSpawning(this);
        for (BiomiaPlayer bp : getPlayers()) {
            bp.setGetDamage(true);
            bp.setDamageEntitys(true);
            bp.setBuild(true);
            bp.getPlayer().getInventory().clear();
            bedWarsScoreboard.setScoreboard(bp, false);
            bp.getPlayer().teleport(getTeam(bp).getHome());
        }
    }

    @Override
    public void stop() {
        super.stop();
        spawnItems.stopSpawning();
        shop.unregister();
    }

}
