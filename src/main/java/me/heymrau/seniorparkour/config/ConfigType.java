package me.heymrau.seniorparkour.config;

public enum ConfigType {
    SETTINGS(ConfigKeys.Settings.class), MESSAGES(ConfigKeys.Messages.class);
    private final Class<?> clazz;

    ConfigType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
