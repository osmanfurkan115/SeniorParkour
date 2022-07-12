package me.heymrau.seniorparkour.task;

import me.heymrau.seniorparkour.SeniorParkour;
import me.heymrau.seniorparkour.config.ConfigKeys;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSaveTask extends BukkitRunnable {

    private final SeniorParkour plugin;

    public AutoSaveTask(SeniorParkour plugin) {
        this.plugin = plugin;
    }

    public void start() {
        long savePeriod = ConfigKeys.Settings.SAVE_INTERVAL.getValue() * 20;
        runTaskTimer(plugin, savePeriod, savePeriod);
    }

    @Override
    public void run() {
        plugin.getParkourManager().saveParkours();
    }
}
