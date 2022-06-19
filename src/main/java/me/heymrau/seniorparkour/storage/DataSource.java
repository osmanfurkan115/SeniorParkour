package me.heymrau.seniorparkour.storage;

public enum DataSource {
    MySQL;

    public static DataSource getDataSource(String name) {
        for (DataSource dataSource : values()) {
            if (dataSource.name().equalsIgnoreCase(name)) {
                return dataSource;
            }
        }
        throw new IllegalArgumentException("Invalid data source: " + name);
    }
}
