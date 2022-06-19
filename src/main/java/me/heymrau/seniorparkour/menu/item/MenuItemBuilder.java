package me.heymrau.seniorparkour.menu.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.heymrau.seniorparkour.menu.Menu;
import me.heymrau.seniorparkour.util.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Getter
public class MenuItemBuilder {
    private final List<Integer> slots;
    private final ItemStack itemStack;

    public static MenuItemBuilder from(ConfigurationSection section) {
        return new MenuItemBuilder(section.isList("slot")
                ? section.getIntegerList("slot")
                : List.of(section.getInt("slot")), ItemBuilder.from(section).build());
    }

    public static MenuItemBuilder from(ConfigurationSection section, Map<String, String> replacements) {
        return new MenuItemBuilder(section.isList("slot")
                ? section.getIntegerList("slot")
                : List.of(section.getInt("slot")), ItemBuilder.from(section).replaceValues(replacements).build());
    }

    public static MenuItemBuilder from(ConfigurationSection section, Map<String, String> replacements, ItemStack item) {
        return new MenuItemBuilder(section.isList("slot")
                ? section.getIntegerList("slot")
                : List.of(section.getInt("slot")), ItemBuilder.of(item).replaceValues(replacements).build());
    }

    public void placeOne(Menu menu, int slotIndex) {
        menu.setItem(slots.get(slotIndex), MenuItem.of(itemStack));
    }

    public void placeOne(Menu menu, int slotIndex, Consumer<InventoryClickEvent> clickAction) {
        menu.setItem(slots.get(slotIndex), MenuItem.of(itemStack, clickAction));
    }

    public void place(Menu menu) {
        slots.forEach(slot -> menu.setItem(slot, MenuItem.of(itemStack)));
    }

    public void place(Menu menu, Consumer<InventoryClickEvent> clickAction) {
        slots.forEach(slot -> menu.setItem(slot, MenuItem.of(itemStack, clickAction)));
    }
}
