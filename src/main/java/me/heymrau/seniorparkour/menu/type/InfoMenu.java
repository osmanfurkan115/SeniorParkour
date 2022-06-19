package me.heymrau.seniorparkour.menu.type;

import me.heymrau.seniorparkour.checkpoint.Checkpoint;
import me.heymrau.seniorparkour.config.ConfigKeys;
import me.heymrau.seniorparkour.menu.Menu;
import me.heymrau.seniorparkour.menu.item.MenuItemBuilder;
import me.heymrau.seniorparkour.parkour.Parkour;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;

public class InfoMenu extends Menu {

    private final Parkour parkour;

    public InfoMenu(Parkour parkour) {
        super("menus/info.yml");
        this.parkour = parkour;
    }

    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    protected void setupMenu(Player player) {
        getItemsSection().getKeys(false).forEach(key -> {
            ConfigurationSection itemSection = getItemsSection().getConfigurationSection(key);
            switch (key) {
                case "start-location" -> MenuItemBuilder.from(itemSection).place(this, inventoryClickEvent -> {
                    player.teleport(parkour.getStartLocation());
                    player.closeInventory();
                });

                case "end-location" -> MenuItemBuilder.from(itemSection).place(this, inventoryClickEvent -> {
                    Location endLocation = parkour.getEndLocation();
                    player.closeInventory();
                    if (endLocation != null) {
                        player.teleport(endLocation);
                        return;
                    }
                    ConfigKeys.Messages.PARKOUR_END_NOT_SET.send(player);
                });

                case "checkpoints" -> {
                    int i = 0;
                    for (Checkpoint checkpoint : parkour.getCheckpoints()) {
                        MenuItemBuilder.from(itemSection, Map.of("checkpoint", checkpoint.name())).placeOne(this, i, inventoryClickEvent -> {
                            player.closeInventory();
                            player.teleport(checkpoint.block().getLocation().add(0, 1, 0));
                        });
                        i++;
                    }
                }

                default -> MenuItemBuilder.from(itemSection).place(this);
            }
        });
    }
}
