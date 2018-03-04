package de.biomia.spigot.server.minigames.versus.vs.game.sw;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.configs.SkyWarsVersusConfig;
import de.biomia.spigot.server.minigames.versus.sw.chests.Chests;
import de.biomia.spigot.server.minigames.versus.sw.kits.KitManager;
import de.biomia.spigot.server.minigames.versus.vs.game.GameInstance;
import de.biomia.spigot.server.minigames.versus.vs.game.GameMode;
import de.biomia.spigot.server.minigames.versus.vs.game.TeamColor;

import java.util.ArrayList;
import java.util.HashMap;

public class SkyWars extends GameMode {

    private final Chests chests = new Chests(this);

    public SkyWars(GameInstance instance) {
        super(instance);
        HashMap<Integer, ArrayList<BiomiaPlayer>> teamPlayers = splitPlayers(getPlayers());
        team1 = new SkyWarsTeam(this, TeamColor.BLUE, teamPlayers.get(1), SkyWarsVersusConfig.loadLocsFromConfig(instance.getMapID(), TeamColor.BLUE.getID(), instance.getWorld()));
        team2 = new SkyWarsTeam(this, TeamColor.RED, teamPlayers.get(2), SkyWarsVersusConfig.loadLocsFromConfig(instance.getMapID(), TeamColor.RED.getID(), instance.getWorld()));
        handler = new SkyWarsHandler(this);
    }

    public Chests getChests() {
        return chests;
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
