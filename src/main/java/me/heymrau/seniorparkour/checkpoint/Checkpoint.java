package me.heymrau.seniorparkour.checkpoint;

import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

public record Checkpoint(String name, Block block) {

    public void deleteHolograms() {
        block.getWorld().getNearbyEntities(block.getLocation(), 3, 3, 3).forEach(entity -> {
            if (entity instanceof ArmorStand) entity.remove();
        });
    }
}
