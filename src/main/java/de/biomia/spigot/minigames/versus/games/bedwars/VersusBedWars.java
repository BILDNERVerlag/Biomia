package de.biomia.spigot.minigames.versus.games.bedwars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.configs.BedWarsVersusConfig;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.minigames.general.SpawnItems;
import de.biomia.spigot.minigames.versus.Versus;
import org.bukkit.Location;

public class VersusBedWars extends GameMode {

    private final BedWarsScoreboard bedWarsScoreboard;
    private final BedWarsShopListener shop = new BedWarsShopListener(this);
    private final SpawnItems spawnItems = new SpawnItems(BedWarsVersusConfig.getSpawner(getInstance()), getInstance().getWorld());

    public VersusBedWars(GameInstance instance) {
        super(instance);
        Location locOne = BedWarsVersusConfig.getLocation(getInstance().getMapDisplayName(), 1, getInstance().getWorld());
        Location locTwo = BedWarsVersusConfig.getLocation(getInstance().getMapDisplayName(), 2, getInstance().getWorld());
        new VersusBedWarsTeam(this, TeamColor.BLUE, locOne);
        new VersusBedWarsTeam(this, TeamColor.RED, locTwo);
        bedWarsScoreboard = new BedWarsScoreboard(this);
        handler = new BedWarsHandler(this);
    }

    public BedWarsScoreboard getBedWarsScoreboard() {
        return bedWarsScoreboard;
    }

    @Override
    public void start() {
        splitPlayersInTwoTeams();
        spawnItems.startSpawning();
        for (BiomiaPlayer bp : getInstance().getPlayers()) {
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
        ((Versus) Biomia.getSeverInstance()).getManager().getRequests().get(getInstance()).finish();
    }

}
