package de.biomia.spigot.minigames.versus.games.bedwars;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.configs.BedWarsVersusConfig;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.minigames.general.SpawnItems;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class BedWars extends GameMode {

    private final BedWarsScoreboard bedWarsScoreboard;
    private final BedWarsShopListener shop = new BedWarsShopListener(this);
    private final SpawnItems spawnItems = new SpawnItems(getInstance().getWorld());

    public BedWars(GameInstance instance) {
        super(instance);

        bedWarsScoreboard = new BedWarsScoreboard(this);

        HashMap<Integer, ArrayList<BiomiaPlayer>> teamPlayers = splitPlayersInTwoTeams(getPlayers());
        Location loc1 = BedWarsVersusConfig.getLocation(getInstance().getMapID(), 1, getInstance().getWorld());
        Location loc2 = BedWarsVersusConfig.getLocation(getInstance().getMapID(), 2, getInstance().getWorld());
        new BedWarsTeam(this, TeamColor.BLUE, teamPlayers.get(1), loc1);
        new BedWarsTeam(this, TeamColor.RED, teamPlayers.get(2), loc2);
        handler = new BedWarsHandler(this);
    }

    public BedWarsScoreboard getBedWarsScoreboard() {
        return bedWarsScoreboard;
    }

    @Override
    public void start() {
        spawnItems.startSpawning();
        for (BiomiaPlayer bp : getPlayers()) {
            bp.setGetDamage(true);
            bp.setDamageEntitys(true);
            bp.setBuild(true);
            bp.getPlayer().getInventory().clear();
            bedWarsScoreboard.setScoreboard(bp, false);
            bp.getPlayer().setCollidable(false);
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
