package com.elic0de.aooni.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class location {

    public static Location stringToLocation(final String location) {
        if (location != null) {
            final String[] locationParts = location.split(":");
            if (locationParts.length == 4) {
                return new Location(Bukkit.getWorld(locationParts[0]), Double.parseDouble(locationParts[1]), Double.parseDouble(locationParts[2]), Double.parseDouble(locationParts[3]));
            } else if (locationParts.length == 6) {
                return new Location(Bukkit.getWorld(locationParts[0]), Double.parseDouble(locationParts[1]), Double.parseDouble(locationParts[2]), Double.parseDouble(locationParts[3]), Float.parseFloat(locationParts[4]), Float.parseFloat(locationParts[5]));
            }
        }
        return null;
    }

    public static String locationToString(final Location location) {
        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }
}
