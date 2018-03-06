package de.biomia.spigot.minigames;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.versus.games.bedwars.BedWars;
import de.biomia.spigot.minigames.versus.games.kitpvp.KitPvP;
import de.biomia.spigot.minigames.versus.games.skywars.SkyWars;
import de.biomia.spigot.minigames.versus.settings.VSRequest;
import de.biomia.spigot.tools.WorldCopy;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GameInstance {

    private final int id;
    private final int mapID;
    private final GameType mode;
    private final VSRequest request;
    private final World world;
    private GameMode gameMode;
    private int playedTime = 0;

    public GameInstance(GameType mode, int id, int mapID, VSRequest request) {
        this.mapID = mapID;
        this.id = id;
        this.mode = mode;
        this.request = request;
        this.world = copyWorld();

        switch (mode) {
        case KIT_PVP:
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

    private World copyWorld() {

        String name = mode.name() + "_" + mapID;

        World w = Bukkit.getWorld(name);
        if (w == null) {
            w = new WorldCreator(name).createWorld();
        }
        return WorldCopy.copyWorld(w, name.concat("_" + id));
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public ArrayList<BiomiaPlayer> getPlayers() {
        return new ArrayList<>(Arrays.asList(request.getLeader(), request.getReceiver()));
    }

    public VSRequest getRequest() {
        return request;
    }

    public World getWorld() {
        return world;
    }

    public int getMapID() {
        return mapID;
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

    public int getPlayedTime() {
        return playedTime;
    }
}
