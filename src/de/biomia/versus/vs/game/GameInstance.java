package de.biomia.versus.vs.game;

import de.biomia.versus.vs.game.bw.BedWars;
import de.biomia.versus.vs.game.pvp.KitPvP;
import de.biomia.versus.vs.game.sw.SkyWars;
import de.biomia.versus.vs.main.VSManager;
import de.biomia.versus.vs.settings.VSRequest;
import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.tools.WorldCopy;
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
    private final VSManager.VSMode mode;
    private final VSRequest request;
    private final World world;
    private GameMode gameMode;

    public GameInstance(VSManager.VSMode mode, int id, int mapID, VSRequest request) {
        this.mapID = mapID;
        this.id = id;
        this.mode = mode;
        this.request = request;
        this.world = copyWorld();

        switch (mode) {
            case KitPVP:
                gameMode = new KitPvP(this);
                break;
            case BedWars:
                gameMode = new BedWars(this);
                break;
            case SkyWars:
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
        } catch (IOException e) {
            //TODO ?
            e.printStackTrace();
        }
    }
}
