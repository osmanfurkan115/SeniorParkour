package me.heymrau.seniorparkour.parkour.record;

import me.heymrau.seniorparkour.parkour.Parkour;

import java.time.Duration;

public record ParkourCompletionRecord(Parkour parkour, long completeTime) {
    public int getAsSeconds() {
        return (int) (completeTime / 1000);
    }

    public Duration getAsDuration() {
        return Duration.ofMillis(completeTime);
    }
}
