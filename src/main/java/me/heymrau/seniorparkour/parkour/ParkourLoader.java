package me.heymrau.seniorparkour.parkour;

import com.google.common.io.Files;
import me.heymrau.seniorparkour.checkpoint.Checkpoint;
import me.heymrau.seniorparkour.checkpoint.CheckpointLoader;
import me.heymrau.seniorparkour.loader.Loader;
import me.heymrau.seniorparkour.util.LocationUtil;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParkourLoader implements Loader<File, Parkour> {

    @Override
    public Parkour loadOne(File source) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(source);

        String name = Files.getNameWithoutExtension(source.getName());

        CheckpointLoader checkpointLoader = new CheckpointLoader();
        List<Checkpoint> checkpoints = config.isConfigurationSection("checkpoints")
                ? config.getConfigurationSection("checkpoints").getKeys(false).stream()
                .map(key -> checkpointLoader.loadOne(config.getConfigurationSection("checkpoints." + key)))
                .collect(Collectors.toList())
                : new ArrayList<>();

        Block startLocation = LocationUtil.getBlock(config.getString("start-location"));
        Block endLocation = config.isSet("end-location") ? LocationUtil.getBlock(config.getString("end-location")) : null;

        return new Parkour(name, checkpoints, startLocation, endLocation);
    }
}
