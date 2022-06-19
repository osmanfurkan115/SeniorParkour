package me.heymrau.seniorparkour.hologram;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.heymrau.seniorparkour.SeniorParkour;
import me.heymrau.seniorparkour.hologram.line.HologramLine;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Data
@RequiredArgsConstructor
public class Hologram {

    private static final double LINE_DISTANCE = 0.25;

    private final String id;
    private final List<HologramLine> lines = new ArrayList<>();
    private final Location location;

    @Setter(AccessLevel.NONE)
    private Consumer<Player> onClick;

    private void updateLocation() {
        Location location = this.location.clone().add(0, (LINE_DISTANCE / 2) * (this.lines.size() + 2), 0);
        this.lines.forEach(line -> line.setLocation(location.subtract(0, LINE_DISTANCE, 0)));
    }

    public Hologram addLines(HologramLine... lines) {
        for (HologramLine line : lines) {
            addLine(line);
        }
        return this;
    }

    public Hologram addLine(HologramLine line) {
        this.lines.add(line);
        updateLocation();

        return this;
    }

    public Hologram onClick(Consumer<Player> onClick) {
        this.onClick = onClick;

        SeniorParkour.getInstance().getHologramManager().getHolograms().put(id, this);
        return this;
    }

    public void delete() {
        this.lines.forEach(HologramLine::delete);
    }
}
