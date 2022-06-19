package me.heymrau.seniorparkour.parkour.entry;

import lombok.Getter;
import me.heymrau.seniorparkour.SeniorParkour;
import me.heymrau.seniorparkour.config.ConfigKeys.Messages;
import me.heymrau.seniorparkour.parkour.Parkour;
import me.heymrau.seniorparkour.parkour.record.ParkourCompletionRecord;
import me.heymrau.seniorparkour.user.User;
import org.bukkit.entity.Player;

import java.util.*;

public class ParkourEntryManager {

    @Getter
    private final List<ParkourEntry> parkourEntries = new ArrayList<>();

    private final SeniorParkour plugin = SeniorParkour.getInstance();

    public Optional<ParkourEntry> findByUUID(UUID uuid) {
        return parkourEntries.stream()
                .filter(parkourEntry -> parkourEntry.getUUID().equals(uuid))
                .findFirst();
    }

    public boolean isInParkour(Player player) {
        return findByUUID(player.getUniqueId()).isPresent();
    }

    public void startEntry(Player player, Parkour parkour) {
        if (findByUUID(player.getUniqueId()).isPresent()) return;
        parkourEntries.add(new ParkourEntry(player.getUniqueId(), parkour, System.currentTimeMillis()));
        Messages.PLAYER_STARTED_PARKOUR.send(player, Map.of("parkour", parkour.getName()));
    }

    public void endEntry(ParkourEntry parkourEntry) {
        User user = plugin.getUserManager().getUser(parkourEntry.getUUID());
        user.getParkourCompletionRecords().add(new ParkourCompletionRecord(parkourEntry.getParkour(), parkourEntry.getDuration()));

        parkourEntries.remove(parkourEntry);
        Messages.PLAYER_FINISHED_PARKOUR.send(user.getPlayer(), Map.of("parkour", parkourEntry.getParkour().getName()));
    }

    public void eliminateEntry(ParkourEntry parkourEntry) {
        parkourEntries.remove(parkourEntry);
        parkourEntry.teleportToStart();

        Messages.PLAYER_ELIMINATED_PARKOUR.send(parkourEntry.getPlayer(), Map.of("parkour", parkourEntry.getParkour().getName()));
    }
}
