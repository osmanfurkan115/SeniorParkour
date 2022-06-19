package me.heymrau.seniorparkour.config;

import me.heymrau.seniorparkour.util.ColorUtil;
import me.heymrau.seniorparkour.util.ReplacementUtil;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ConfigKey<V> {

    private final ConfigType configType;
    private final String[] path;
    private V value;

    public ConfigKey(V value, ConfigType configType, String... path) {
        this.value = value;
        this.configType = configType;
        this.path = path;
    }

    public ConfigKey(ConfigType configType, String... path) {
        this.configType = configType;
        this.path = path;
    }

    public ConfigType getConfigType() {
        return configType;
    }

    public String[] getPath() {
        return path;
    }

    public V getValue() {
        if (value instanceof String) return (V) ColorUtil.colorize(value.toString());
        return value;
    }

    public V getReplacedValue(Map<String, String> replacements) {
        if (value instanceof String) {
            return (V) ColorUtil.colorize(ReplacementUtil.replaceValues(value.toString(), replacements));
        }
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public void send(Player player) {
        player.sendMessage(getReplacedValue(Map.of("player", player.getName())).toString());
    }

    public void send(Player player, Map<String, String> replacements) {
        Map<String, String> safeReplacements = new HashMap<>(replacements);
        safeReplacements.put("player", player.getName());
        player.sendMessage(getReplacedValue(safeReplacements).toString());
    }
}
