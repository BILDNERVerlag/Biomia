package de.biomia.spigot.minigames.bedwars;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.configs.BedWarsConfig;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.events.bedwars.BedWarsEndEvent;
import de.biomia.spigot.events.bedwars.BedWarsStartEvent;
import de.biomia.spigot.minigames.*;
import de.biomia.spigot.minigames.general.SpawnItems;
import de.biomia.spigot.minigames.general.TeamSwitcher;
import de.biomia.spigot.minigames.general.shop.Shop;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class BedWars extends GameMode {

    //TODO: bei spielende die leute ordentlich rausteleporten
    //TODO: schaden in der lobby

    @Override
    protected GameHandler initHandler() {
        return new BedWarsListener(this);
    }

    public final HashMap<GameTeam, ArrayList<Block>> teamChestsLocs = new HashMap<>();
    public final HashMap<UUID, ArrayList<Player>> handlerMap = new HashMap<>();

    public BedWars(GameInstance instance) {
        super(instance);
    }

    @Override
    public void start() {

        getStateManager().setInGameState(new GameStateManager.InGameState(this) {

            @Override
            public void start() {
                super.start();
                new SpawnItems(((BedWarsConfig) getConfig()).loadSpawner(getInstance()), getInstance().getWorld()).startSpawning();
                Bukkit.getPluginManager().callEvent(new BedWarsStartEvent(getMode()));
            }

            @Override
            public void stop() {
                ArrayList<BiomiaPlayer> biomiaPlayersWinner = new ArrayList<>();
                String winnerTeam = null;

                for (GameTeam teams : getMode().getTeams()) {
                    for (BiomiaPlayer bp : teams.getPlayers()) {
                        if (teams.lives(bp)) {
                            biomiaPlayersWinner.add(bp);
                            winnerTeam = teams.getTeamname();
                        }
                    }
                }

                Bukkit.getPluginManager().callEvent(new BedWarsEndEvent(biomiaPlayersWinner, getMode().getInstance().getPlayedTime(), winnerTeam));
                super.stop();
            }
        });

        super.start();
        Shop.init();
        Bukkit.getPluginManager().registerEvents(new SpecialItems(this), Main.getPlugin());

        teamSwitcher = TeamSwitcher.getTeamSwitcher(this);
    }

    public GameTeam getTeamByTeamChests(Block block) {
        for (GameTeam team : getTeams()) {
            if (teamChestsLocs.containsKey(team)) {
                for (Block b : teamChestsLocs.get(team)) {
                    if (block.equals(b)) {
                        return team;
                    }
                }
            }
        }
        return null;
    }


    @Override
    protected MinigamesConfig initConfig() {
        return new BedWarsConfig(this);
    }
}
