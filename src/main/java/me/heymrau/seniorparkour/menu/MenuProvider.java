package me.heymrau.seniorparkour.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MenuProvider {
    private static MenuProvider instance;
    private final Map<UUID, Menu> menuRegistry = new HashMap<>();

    private MenuProvider() {
    }

    public static MenuProvider getInstance() {
        if (instance == null) instance = new MenuProvider();
        return instance;
    }

    /**
     * @param uuid the uuid of the player
     * @param menu the menu to put in the registry
     */
    public void putMenu(UUID uuid, Menu menu) {
        menuRegistry.put(uuid, menu);
    }

    /**
     * @param uuid the uuid of the player
     */
    public void removePlayerMenu(UUID uuid) {
        menuRegistry.remove(uuid);
    }

    /**
     * @param uuid the uuid of the player
     * @return if the player is in a menu
     */
    public boolean containsPlayer(UUID uuid) {
        return menuRegistry.containsKey(uuid);
    }

    /**
     * @param uuid the uuid of the player
     * @return the menu of the player
     */
    public Menu getMenu(UUID uuid) {
        return menuRegistry.get(uuid);
    }
}
