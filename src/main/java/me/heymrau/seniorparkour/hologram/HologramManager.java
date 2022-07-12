package me.heymrau.seniorparkour.hologram;

import lombok.Getter;
import me.heymrau.seniorparkour.checkpoint.Checkpoint;
import me.heymrau.seniorparkour.hologram.line.HologramLine;
import me.heymrau.seniorparkour.parkour.Parkour;
import me.heymrau.seniorparkour.util.ReplacementUtil;
import me.heymrau.seniorparkour.util.Yaml;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class HologramManager {

    @Getter
    private final Map<String, Hologram> holograms = new HashMap<>();

    private final Yaml hologramsFile = new Yaml("holograms.yml");

    public Optional<Hologram> findById(String id) {
        return Optional.ofNullable(holograms.get(id));
    }

    public Optional<Hologram> findByBlock(Block block) {
        return holograms.values()
                .stream()
                .filter(hologram -> hologram.getLines().stream().anyMatch(line -> {
                    Location location = line.getHologramStand().getLocation();
                    return location.getBlockX() == block.getX() && location.getBlockZ() == block.getZ();
                }))
                .findFirst();
    }

    public void createStartHologram(Parkour parkour) {
        createHologram("start-hologram", parkour.getStartBlock().getLocation(), Map.of("parkour", parkour.getName()));
    }

    public void createEndHologram(Parkour parkour) {
        createHologram("start-hologram", parkour.getEndBlock().getLocation(), Map.of("parkour", parkour.getName()));
    }

    public void createCheckpointHologram(Parkour parkour, Checkpoint checkpoint) {
        createHologram("checkpoint-hologram", checkpoint.block().getLocation(), Map.of("parkour", parkour.getName(),
                "checkpoint", checkpoint.name()));
    }

    public Hologram createHologram(String configKey, Location location, Map<String, String> replacements) {
        ConfigurationSection hologramSection = hologramsFile.getConfigurationSection(configKey);
        int height = hologramSection.getInt("height");
        List<String> lines = ReplacementUtil.replaceValues(hologramSection.getStringList("lines"), replacements);
        Hologram hologram = new Hologram(UUID.randomUUID().toString(), location.add(0, height, 0));
        hologram.addLines(lines.stream().map(line -> new HologramLine(hologram, line)).toArray(HologramLine[]::new));

        this.holograms.put(hologram.getId(), hologram);
        return hologram;
    }
}
