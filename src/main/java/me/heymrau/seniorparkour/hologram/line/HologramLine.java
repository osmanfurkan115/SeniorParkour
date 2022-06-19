package me.heymrau.seniorparkour.hologram.line;

import lombok.Getter;
import me.heymrau.seniorparkour.hologram.Hologram;
import me.heymrau.seniorparkour.util.ColorUtil;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class HologramLine {

    private final Hologram hologram;

    @Getter
    private final ArmorStand hologramStand;

    public HologramLine(Hologram hologram, String text) {
        this.hologram = hologram;
        this.hologramStand = setupArmorStand(ColorUtil.colorize(text));
    }

    private ArmorStand setupArmorStand(String customName) {
        ArmorStand armorStand = hologram.getLocation().getWorld().spawn(hologram.getLocation(), ArmorStand.class);

        armorStand.setPersistent(true);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setCustomName(customName);
        armorStand.setCustomNameVisible(true);
        armorStand.setMarker(true);
        armorStand.setArms(false);
        armorStand.setBasePlate(false);
        armorStand.setGravity(false);
        armorStand.setSmall(true);

        return armorStand;
    }

    public void setLocation(Location location) {
        hologramStand.teleport(location);
    }

    public void delete() {
        hologramStand.remove();
    }
}
