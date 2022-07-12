package me.heymrau.seniorparkour.listener;

import me.heymrau.seniorparkour.SeniorParkour;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ParkourEntryListener implements Listener {

    private final SeniorParkour plugin;

    public ParkourEntryListener(SeniorParkour plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPressurePlate(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) return;
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;
        plugin.getParkourManager().handlePressurePlate(event.getPlayer(), clickedBlock);
    }
}
