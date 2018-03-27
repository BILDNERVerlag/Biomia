package de.biomia.spigot.commands.general;

import org.bukkit.Bukkit;
import org.bukkit.Location;

class WarpLocation {

    private final String groupname;
    private final Location loc;

    WarpLocation(double x, double y, double z, float yaw, float pitch, String groupname, String worldname) {
        loc = new Location(Bukkit.getWorld(worldname), x, y, z, yaw, pitch);
        this.groupname = groupname;
    }

    WarpLocation(Location loc0, String groupname) {
        loc = loc0;
        this.groupname = groupname;
    }

    public Location getLocation() {
        return loc;
    }

    public String toString() {
        return loc.serialize().toString();
    }

    public String getWorldname() {
        return loc.getWorld().getName();
    }

    public String getGroupname() {
        return groupname;
    }

}