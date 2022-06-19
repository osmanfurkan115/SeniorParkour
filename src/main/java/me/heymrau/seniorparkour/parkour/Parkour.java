package me.heymrau.seniorparkour.parkour;

import lombok.Data;
import me.heymrau.seniorparkour.checkpoint.Checkpoint;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Data
public class Parkour {
    private final String name;
    private final List<Checkpoint> checkpoints = new ArrayList<>();

    private final Block startBlock;
    private Block endBlock;

    public Parkour(String name, Block startBlock) {
        this.name = name;
        this.startBlock = startBlock;
    }

    public Parkour(String name, List<Checkpoint> checkpoints, Block startBlock, Block endBlock) {
        this.name = name;
        this.checkpoints.addAll(checkpoints);
        this.startBlock = startBlock;
        this.endBlock = endBlock;
    }


    public Location getStartLocation() {
        return startBlock.getLocation().add(0, 1, 0);
    }

    @Nullable
    public Location getEndLocation() {
        if (endBlock == null) {
            return null;
        }
        return endBlock.getLocation().add(0, 1, 0);
    }

    public void deleteHolograms() {
        startBlock.getWorld().getNearbyEntities(startBlock.getLocation(), 3, 3, 3).forEach(entity -> {
            if (entity instanceof ArmorStand) entity.remove();
        });

        checkpoints.forEach(Checkpoint::deleteHolograms);

        if (endBlock == null) return;
        endBlock.getWorld().getNearbyEntities(endBlock.getLocation(), 3, 3, 3).forEach(entity -> {
            if (entity instanceof ArmorStand) entity.remove();
        });
    }
}
