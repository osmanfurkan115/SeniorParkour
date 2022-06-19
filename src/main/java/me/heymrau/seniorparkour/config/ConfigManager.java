package me.heymrau.seniorparkour.config;

import me.heymrau.seniorparkour.config.node.NodeLoader;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ConfigManager {

    private final NodeLoader loader = new NodeLoader();

    private final Map<String, Config> configs = new HashMap<>();

    /**
     * Put config's nodes into a class's fields
     * @param config config to put nodes into fields
     * @param clazz class to put nodes into fields
     * @param prefix prefix to put nodes into fields
     * @param <V> type of the class
     */
    public <V> void putNodesToClass(Config config, Class<?> clazz, boolean prefix) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                ConfigKey<V> configKey = (ConfigKey<V>) field.get(null);
                ConfigurationNode node = config.getNode();
                if (prefix) node = node.getNode(config.getName().toLowerCase(Locale.ROOT));
                for (String path : configKey.getPath()) node = node.getNode(path);
                V value = (V) node.getValue();
                if (value == null) continue;
                configKey.setValue(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a config from a path and input stream
     * @param name name of the config
     * @param path path of the config
     * @param inputStream input stream of the config
     * @return config
     */
    public Config createConfig(String name, String path, InputStream inputStream) {
        return new Config(name, path, loader.loadNode(path, inputStream));
    }

    /**
     * Create a config from a path and input stream
     * @param name name of the config
     * @param path path of the config
     * @return config
     */
    @Nullable
    public Config createConfig(String name, String path) {
        return new Config(name, path, loader.loadNode(path));
    }

    /**
     * Get a config from a config type
     * @param name name of the config
     * @return config
     */
    @Nullable
    public Config getConfig(String name) {
        return configs.get(name.toLowerCase(Locale.ROOT));
    }

    /**
     * Get a config from a config type
     * @param configType config type
     * @return config
     */
    @Nullable
    public Config getConfig(ConfigType configType) {
        return configs.get(configType.name().toLowerCase(Locale.ROOT));
    }

    /**
     * Add a config to the map
     * @param name name of the config
     * @param config config
     */
    public void addConfig(String name, Config config) {
        configs.put(name.toLowerCase(Locale.ROOT), config);
    }

    /**
     * Add a config to the map
     * @param configType config type
     * @param config config
     */
    public void addConfig(ConfigType configType, Config config) {
        configs.put(configType.name().toLowerCase(Locale.ROOT), config);
    }

    /**
     * Remove a config from the map
     * @param name name of the config
     */
    public void removeConfig(String name) {
        configs.remove(name.toLowerCase(Locale.ROOT));
    }

    /**
     * Remove a config from the map
     * @param configType config type
     */
    public void removeConfig(ConfigType configType) {
        configs.remove(configType.name().toLowerCase(Locale.ROOT));
    }

    /**
     * Contains a config
     * @param name name of the config
     * @return true if the config exists
     */
    public boolean containsConfig(String name) {
        return configs.containsKey(name.toLowerCase(Locale.ROOT));
    }

    /**
     * Contains a config
     * @param configType config type
     * @return true if the config exists
     */
    public boolean containsConfig(ConfigType configType) {
        return configs.containsKey(configType.name().toLowerCase(Locale.ROOT));
    }

    /**
     * Reloads a config
     * @param config config to reload
     */
    public void reloadConfig(@NotNull Config config, @NotNull ConfigType configType) {
        addConfig(configType, config);
        putNodesToClass(config, configType.getClazz(), true);
    }
}
