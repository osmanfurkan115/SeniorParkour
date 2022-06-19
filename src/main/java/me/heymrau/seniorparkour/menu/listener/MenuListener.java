package me.heymrau.seniorparkour.menu.listener;

import me.heymrau.seniorparkour.menu.Menu;
import me.heymrau.seniorparkour.menu.MenuProvider;
import me.heymrau.seniorparkour.menu.item.MenuItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class MenuListener implements Listener {
    private final MenuProvider menuProvider = MenuProvider.getInstance();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity whoClicked = event.getWhoClicked();

        if (event.getClick().equals(ClickType.UNKNOWN)) return;
        if (event.getClickedInventory() == null || event.getClickedInventory().equals(whoClicked.getInventory())) return;
        if (!isOnMenu(whoClicked.getUniqueId())) return;
        event.setCancelled(true);

        Menu menu = menuProvider.getMenu(whoClicked.getUniqueId());
        MenuItem item = menu.getItem(event.getSlot());

        if (item == null) return;
        item.getClickAction().accept(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (!isOnMenu(player.getUniqueId())) return;

        Menu menu = menuProvider.getMenu(player.getUniqueId());
        if (menu.isCloseable()) {
            menuProvider.removePlayerMenu(event.getPlayer().getUniqueId());
        } else {
            menu.open(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        menuProvider.removePlayerMenu(e.getPlayer().getUniqueId());
    }

    private boolean isOnMenu(UUID uuid) {
        return menuProvider.containsPlayer(uuid);
    }
}
