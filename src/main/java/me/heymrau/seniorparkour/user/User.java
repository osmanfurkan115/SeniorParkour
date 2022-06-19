package me.heymrau.seniorparkour.user;

import lombok.Data;
import me.heymrau.seniorparkour.parkour.Parkour;
import me.heymrau.seniorparkour.parkour.record.ParkourCompletionRecord;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class User {

    private final UUID uuid;
    private final List<ParkourCompletionRecord> parkourCompletionRecords = new ArrayList<>();

    public User(UUID uuid, List<ParkourCompletionRecord> parkourCompletionRecords) {
        this.uuid = uuid;
        this.parkourCompletionRecords.addAll(parkourCompletionRecords);
    }

    public int getBestTime(Parkour parkour) {
        return parkourCompletionRecords.stream()
                .filter(record -> record.parkour().equals(parkour))
                .mapToInt(ParkourCompletionRecord::getAsSeconds)
                .min()
                .orElse(0);
    }

    public int getBestTime() {
        return parkourCompletionRecords.stream()
                .mapToInt(ParkourCompletionRecord::getAsSeconds)
                .min()
                .orElse(0);
    }

    public UUID getUUID() {
        return uuid;
    }

    @Nullable
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
