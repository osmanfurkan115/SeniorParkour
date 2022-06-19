package me.heymrau.seniorparkour.util;

import me.heymrau.seniorparkour.SeniorParkour;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Yaml extends YamlConfiguration {
    private final File file;

    public Yaml(String fileName) {
        JavaPlugin plugin = SeniorParkour.getInstance();
        this.file = new File(plugin.getDataFolder() + File.separator + fileName);

        if (plugin.getResource(fileName) != null && !exists()) plugin.saveResource(fileName, false);

        createFile();
    }

    public Yaml(File file) {
        this.file = file;
        createFile();
    }

    public void createFile() {
        FileUtil.createIfAbsent(file);
        reload();
    }

    public void reload() {
        try {
            load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        if (file.exists()) {
            file.delete();
        }
    }

    public boolean exists() {
        return file.exists();
    }
}