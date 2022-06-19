package me.heymrau.seniorparkour.parkour.entry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.heymrau.seniorparkour.checkpoint.Checkpoint;
import me.heymrau.seniorparkour.parkour.Parkour;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ParkourEntry {

    private final UUID uuid;
    private final Parkour parkour;
    private final List<Checkpoint> completedCheckpoints = new ArrayList<>();
    private final long startTime;

    public void addCheckpoint(Checkpoint checkpoint) {
        completedCheckpoints.add(checkpoint);
    }

    @Nullable
    public Checkpoint getLastCheckpoint() {
        if (completedCheckpoints.isEmpty()) return null;
        return completedCheckpoints.get(completedCheckpoints.size() - 1);
    }

    public long getDuration() {
        return System.currentTimeMillis() - startTime;
    }

    public UUID getUUID() {
        return uuid;
    }

    @Nullable
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void teleportToStart() {
        Player player = getPlayer();
        if (player == null) return;
        player.teleport(parkour.getStartLocation());
    }
}
