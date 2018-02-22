package de.biomia.versus.vs.game.sw;

import de.biomia.versus.global.configs.SkyWarsConfig;
import de.biomia.versus.sw.chests.Chests;
import de.biomia.versus.sw.kits.KitManager;
import de.biomia.versus.vs.game.GameInstance;
import de.biomia.versus.vs.game.GameMode;
import de.biomia.versus.vs.game.TeamColor;
import de.biomiaAPI.BiomiaPlayer;

import java.util.ArrayList;
import java.util.HashMap;

public class SkyWars extends GameMode {

    private final Chests chests = new Chests(this);

    public SkyWars(GameInstance instance) {
        super(instance);
        HashMap<Integer, ArrayList<BiomiaPlayer>> teamPlayers = splitPlayers(getPlayers());
        team1 = new SkyWarsTeam(this, TeamColor.BLUE, teamPlayers.get(1), SkyWarsConfig.loadLocsFromConfig(instance.getMapID(), TeamColor.BLUE.getID(), instance.getWorld()));
        team2 = new SkyWarsTeam(this, TeamColor.RED, teamPlayers.get(2), SkyWarsConfig.loadLocsFromConfig(instance.getMapID(), TeamColor.RED.getID(), instance.getWorld()));
        handler = new SkyWarsHandler(this);
    }

    public Chests getChests() {
        return chests;
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void start() {
        for (BiomiaPlayer bp : getPlayers()) {
            bp.setGetDamage(true);
            bp.setDamageEntitys(true);
            bp.setBuild(true);
            bp.getPlayer().getInventory().clear();
            bp.getPlayer().teleport(getTeam(bp).getHome());
            KitManager.getManager(bp).setKitInventory();
        }
    }

}
