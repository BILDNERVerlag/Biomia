package de.biomia.spigot.minigames;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.versus.games.bedwars.BedWars;
import de.biomia.spigot.minigames.versus.games.kitpvp.KitPvP;
import de.biomia.spigot.minigames.versus.games.skywars.SkyWars;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;
import java.util.ArrayList;

public class GameInstance {

    private final GameType mode;
    private final World world;
    private GameMode gameMode;
    private int playedTime = 0;
    private final String mapDisplayName;
    private final ArrayList<BiomiaPlayer> players = new ArrayList<>();
    private int teamSize;

    public GameInstance(GameType mode, World world, String mapDisplayName) {
        Bukkit.broadcastMessage("%%% making a new gameInstance");
        this.mode = mode;
        this.world = world;
        this.mapDisplayName = mapDisplayName;
        switch (mode) {
            case KIT_PVP_VS:
                gameMode = new KitPvP(this);
                break;
            case BED_WARS_VS:
                gameMode = new BedWars(this);
                break;
            case SKY_WARS_VS:
                gameMode = new SkyWars(this);
                break;
            default:
                break;
        }
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public ArrayList<BiomiaPlayer> getPlayers() {
        return players;
    }

    public World getWorld() {
        return world;
    }

    void startDeleting() {
        Bukkit.unloadWorld(world, false);
        try {
            FileUtils.deleteDirectory(world.getWorldFolder());
        } catch (IOException ignored) {
        }
    }

    public GameType getType() {
        return mode;
    }

    public void incPlayTime() {
        playedTime++;
    }

    public boolean containsPlayer(BiomiaPlayer bp) {
        return getPlayers().contains(bp);
    }

    public void addPlayer(BiomiaPlayer bp) {
        players.add(bp);
    }

    public String getMapDisplayName() {
        return mapDisplayName;
    }

    public int getPlayedTime() {
        return playedTime;
    }
}
