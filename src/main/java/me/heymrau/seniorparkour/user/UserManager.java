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

    private final UserRepository userRepository = SeniorParkour.getInstance().getStorageManager().getUserRepository();

    public void addIfAbsent(Player player) {
        if (users.stream().anyMatch(user -> user.getUUID().equals(player.getUniqueId()))) return;
        users.add(userRepository.findByUUID(player.getUniqueId()).orElseGet(() -> createNewUser(player.getUniqueId())));
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
        return userRepository.findTopByParkour(parkour, limit);
    }

    public void saveUsers() {
        userRepository.saveAll(users);
    }
}
