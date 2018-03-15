package de.biomia.spigot.tools;

import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class Teleporter {

    private static final TeleportListener listener;

    static {
        listener = new TeleportListener();
    }

    private final Location from;
    private final Location to;
    private final Location backTeleport;
    private final Destination dest;
    private boolean inverted;
    private final BiomiaServerType serverType;
    private final Location location;

    public Teleporter(Location from, Location to, Location backTeleport, BiomiaServerType serverType) {
        this.from = from;
        this.to = to;
        dest = Destination.SERVER_GROUP;
        this.serverType = serverType;
        this.backTeleport = backTeleport;
        listener.addTeleporter(this);
        this.location = null;
    }

    public Teleporter(Location from, Location to, Location destinationLocation) {
        this.from = from;
        this.to = to;
        dest = Destination.LOCATION;
        this.location = destinationLocation;
        listener.addTeleporter(this);
        this.backTeleport = null;
        this.serverType = null;
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

    public enum Destination {
        SERVER_GROUP, LOCATION
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

            if ((from.getX() <= x) && (x <= to.getX()) && (from.getY() <= y) && (y <= to.getY()) && (from.getZ() <= z) && (z <= to.getZ())) {

                if (!e.getTo().getWorld().equals(eachTeleporter.getFrom().getWorld()) || eachTeleporter.isInverted())
                    return;

                if (eachTeleporter.getDestination() == Teleporter.Destination.SERVER_GROUP) {
                    e.getPlayer().teleport(eachTeleporter.getBackTeleport());
                    PlayerToServerConnector.connectToRandom(e.getPlayer(), eachTeleporter.getServerType());
                } else {
                    e.getPlayer().teleport(eachTeleporter.getLocation());
                }

            } else if (e.getTo().getWorld().equals(eachTeleporter.getFrom().getWorld()) && eachTeleporter.isInverted()) {
                e.getPlayer().teleport(eachTeleporter.getLocation());
            }
        });

    }
}
