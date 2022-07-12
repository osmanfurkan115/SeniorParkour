package me.heymrau.seniorparkour.storage;

import lombok.Getter;
import me.heymrau.seniorparkour.SeniorParkour;
import me.heymrau.seniorparkour.config.ConfigKeys.Settings;
import me.heymrau.seniorparkour.storage.sql.SQLCredentials;
import me.heymrau.seniorparkour.storage.sql.connection.MySQLConnection;
import me.heymrau.seniorparkour.storage.user.UserRepository;
import me.heymrau.seniorparkour.storage.user.sql.SQLUserRepository;

@Getter
public class StorageManager {

    private final UserRepository userRepository;

    public StorageManager(SeniorParkour plugin, DataSource dataSource) {
        switch (dataSource) {
            case MySQL -> { // Add other sql types here if you want to support them
                SQLCredentials credentials = SQLCredentials.builder()
                        .host(Settings.SQL_HOST.getValue())
                        .database(Settings.SQL_DATABASE.getValue())
                        .username(Settings.SQL_USERNAME.getValue())
                        .password(Settings.SQL_PASSWORD.getValue())
                        .build();
                this.userRepository = new SQLUserRepository(plugin, new MySQLConnection(credentials));
            }
            default -> throw new IllegalArgumentException("Unknown data source: " + dataSource);
        }
    }
}
