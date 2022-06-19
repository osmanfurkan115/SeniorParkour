package me.heymrau.seniorparkour.listener;

import me.heymrau.seniorparkour.SeniorParkour;
import me.heymrau.seniorparkour.parkour.entry.ParkourEntryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class ParkourProtectionListener implements Listener {

    private final ParkourEntryManager parkourEntryManager = SeniorParkour.getInstance().getParkourEntryManager();

    @EventHandler
    public void onFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        parkourEntryManager.findByUUID(player.getUniqueId()).ifPresent(parkourEntryManager::eliminateEntry);
    }
}
