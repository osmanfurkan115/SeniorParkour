package me.heymrau.seniorparkour.util;

import net.md_5.bungee.api.ChatColor;

public final class ColorUtil {
    private ColorUtil() {
    }

    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
