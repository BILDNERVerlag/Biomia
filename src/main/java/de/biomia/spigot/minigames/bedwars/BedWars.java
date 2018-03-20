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

    //TODO: messages anpassen (farben + satzzeichen)
    //TODO: mit den chatlistenern (und vmtl auch n paar anderen listenern) ist irgendetwas echt funky (siehe testnachrichten), bedwarslistener.onChat und gamehandler.onChat werden je zweimal aufgerufen bei jeder msg
    //vmtl ne gute idee onChat in gamehandler nichtmehr final zu machen und in bwlistener das ganze zu überschreiben (würde zumindest schonmal 2 statt 4 nachrichten pro nachricht bedeuten), aber nicht sicher ob damit in SW was kaputtgeht
    //TODO: spawnpunkt zum bett setzen
    //TODO: bei spielende die leute ordentlich rausteleporten
    //TODO: @all-Nachrichten nicht überall gleichzeitig hinschicken (aktuell bekommt man in der lobby die nachrichten auch)

    @Override
    protected GameHandler initHandler() {
        return new BedWarsListener(this);
    }

    private static BedWars instance;

    public final HashMap<GameTeam, ArrayList<Block>> teamChestsLocs = new HashMap<>();
    public final HashMap<UUID, ArrayList<Player>> handlerMap = new HashMap<>();

    public BedWars(GameInstance instance) {
        super(instance);
        BedWars.instance = this;
    }

    @Override
    public void start() {
        getStateManager().setLobbyState(new GameStateManager.LobbyState(this) {
            private SpawnItems itemManager;

            @Override
            public void start() {
                super.start();
                itemManager = new SpawnItems(((BedWarsConfig) getConfig()).loadSpawner(getInstance()), getInstance().getWorld());
                itemManager.startSpawning();
            }
        });

        getStateManager().setInGameState(new GameStateManager.InGameState(this) {
            @Override
            public void start() {
                super.start();
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
