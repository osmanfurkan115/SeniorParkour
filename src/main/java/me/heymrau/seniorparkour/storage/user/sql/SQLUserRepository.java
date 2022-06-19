package me.heymrau.seniorparkour.storage.user.sql;

import me.heymrau.seniorparkour.SeniorParkour;
import me.heymrau.seniorparkour.parkour.Parkour;
import me.heymrau.seniorparkour.parkour.ParkourManager;
import me.heymrau.seniorparkour.parkour.record.ParkourCompletionRecord;
import me.heymrau.seniorparkour.storage.sql.connection.SQLConnection;
import me.heymrau.seniorparkour.storage.user.UserRepository;
import me.heymrau.seniorparkour.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class SQLUserRepository implements UserRepository {

    private final SQLConnection connection;

    private final ParkourManager parkourManager = SeniorParkour.getInstance().getParkourManager();

    public SQLUserRepository(SQLConnection connection) {
        this.connection = connection;
        initializeTables();
    }

    private void initializeTables() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `users` (`uuid` VARCHAR(36) NOT NULL, PRIMARY KEY (`uuid`))");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `user_parkour_completion_records` (`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, `uuid` VARCHAR(36) NOT NULL, `parkour_name` VARCHAR(36) NOT NULL, `completion_time` INT NOT NULL, FOREIGN KEY (`uuid`) REFERENCES `users`(`uuid`))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Collection<User> getAll() {
        try (Statement statement = connection.createStatement()) {
            List<User> users = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                List<ParkourCompletionRecord> parkourCompletionRecords = getParkourCompletionRecords(uuid);
                users.add(new User(uuid, parkourCompletionRecords));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<ParkourCompletionRecord> getParkourCompletionRecords(UUID uuid) {
        List<ParkourCompletionRecord> parkourCompletionRecords = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user_parkour_completion_records WHERE uuid = '" + uuid.toString() + "'");
            while (resultSet.next()) {
                String name = resultSet.getString("parkour_name");
                int completionTime = resultSet.getInt("completion_time");
                if (completionTime == 0) continue;
                parkourManager.findByName(name).ifPresent(parkour -> parkourCompletionRecords.add(new ParkourCompletionRecord(parkour, completionTime * 1000L)));
            }
            return parkourCompletionRecords;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<User> findByUUID(UUID uuid) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE uuid = '" + uuid.toString() + "'");
            if (resultSet.next()) {
                List<ParkourCompletionRecord> parkourCompletionRecords = getParkourCompletionRecords(uuid);
                return Optional.of(new User(uuid, parkourCompletionRecords));
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<User> findTopByParkour(Parkour parkour, int limit) {
        try (Statement statement = connection.createStatement()) {
            List<User> users = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user_parkour_completion_records WHERE parkour_name = '" + parkour.getName() + "' ORDER BY completion_time ASC LIMIT " + limit);
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                List<ParkourCompletionRecord> parkourCompletionRecords = getParkourCompletionRecords(uuid);
                users.add(new User(uuid, parkourCompletionRecords));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void save(User data) {
        try (Statement statement = connection.createStatement()) {
            statement.addBatch("INSERT INTO `users` (`uuid`) VALUES ('" + data.getUUID().toString() + "') ON DUPLICATE KEY UPDATE uuid = '" + data.getUUID().toString() + "'");
            for (ParkourCompletionRecord parkourCompletionRecord : data.getParkourCompletionRecords()) {
                statement.addBatch("INSERT INTO `user_parkour_completion_records` (`uuid`, `parkour_name`, `completion_time`) VALUES ('" + data.getUUID().toString() + "', '" + parkourCompletionRecord.parkour().getName() + "', " + parkourCompletionRecord.getAsSeconds() + ")");
            }

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveAll(List<User> data) {
        try (Statement statement = connection.createStatement()) {
            for (User user : data) {
                statement.addBatch("INSERT INTO `users` (`uuid`) VALUES ('" + user.getUUID().toString() + "') ON DUPLICATE KEY UPDATE uuid = '" + user.getUUID().toString() + "'");
                for (ParkourCompletionRecord parkourCompletionRecord : user.getParkourCompletionRecords()) {
                    if (parkourCompletionRecord.completeTime() == 0) continue;
                    statement.addBatch("INSERT INTO `user_parkour_completion_records` (`uuid`, `parkour_name`, `completion_time`) VALUES ('" + user.getUUID().toString() + "', '" + parkourCompletionRecord.parkour().getName() + "', " + parkourCompletionRecord.getAsSeconds() + ")");
                }
            }

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
