package de.biomia.spigot.tools;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class Teleporter {

    private static final TeleportListener listener = new TeleportListener();

    private final Location from;
    private final Location to;
    private final Location backTeleport;
    private final Destination dest;
    private boolean inverted;
    private final BiomiaServerType serverType;
    private final Location location;
    private final TeleportExecutor teleportExecutor;

    public Teleporter(Location from, Location to, Location backTeleport, BiomiaServerType serverType) {
        this.from = from;
        this.to = to;
        dest = Destination.SERVER_GROUP;
        this.serverType = serverType;
        this.backTeleport = backTeleport;
        listener.addTeleporter(this);
        this.location = null;
        teleportExecutor = null;
    }

    public Teleporter(Location from, Location to, Location destinationLocation) {
        this.from = from;
        this.to = to;
        dest = Destination.LOCATION;
        this.location = destinationLocation;
        listener.addTeleporter(this);
        this.backTeleport = null;
        this.serverType = null;
        teleportExecutor = null;
    }

    public Teleporter(Location from, Location to, TeleportExecutor teleportExecutor) {
        this.from = from;
        this.to = to;
        dest = Destination.EXECUTION;
        listener.addTeleporter(this);
        this.location = null;
        this.backTeleport = null;
        this.serverType = null;
        this.teleportExecutor = teleportExecutor;
    }

    public void setInverted() {
        this.inverted = true;
    }

    public boolean isInverted() {
        return inverted;
    }

    public Location getTo() {
        return to;
    }

    public Location getBackTeleport() {
        return backTeleport;
    }

    public Location getFrom() {
        return from;
    }

    public Destination getDestination() {
        return dest;
    }

    public BiomiaServerType getServerType() {
        return serverType;
    }

    public Location getLocation() {
        return location;
    }

    public TeleportExecutor getTeleportExecutor() {
        return teleportExecutor;
    }

    public enum Destination {
        SERVER_GROUP, LOCATION, EXECUTION
    }
}

class TeleportListener implements Listener {

    private final ArrayList<Teleporter> teleporters = new ArrayList<>();

    TeleportListener() {
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
    }

    public void addTeleporter(Teleporter teleporter) {
        teleporters.add(teleporter);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        teleporters.forEach(eachTeleporter -> {

            if (!e.getTo().getWorld().equals(eachTeleporter.getFrom().getWorld()))
                return;

            boolean isFromInside = isInside(e.getFrom(), eachTeleporter), isToInside = isInside(e.getTo(), eachTeleporter);

            if ((isToInside && !isFromInside && !eachTeleporter.isInverted()) || (!isToInside && isFromInside && eachTeleporter.isInverted())) {
                if (eachTeleporter.getDestination() == Teleporter.Destination.SERVER_GROUP) {
                    e.getPlayer().teleport(eachTeleporter.getBackTeleport());
                    PlayerToServerConnector.connectToRandom(e.getPlayer(), eachTeleporter.getServerType());
                } else if (eachTeleporter.getDestination() == Teleporter.Destination.LOCATION)
                    e.getPlayer().teleport(eachTeleporter.getLocation());
                else if (eachTeleporter.getDestination() == Teleporter.Destination.EXECUTION)
                    eachTeleporter.getTeleportExecutor().execute(Biomia.getBiomiaPlayer(e.getPlayer()));
            }
        });

    }

    public boolean isInside(Location loc, Teleporter teleporter) {
        return ((loc.getX() > teleporter.getFrom().getX() && loc.getX() < teleporter.getTo().getX()) &&
                (loc.getY() > teleporter.getFrom().getY() && loc.getY() < teleporter.getTo().getY()) &&
                (loc.getZ() > teleporter.getFrom().getZ() && loc.getZ() < teleporter.getTo().getZ()));
    }
}
