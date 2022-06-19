package me.heymrau.seniorparkour.storage.user;

import me.heymrau.seniorparkour.parkour.Parkour;
import me.heymrau.seniorparkour.storage.repository.Repository;
import me.heymrau.seniorparkour.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends Repository<User> {
    Optional<User> findByUUID(UUID uuid);

    List<User> findTopByParkour(Parkour parkour, int limit);
}
