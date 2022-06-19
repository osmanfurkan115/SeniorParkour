package me.heymrau.seniorparkour.menu.type;

import me.heymrau.seniorparkour.SeniorParkour;
import me.heymrau.seniorparkour.menu.Menu;
import me.heymrau.seniorparkour.menu.item.MenuItemBuilder;
import me.heymrau.seniorparkour.menu.page.Page;
import me.heymrau.seniorparkour.parkour.Parkour;
import me.heymrau.seniorparkour.user.User;
import me.heymrau.seniorparkour.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;

public class TopMenu extends Menu {

    private final Parkour parkour;
    private Page<User> page;

    public TopMenu(Parkour parkour) {
        super("menus/top.yml");
        this.parkour = parkour;
        this.page = Page.of(SeniorParkour.getInstance().getUserManager().findTopByParkour(parkour, 9), 9);
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
                case "previous-page" -> MenuItemBuilder.from(itemSection).place(this, event -> {
                    this.page = page.previous();
                    open(player);
                });
                case "next-page" -> MenuItemBuilder.from(itemSection).place(this, event -> {
                    this.page = page.next();
                    open(player);
                });
                case "user" -> {
                    int i = 0;
                    for (User user : page.getContent()) {
                        MenuItemBuilder.from(itemSection, Map.of(
                                "player", Bukkit.getOfflinePlayer(user.getUUID()).getName(),
                                "time", user.getBestTime(parkour) + "",
                                "position", page.position(user) + ""
                        ), ItemBuilder.from(itemSection).setHead(user.getUUID()).build()).placeOne(this, i);
                        i++;
                    }
                }
                default -> MenuItemBuilder.from(itemSection).place(this);
            }
        });
    }
}
