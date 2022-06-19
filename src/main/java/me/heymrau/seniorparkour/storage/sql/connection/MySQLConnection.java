package me.heymrau.seniorparkour.storage.sql.connection;

import me.heymrau.seniorparkour.storage.sql.SQLCredentials;

public class MySQLConnection extends SQLConnection {

    public MySQLConnection(SQLCredentials credentials) {
        super(credentials);
    }

    @Override
    public String getDriver() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    public String getPort() {
        return "3306";
    }

    @Override
    public String getBareName() {
        return "mysql";
    }
}
