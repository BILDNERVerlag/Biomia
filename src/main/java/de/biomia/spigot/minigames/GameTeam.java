package de.biomia.spigot.minigames;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.messages.MinigamesItemNames;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.messages.manager.ActionBar;
import de.biomia.spigot.minigames.general.Dead;
import de.biomia.spigot.minigames.general.Scoreboards;
import de.biomia.spigot.minigames.general.TeamSwitcher;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;

public class GameTeam {

    private final GameMode mode;
    private final HashMap<BiomiaPlayer, Boolean> players;
    private final TeamColor color;
    private final Location home;

    protected GameTeam(TeamColor color, GameMode mode) {
        this.players = new HashMap<>();
        this.color = color;
        this.home = mode.getConfig().getSpawnLocation(color, mode.getInstance());
        this.mode = mode;
        mode.registerTeam(this);
    }

    public boolean lives(BiomiaPlayer bp) {
        return players.get(bp);
    }

    public void setDead(BiomiaPlayer bp) {
        players.put(bp, false);
        Dead.setDead(bp);
        if (mode.canStop()) {
            mode.stop();
        }
    }

    public void join(BiomiaPlayer bp) {
        if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.LOBBY) {
            bp.sendMessage("\u00A7cDas Spiel hat bereits begonnen!");
            return;
        }

        if (isFull() && !bp.getTeam().equals(this)) {
            bp.sendMessage(getColorcode() + MinigamesMessages.teamFull);
            return;
        }

        GameTeam team = bp.getTeam();
        if (team != null) {
            if (team.equals(this)) {
                bp.sendMessage(MinigamesMessages.alreadyInTeam);
                return;
            }
            team.leave(bp);
        }
        bp.getPlayer().getInventory().setItem(4, ItemCreator.itemCreate(Material.WOOL, MinigamesItemNames.teamWaehlerItem, getColordata()));
        bp.setTeam(this);

        for (BiomiaPlayer pl : getPlayers()) {
            ActionBar.sendActionBar(MinigamesMessages.joinedTeam.replace("%p", getColorcode() + bp.getName()).replace("%t", getTeamname()), pl.getPlayer());
        }

        players.put(bp, true);

        if (!mode.getInstance().getType().isVersus()) {
            Scoreboards.lobbySB.getTeam(color.name()).addEntry(bp.getName());
            TeamSwitcher.getTeamSwitcher(mode);
        }
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
        return color.translate();
    }

    public boolean isFull() {
        return players.size() == mode.getInstance().getTeamSize();
    }

    public TeamColor getColor() {
        return color;
    }

    public void leave(BiomiaPlayer bp) {
        bp.setTeam(null);
        players.remove(bp);
        if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.LOBBY)
            if (mode.canStop()) {
                mode.stop();
            }
        if (!mode.getInstance().getType().isVersus())
            TeamSwitcher.getTeamSwitcher(mode);
    }

    public Location getHome() {
        return home;
    }

    public short getColordata() {
        return color.getData();
    }

    public GameMode getMode() {
        return mode;
    }
}
