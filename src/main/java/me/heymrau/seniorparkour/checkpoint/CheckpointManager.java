package me.heymrau.seniorparkour.checkpoint;

import me.heymrau.seniorparkour.SeniorParkour;
import org.bukkit.block.Block;

import java.util.List;
import java.util.Optional;

public class CheckpointManager {

    private final SeniorParkour plugin;

    public CheckpointManager(SeniorParkour plugin) {
        this.plugin = plugin;
    }

    public List<Checkpoint> getCheckpoints() {
        return plugin.getParkourManager().getParkours().stream()
                .flatMap(parkour -> parkour.getCheckpoints().stream())
                .toList();
    }

    public Optional<Checkpoint> findByName(String name) {
        return getCheckpoints().stream()
                .filter(checkpoint -> checkpoint.name().equals(name))
                .findFirst();
    }

    public Optional<Checkpoint> findByBlock(Block block) {
        return getCheckpoints().stream()
                .filter(checkpoint -> checkpoint.block().equals(block))
                .findFirst();
    }
}
