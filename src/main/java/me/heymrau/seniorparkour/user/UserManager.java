package me.heymrau.seniorparkour.user;

import me.heymrau.seniorparkour.SeniorParkour;
import me.heymrau.seniorparkour.parkour.Parkour;
import me.heymrau.seniorparkour.storage.user.UserRepository;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserManager {

    private final List<User> users = new ArrayList<>();

    private final SeniorParkour plugin;

    public UserManager(SeniorParkour plugin) {
        this.plugin = plugin;
    }

    public void addIfAbsent(Player player) {
        if (users.stream().anyMatch(user -> user.getUUID().equals(player.getUniqueId()))) return;
        users.add(plugin.getStorageManager().getUserRepository().findByUUID(player.getUniqueId())
                .orElseGet(() -> createNewUser(player.getUniqueId())));
    }

    public User getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    public User getUser(UUID uuid) {
        return users.stream().filter(user -> user.getUUID().equals(uuid)).findFirst().orElseGet(() -> createNewUser(uuid));
    }

    public User createNewUser(UUID uuid) {
        User user = new User(uuid, new ArrayList<>());
        users.add(user);
        return user;
    }

    public List<User> findTopByParkour(Parkour parkour, int limit) {
        UserRepository userRepository = plugin.getStorageManager().getUserRepository();

        userRepository.saveAll(users);
        return userRepository.findTopByParkour(parkour, limit);
    }

    public void saveUsers() {
        plugin.getStorageManager().getUserRepository().saveAll(users);
    }
}
