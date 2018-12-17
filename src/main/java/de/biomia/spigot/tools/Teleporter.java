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

        double x1, x2, y1, y2, z1, z2;

        if (from.getX() < to.getX()) {
            x1 = from.getX();
            x2 = to.getX();
        } else {
            x1 = to.getX();
            x2 = from.getX();
        }
        if (from.getY() < to.getY()) {
            y1 = from.getY();
            y2 = to.getY();
        } else {
            y1 = to.getY();
            y2 = from.getY();
        }
        if (from.getZ() < to.getZ()) {
            z1 = from.getZ();
            z2 = to.getZ();
        } else {
            z1 = to.getZ();
            z2 = from.getZ();
        }

        this.from = new Location(from.getWorld(), x1, y1, z1);
        this.to = new Location(from.getWorld(), x2, y2, z2);
        dest = Destination.SERVER_GROUP;
        this.serverType = serverType;
        this.backTeleport = backTeleport;
        listener.addTeleporter(this);
        this.location = null;
        teleportExecutor = null;
    }

    public Teleporter(Location from, Location to, Location destinationLocation) {
        double x1, x2, y1, y2, z1, z2;

        if (from.getX() < to.getX()) {
            x1 = from.getX();
            x2 = to.getX();
        } else {
            x1 = to.getX();
            x2 = from.getX();
        }
        if (from.getY() < to.getY()) {
            y1 = from.getY();
            y2 = to.getY();
        } else {
            y1 = to.getY();
            y2 = from.getY();
        }
        if (from.getZ() < to.getZ()) {
            z1 = from.getZ();
            z2 = to.getZ();
        } else {
            z1 = to.getZ();
            z2 = from.getZ();
        }
        this.from = new Location(from.getWorld(), x1, y1, z1);
        this.to = new Location(from.getWorld(), x2, y2, z2);
        dest = Destination.LOCATION;
        this.location = destinationLocation;
        listener.addTeleporter(this);
        this.backTeleport = null;
        this.serverType = null;
        teleportExecutor = null;
    }

    public Teleporter(Location from, Location to, TeleportExecutor teleportExecutor) {
        double x1, x2, y1, y2, z1, z2;

        if (from.getX() < to.getX()) {
            x1 = from.getX();
            x2 = to.getX();
        } else {
            x1 = to.getX();
            x2 = from.getX();
        }
        if (from.getY() < to.getY()) {
            y1 = from.getY();
            y2 = to.getY();
        } else {
            y1 = to.getY();
            y2 = from.getY();
        }
        if (from.getZ() < to.getZ()) {
            z1 = from.getZ();
            z2 = to.getZ();
        } else {
            z1 = to.getZ();
            z2 = from.getZ();
        }

        this.from = new Location(from.getWorld(), x1, y1, z1);
        this.to = new Location(from.getWorld(), x2, y2, z2);
        dest = Destination.EXECUTION;
        listener.addTeleporter(this);
        this.location = null;
        this.backTeleport = null;
        this.serverType = null;
        this.teleportExecutor = teleportExecutor;
    }

    public Teleporter setInverted() {
        this.inverted = true;
        return this;
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

    public static void removeTeleporter(Teleporter teleporter) {
        TeleportListener.teleporters.remove(teleporter);
    }
}

class TeleportListener implements Listener {

    static final ArrayList<Teleporter> teleporters = new ArrayList<>();

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
                switch (eachTeleporter.getDestination()) {
                    case SERVER_GROUP:
                        e.getPlayer().teleport(eachTeleporter.getBackTeleport());
                        PlayerToServerConnector.connectToRandom(e.getPlayer(), eachTeleporter.getServerType());
                        break;
                    case LOCATION:
                        e.getPlayer().teleport(eachTeleporter.getLocation());
                        break;
                    case EXECUTION:
                        eachTeleporter.getTeleportExecutor().execute(Biomia.getBiomiaPlayer(e.getPlayer()));
                        break;
                }
            }
        });

    }

    private boolean isInside(Location loc, Teleporter teleporter) {
        return ((loc.getX() >= teleporter.getFrom().getX() && loc.getX() <= teleporter.getTo().getX()) &&
                (loc.getY() >= teleporter.getFrom().getY() && loc.getY() <= teleporter.getTo().getY()) &&
                (loc.getZ() >= teleporter.getFrom().getZ() && loc.getZ() <= teleporter.getTo().getZ()));
    }
}
