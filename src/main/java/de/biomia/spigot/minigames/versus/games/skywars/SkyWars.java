package de.biomia.spigot.minigames.versus.games.skywars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.configs.SkyWarsVersusConfig;
import de.biomia.spigot.minigames.versus.Versus;
import de.biomia.spigot.minigames.versus.games.skywars.chests.Chests;
import de.biomia.spigot.minigames.versus.games.skywars.kits.KitManager;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;

public class SkyWars extends GameMode {

    private final Chests chests = new Chests(this);

    public SkyWars(GameInstance instance) {
        super(instance);
        Bukkit.broadcastMessage("%%% starting SkyWarsInstance");
        HashMap<Integer, ArrayList<BiomiaPlayer>> teamPlayers = splitPlayersInTwoTeams(getInstance().getPlayers());
        new SkyWarsTeam(this, TeamColor.BLUE, teamPlayers.get(1), SkyWarsVersusConfig.loadLocsFromConfig(instance.getMapDisplayName(), TeamColor.BLUE.getID(), instance.getWorld()));
        new SkyWarsTeam(this, TeamColor.RED, teamPlayers.get(2), SkyWarsVersusConfig.loadLocsFromConfig(instance.getMapDisplayName(), TeamColor.RED.getID(), instance.getWorld()));
        handler = new SkyWarsHandler(this);
        Bukkit.broadcastMessage("%%% SkyWars constructor ends");
    }

    public Chests getChests() {
        return chests;
    }

    @Override
    public void start() {
        Bukkit.broadcastMessage("%%% SkyWars.start()");
        for (BiomiaPlayer bp : getInstance().getPlayers()) {
            bp.setGetDamage(true);
            bp.setDamageEntitys(true);
            bp.setBuild(true);
            bp.getPlayer().getInventory().clear();
            //TODO: getTeam(bp) is null
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
