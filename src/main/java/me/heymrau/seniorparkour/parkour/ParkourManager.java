package me.heymrau.seniorparkour.parkour;

import lombok.Getter;
import me.heymrau.seniorparkour.SeniorParkour;
import me.heymrau.seniorparkour.checkpoint.Checkpoint;
import me.heymrau.seniorparkour.config.ConfigKeys;
import me.heymrau.seniorparkour.parkour.entry.ParkourEntry;
import me.heymrau.seniorparkour.util.FileUtil;
import me.heymrau.seniorparkour.util.LocationUtil;
import me.heymrau.seniorparkour.util.Yaml;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class ParkourManager {

    private final SeniorParkour plugin = SeniorParkour.getInstance();

    @Getter
    private final List<Parkour> parkours = new ArrayList<>();

    public void loadParkours() {
        File parkoursDirectory = new File(plugin.getDataFolder(), "parkours");
        FileUtil.createIfAbsent(parkoursDirectory);

        List<Parkour> parkours = new ParkourLoader().loadAll(Arrays.asList(parkoursDirectory.listFiles()));
        this.parkours.addAll(parkours);
    }

    public void saveParkours() {
        File parkoursDirectory = new File(plugin.getDataFolder(), "parkours");
        FileUtil.deleteContents(parkoursDirectory);

        parkours.forEach(parkour -> {
            Yaml parkourFile = new Yaml(new File(plugin.getDataFolder(), "parkours" + File.separator + parkour.getName() + ".yml"));

            parkour.getCheckpoints().forEach(checkpoint ->
                    parkourFile.set("checkpoints." + checkpoint.name() + ".location", LocationUtil.serialize(checkpoint.block()
                            .getLocation())));

            parkourFile.set("start-location", LocationUtil.serialize(parkour.getStartBlock().getLocation()));
            if (parkour.getEndBlock() != null) {
                parkourFile.set("end-location", LocationUtil.serialize(parkour.getEndBlock().getLocation()));
            }

            parkourFile.save();
        });
    }

    public Optional<Parkour> findByName(String name) {
        return parkours.stream()
                .filter(parkour -> parkour.getName().equals(name))
                .findFirst();
    }

    @Nullable
    public Checkpoint getNextCheckpoint(@NotNull Parkour parkour, @Nullable Checkpoint currentCheckpoint) {
        if (currentCheckpoint == null) {
            return parkour.getCheckpoints().get(0);
        }

        int nextCheckpointIndex = parkour.getCheckpoints().indexOf(currentCheckpoint) + 1;

        if (nextCheckpointIndex >= parkour.getCheckpoints().size()) {
            return null;
        }
        return parkour.getCheckpoints().get(nextCheckpointIndex);
    }

    public void handlePressurePlate(Player player, Block clickedBlock) {
        parkours.forEach(parkour -> {
            if (parkour.getStartBlock().equals(clickedBlock)) {
                plugin.getParkourEntryManager().startEntry(player, parkour);
                return;
            }

            Optional<ParkourEntry> optionalParkourEntry = plugin.getParkourEntryManager().findByUUID(player.getUniqueId());
            if (optionalParkourEntry.isEmpty()) return;
            ParkourEntry parkourEntry = optionalParkourEntry.get();
            if (!parkourEntry.getParkour().equals(parkour)) return;

            if (parkour.getEndBlock() != null && parkour.getEndBlock().equals(clickedBlock)) {
                if (!new HashSet<>(parkourEntry.getCompletedCheckpoints()).containsAll(parkour.getCheckpoints())) {
                    ConfigKeys.Messages.PLAYER_NOT_REACHED_CHECKPOINT.send(player);
                    return;
                }
                plugin.getParkourEntryManager().endEntry(parkourEntry);
                return;
            }

            plugin.getCheckpointManager().findByBlock(clickedBlock).ifPresent(checkpoint -> {
                if (!parkour.getCheckpoints().contains(checkpoint)) return;
                Checkpoint nextCheckpoint = getNextCheckpoint(parkour, parkourEntry.getLastCheckpoint());
                if (nextCheckpoint == null || !nextCheckpoint.equals(checkpoint)) return;
                parkourEntry.addCheckpoint(checkpoint);
                ConfigKeys.Messages.PLAYER_REACHED_CHECKPOINT.send(player, Map.of("checkpoint", checkpoint.name()));
            });
        });
    }
}
