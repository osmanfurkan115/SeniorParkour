package me.heymrau.seniorparkour.menu.type;

import me.heymrau.seniorparkour.SeniorParkour;
import me.heymrau.seniorparkour.menu.Menu;
import me.heymrau.seniorparkour.menu.item.MenuItemBuilder;
import me.heymrau.seniorparkour.parkour.Parkour;
import me.heymrau.seniorparkour.user.User;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;


public class StatsMenu extends Menu {

    private final SeniorParkour plugin;

    private final Player target;

    public StatsMenu(SeniorParkour plugin, Player target) {
        super("menus/stats.yml");
        this.plugin = plugin;
        this.target = target;
    }

    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    protected void setupMenu(Player player) {
        User user = plugin.getUserManager().getUser(target);

        getItemsSection().getKeys(false).forEach(key -> {
            ConfigurationSection itemSection = getItemsSection().getConfigurationSection(key);
            if (!key.equals("parkour-item")) {
                MenuItemBuilder.from(itemSection).place(this);
                return;
            }

            int i = 0;
            for (Parkour parkour : plugin.getParkourManager().getParkours()) {
                MenuItemBuilder.from(itemSection, Map.of(
                        "parkour", parkour.getName(),
                        "best_time", user.getBestTime(parkour) + ""
                )).placeOne(this, i);
                i++;
            }
        });
    }
}
