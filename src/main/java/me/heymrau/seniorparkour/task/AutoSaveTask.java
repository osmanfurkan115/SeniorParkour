package me.heymrau.seniorparkour.task;

import me.heymrau.seniorparkour.SeniorParkour;
import me.heymrau.seniorparkour.config.ConfigKeys;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSaveTask extends BukkitRunnable {

    public void start() {
        long savePeriod = ConfigKeys.Settings.SAVE_INTERVAL.getValue() * 20;
        runTaskTimer(SeniorParkour.getInstance(), savePeriod, savePeriod);
    }

    @Override
    public void run() {
        SeniorParkour.getInstance().getParkourManager().saveParkours();
    }
}
