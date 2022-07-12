package me.heymrau.seniorparkour.listener;

import me.heymrau.seniorparkour.SeniorParkour;
import me.heymrau.seniorparkour.hologram.Hologram;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Optional;

public class HologramListener implements Listener {

    private final SeniorParkour plugin;

    public HologramListener(SeniorParkour plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getHand() != null && event.getHand() == EquipmentSlot.OFF_HAND) return;
        for (Entity entity: event.getPlayer().getNearbyEntities(2, 2, 2)) {
            if (!isLookingAt(event.getPlayer(), entity)) continue;
            if (!(entity instanceof ArmorStand armorStand)) continue;

            Optional<Hologram> hologramByBlock = plugin.getHologramManager().findByBlock(armorStand.getLocation().getBlock());
            if (hologramByBlock.isPresent()) {
                hologramByBlock.get().getOnClick().accept(event.getPlayer());
                break;
            }
        }
    }

    private boolean isLookingAt(Player player, Entity entity) {
        return player.getLocation().getDirection().normalize().dot(entity.getLocation().getDirection().normalize()) > 0.7;
    }
}
