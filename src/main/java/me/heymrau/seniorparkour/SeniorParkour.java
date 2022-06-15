package me.heymrau.seniorparkour;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SeniorParkour extends JavaPlugin {

    private static SeniorParkour instance;

    @Override
    public void onEnable() {
        instance = this;

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
