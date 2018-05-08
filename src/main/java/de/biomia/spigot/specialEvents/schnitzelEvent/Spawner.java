package de.biomia.spigot.specialEvents.schnitzelEvent;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;

class Spawner {

    private static final ArrayList<Spawner> monsterSpawner = new ArrayList<>();

    public static ArrayList<Spawner> getMonsterSpawner() {
        return monsterSpawner;
    }

    private final EntityType type;
    private final Location location;
    private final int distance;
    private final int monsterPerPlayer;

    private int lastSpawnedTime = 0;
    private final ArrayList<Location> possibleSpawnLocations = new ArrayList<>();

    Spawner(EntityType type, Location location, int distance, int monsterPerPlayer) {
        this.type = type;
        this.location = location;
        this.distance = distance;
        this.monsterPerPlayer = monsterPerPlayer;
        monsterSpawner.add(this);
    }

    public Location getLocation() {
        return location;
    }

    public EntityType getType() {
        return type;
    }

    public int getDistance() {
        return distance;
    }

    public int getMonsterPerPlayer() {
        return monsterPerPlayer;
    }

    public void setLastSpawnedTime(int nowInSeconds) {
        this.lastSpawnedTime = nowInSeconds;
    }

    public int getLastSpawnedTime() {
        return lastSpawnedTime;
    }

    /**
     * to made it more efficient
     */
    public ArrayList<Location> getPossibleSpawnLocations() {
        return possibleSpawnLocations;
    }
}
