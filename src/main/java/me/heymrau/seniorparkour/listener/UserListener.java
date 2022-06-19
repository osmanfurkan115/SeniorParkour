package me.heymrau.seniorparkour.listener;

import me.heymrau.seniorparkour.SeniorParkour;
import me.heymrau.seniorparkour.user.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UserListener implements Listener {

    private final UserManager userManager = SeniorParkour.getInstance().getUserManager();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        userManager.addIfAbsent(event.getPlayer());
    }
}
