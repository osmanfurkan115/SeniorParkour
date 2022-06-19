package me.heymrau.seniorparkour.checkpoint;

import me.heymrau.seniorparkour.loader.Loader;
import me.heymrau.seniorparkour.util.LocationUtil;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

public class CheckpointLoader implements Loader<ConfigurationSection, Checkpoint> {

    @Override
    public Checkpoint loadOne(ConfigurationSection source) {
        String name = source.getName();
        Block block = LocationUtil.getBlock(source.getString("location"));

        return new Checkpoint(name, block);
    }
}
