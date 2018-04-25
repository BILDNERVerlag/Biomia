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

            Location from = eachTeleporter.getFrom();
            Location to = eachTeleporter.getTo();

            double x = e.getTo().getX();
            double y = e.getTo().getY();
            double z = e.getTo().getZ();

            double x2 = e.getFrom().getX();
            double y2 = e.getFrom().getY();
            double z2 = e.getFrom().getZ();

            if ((from.getX() <= x) && (x <= to.getX()) && (from.getY() <= y) && (y <= to.getY()) && (from.getZ() <= z) && (z <= to.getZ())) {
                if (!e.getTo().getWorld().equals(eachTeleporter.getFrom().getWorld()) || eachTeleporter.isInverted())
                    return;
                if ((from.getX() > x2) || (x2 > to.getX()) || (from.getY() > y2) || (y2 > to.getY()) || (from.getZ() > z2) || (z2 > to.getZ()))
                    if (eachTeleporter.getDestination() == Teleporter.Destination.SERVER_GROUP) {
                    e.getPlayer().teleport(eachTeleporter.getBackTeleport());
                    PlayerToServerConnector.connectToRandom(e.getPlayer(), eachTeleporter.getServerType());
                    } else if (eachTeleporter.getDestination() == Teleporter.Destination.LOCATION)
                        e.getPlayer().teleport(eachTeleporter.getLocation());
                    else
                        eachTeleporter.getTeleportExecutor().execute(Biomia.getBiomiaPlayer(e.getPlayer()));
            } else if ((from.getX() > x) && (x > to.getX()) && (from.getY() > y) && (y > to.getY()) && (from.getZ() > z) && (z > to.getZ()) && e.getTo().getWorld().equals(eachTeleporter.getFrom().getWorld()) && eachTeleporter.isInverted())
                e.getPlayer().teleport(eachTeleporter.getLocation());
        });

    }
}
