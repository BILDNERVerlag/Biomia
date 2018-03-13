package de.biomia.spigot.minigames.versus.games.bedwars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.configs.BedWarsConfig;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.minigames.general.SpawnItems;
import de.biomia.spigot.minigames.versus.Versus;

public class VersusBedWars extends GameMode {

    private final BedWarsScoreboard bedWarsScoreboard;
    private final BedWarsShopListener shop = new BedWarsShopListener(this);
    private final SpawnItems spawnItems = new SpawnItems(((BedWarsConfig) getConfig()).loadSpawner(getInstance()), getInstance().getWorld());

    public VersusBedWars(GameInstance instance) {
        super(instance);
        new VersusBedWarsTeam(this, TeamColor.BLUE);
        new VersusBedWarsTeam(this, TeamColor.RED);
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

    @Override
    protected MinigamesConfig initConfig() {
        return new BedWarsConfig();
    }

}
