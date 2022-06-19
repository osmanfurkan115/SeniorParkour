package me.heymrau.seniorparkour.menu;

import lombok.Getter;
import me.heymrau.seniorparkour.menu.item.MenuItem;
import me.heymrau.seniorparkour.util.ColorUtil;
import me.heymrau.seniorparkour.util.Yaml;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu {
    @Getter
    private final Inventory inventory;
    private final Map<Integer, MenuItem> menuItems = new HashMap<>();

    private final MenuProvider menuProvider = MenuProvider.getInstance();

    private final Yaml yaml;

    public Menu(String menuPath) {
        this.yaml = new Yaml(menuPath);
        this.inventory = Bukkit.createInventory(null, getSize(), getTitle());
    }

    public MenuItem getItem(int slot) {
        return menuItems.get(slot);
    }

    public void setItem(int slot, @NotNull MenuItem item) {
        inventory.setItem(slot, item.getItemStack());
        menuItems.put(slot, item);
    }

    public void addItem(@NotNull MenuItem menuItem) {
        setItem(inventory.firstEmpty(), menuItem);
    }

    public void fill(@NotNull MenuItem item) {
        for (int i = 0; i < inventory.getSize(); i++) {
            setItem(i, item);
        }
    }

    public void open(Player player) {
        setupMenu(player);
        player.openInventory(inventory);
        menuProvider.putMenu(player.getUniqueId(), this);
    }

    public String getTitle() {
        return ColorUtil.colorize(yaml.getString("menu.title"));
    }

    public int getSize() {
        return yaml.getInt("menu.size");
    }

    public ConfigurationSection getItemsSection() {
        return yaml.getConfigurationSection("menu.items");
    }

    public abstract boolean isCloseable();

    protected abstract void setupMenu(Player player);
}
