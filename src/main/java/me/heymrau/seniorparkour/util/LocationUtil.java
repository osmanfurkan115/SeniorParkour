package me.heymrau.seniorparkour.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

public final class LocationUtil {
    private LocationUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String serialize(Location location) {
        return location.getWorld().getName() + "," +
                location.getX() + "," +
                location.getY() + "," +
                location.getZ() + "," +
                location.getYaw() + "," +
                location.getPitch();
    }

    public static Location deserialize(String serializedLocation) {
        String[] parts = serializedLocation.split(",");
        return new Location(
                Bukkit.getWorld(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]),
                Float.parseFloat(parts[4]),
                Float.parseFloat(parts[5])
        );
    }

    public static Block getBlock(String serializedLocation) {
        return deserialize(serializedLocation).getBlock();
    }
}
