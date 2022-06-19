package me.heymrau.seniorparkour.config;

public final class ConfigKeys {
    private ConfigKeys() {}

    public static final class Messages {

        private Messages() {}

        public static final ConfigKey<String> PARKOUR_CREATED = new ConfigKey<>(ConfigType.MESSAGES, "parkour", "created");
        public static final ConfigKey<String> PARKOUR_SET_END_LOCATION = new ConfigKey<>(ConfigType.MESSAGES, "parkour", "set-end");
        public static final ConfigKey<String> PARKOUR_DELETED = new ConfigKey<>(ConfigType.MESSAGES, "parkour", "deleted");
        public static final ConfigKey<String> PARKOUR_TELEPORT = new ConfigKey<>(ConfigType.MESSAGES, "parkour", "teleport");
        public static final ConfigKey<String> PARKOUR_END_NOT_SET = new ConfigKey<>(ConfigType.MESSAGES, "parkour", "end-not-set");

        public static final ConfigKey<String> CHECKPOINT_CREATED = new ConfigKey<>(ConfigType.MESSAGES, "checkpoint", "created");
        public static final ConfigKey<String> CHECKPOINT_DELETED = new ConfigKey<>(ConfigType.MESSAGES, "checkpoint", "deleted");
        public static final ConfigKey<String> CHECKPOINT_TELEPORT = new ConfigKey<>(ConfigType.MESSAGES, "checkpoint", "teleport");

        public static final ConfigKey<String> PLAYER_STARTED_PARKOUR = new ConfigKey<>(ConfigType.MESSAGES, "player", "started-parkour");
        public static final ConfigKey<String> PLAYER_FINISHED_PARKOUR = new ConfigKey<>(ConfigType.MESSAGES, "player", "finished-parkour");
        public static final ConfigKey<String> PLAYER_ELIMINATED_PARKOUR = new ConfigKey<>(ConfigType.MESSAGES, "player", "eliminated");
        public static final ConfigKey<String> PLAYER_REACHED_CHECKPOINT = new ConfigKey<>(ConfigType.MESSAGES, "player", "reached-checkpoint");
        public static final ConfigKey<String> PLAYER_NOT_REACHED_CHECKPOINT = new ConfigKey<>(ConfigType.MESSAGES, "player", "not-reached-checkpoint");
    }

    public static final class Settings {

        private Settings() {}

        public static final ConfigKey<String> STORAGE_TYPE = new ConfigKey<>("MySQL", ConfigType.SETTINGS, "storage", "type");

        public static final ConfigKey<String> SQL_HOST = new ConfigKey<>("localhost", ConfigType.SETTINGS, "storage", "host");
        public static final ConfigKey<String> SQL_DATABASE = new ConfigKey<>("parkours", ConfigType.SETTINGS, "storage", "database");
        public static final ConfigKey<String> SQL_USERNAME = new ConfigKey<>("root", ConfigType.SETTINGS, "storage", "username");
        public static final ConfigKey<String> SQL_PASSWORD = new ConfigKey<>("root", ConfigType.SETTINGS, "storage", "password");

        public static final ConfigKey<Integer> SAVE_INTERVAL = new ConfigKey<>(1200, ConfigType.SETTINGS, "save-interval");

        public static final ConfigKey<String> PRESSURE_PLATE_MATERIAL = new ConfigKey<>("LIGHT_WEIGHTED_PRESSURE_PLATE", ConfigType.SETTINGS, "pressure-plate", "material");
    }

}
