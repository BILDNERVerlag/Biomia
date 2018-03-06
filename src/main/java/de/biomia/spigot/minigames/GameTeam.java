package de.biomia.spigot.minigames;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.general.Dead;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameTeam {

    protected final GameMode mode;
    private final HashMap<BiomiaPlayer, Boolean> players;
    private final TeamColor color;
    private final Location home;

    protected GameTeam(TeamColor color, ArrayList<BiomiaPlayer> player, Location loc, GameMode mode) {
        this.players = new HashMap<>();
        for (BiomiaPlayer bp : player)
            players.put(bp, true);
        this.color = color;
        this.home = loc;
        this.mode = mode;
        mode.registerTeam(this);
    }

    public boolean lives(BiomiaPlayer bp) {
        return players.get(bp);
    }

    public void setDead(BiomiaPlayer bp) {
        players.put(bp, false);
        Dead.setDead(bp);
        if (mode.canStop())
            mode.stop();
    }

    public boolean containsPlayer(BiomiaPlayer bp) {
        return players.containsKey(bp);
    }

    public ArrayList<BiomiaPlayer> getPlayers() {
        return new ArrayList<>(players.keySet());
    }

    public String getColorcode() {
        return color.getColorcode();
    }

    public String getTeamname() {
        return color.getGermanName();
    }

    public TeamColor getColor() {
        return color;
    }

    public void leave(BiomiaPlayer bp) {
        setDead(bp);
    }

    public Location getHome() {
        return home;
    }

    public short getColordata() {
        return color.getData();
    }
}
