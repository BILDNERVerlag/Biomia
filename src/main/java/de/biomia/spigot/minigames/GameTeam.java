package de.biomia.spigot.minigames;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.messages.manager.ActionBar;
import de.biomia.spigot.minigames.bedwars.lobby.TeamSwitcher;
import de.biomia.spigot.minigames.general.Dead;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameTeam {

    protected final GameMode mode;
    private final HashMap<BiomiaPlayer, Boolean> players;
    private final TeamColor color;
    private final Location home;

    protected GameTeam(TeamColor color, Location loc, GameMode mode) {
        this.players = new HashMap<>();
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
        if (mode.canStop()) {
            mode.stop();
        }
    }

    public void join(BiomiaPlayer bp) {
        if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.LOBBY) {
            bp.sendMessage("\u00A7cDas Spiel hat bereits begonnen!");
            return;
        }

        if (isFull()) {
            bp.sendMessage(getColorcode() + BedWarsMessages.teamFull);
            return;
        }

        GameTeam team = mode.getTeam(bp);
        if (team != null) {
            if (team.equals(this)) {
                bp.sendMessage(BedWarsMessages.alreadyInTeam);
                return;
            }
            team.leave(bp);
        }

        for (BiomiaPlayer pl : getPlayers()) {
            ActionBar.sendActionBar(BedWarsMessages.joinedTeam.replace("%p", getColorcode() + bp.getName()), pl.getPlayer());
        }

        bp.getPlayer().getInventory().setItem(4, ItemCreator.itemCreate(Material.WOOL, BedWarsItemNames.teamWaehlerItem, getColordata()));

        players.put(bp, true);

        TeamSwitcher.getTeamSwitcher(mode);
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
        players.remove(bp);
    }

    public Location getHome() {
        return home;
    }

    public short getColordata() {
        return color.getData();
    }
}
