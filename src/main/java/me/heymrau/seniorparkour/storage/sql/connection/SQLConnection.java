package me.heymrau.seniorparkour.storage.sql.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.heymrau.seniorparkour.storage.sql.SQLCredentials;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public abstract class SQLConnection {

    private final SQLCredentials credentials;

    @Getter
    private final HikariDataSource dataSource = new HikariDataSource();

    public SQLConnection(SQLCredentials credentials) {
        this.credentials = credentials;
        configureDataSource();
    }

    private void configureDataSource() {
        dataSource.setUsername(credentials.getUsername());
        dataSource.setPassword(credentials.getPassword());
        dataSource.setDriverClassName(getDriver());
        dataSource.setJdbcUrl(getConnectionString());
        dataSource.setConnectionTimeout(TimeUnit.SECONDS.toMillis(10));
        dataSource.setMaxLifetime(TimeUnit.MINUTES.toMillis(30));
        dataSource.setMaximumPoolSize(50);
    }

    public abstract String getDriver();

    public abstract String getPort();

    public abstract String getBareName();

    public String getConnectionString() {
        return "jdbc:" + getBareName() + "://" + credentials.getHost() + ":" + getPort() + "/" + credentials.getDatabase();
    }

    public Statement createStatement() {
        try {
            return dataSource.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
