package me.heymrau.seniorparkour.menu.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@AllArgsConstructor(staticName = "of")
@Getter
public class MenuItem {
    private final ItemStack itemStack;
    private final Consumer<InventoryClickEvent> clickAction;

    public static MenuItem of(ItemStack itemStack) {
        return of(itemStack, (e) -> {});
    }

    @Override
    public MenuItem clone() {
        try {
            return (MenuItem) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
