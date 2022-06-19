package me.heymrau.seniorparkour;

import co.aikar.commands.*;
import lombok.AccessLevel;
import lombok.Getter;
import me.heymrau.seniorparkour.checkpoint.Checkpoint;
import me.heymrau.seniorparkour.checkpoint.CheckpointManager;
import me.heymrau.seniorparkour.commands.ParkourCommand;
import me.heymrau.seniorparkour.config.Config;
import me.heymrau.seniorparkour.config.ConfigKeys.Settings;
import me.heymrau.seniorparkour.config.ConfigManager;
import me.heymrau.seniorparkour.config.ConfigType;
import me.heymrau.seniorparkour.hologram.HologramManager;
import me.heymrau.seniorparkour.listener.HologramListener;
import me.heymrau.seniorparkour.listener.ParkourEntryListener;
import me.heymrau.seniorparkour.listener.ParkourProtectionListener;
import me.heymrau.seniorparkour.listener.UserListener;
import me.heymrau.seniorparkour.menu.listener.MenuListener;
import me.heymrau.seniorparkour.parkour.Parkour;
import me.heymrau.seniorparkour.parkour.ParkourManager;
import me.heymrau.seniorparkour.parkour.entry.ParkourEntryManager;
import me.heymrau.seniorparkour.storage.DataSource;
import me.heymrau.seniorparkour.storage.StorageManager;
import me.heymrau.seniorparkour.task.AutoSaveTask;
import me.heymrau.seniorparkour.user.UserManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


@Getter
public final class SeniorParkour extends JavaPlugin {

    @Getter(AccessLevel.PUBLIC)
    private static SeniorParkour instance;

    private final ConfigManager configManager = new ConfigManager();
    private HologramManager hologramManager;
    private ParkourManager parkourManager;
    private CheckpointManager checkpointManager;
    private StorageManager storageManager;
    private UserManager userManager;
    private ParkourEntryManager parkourEntryManager;

    @Override
    public void onEnable() {
        instance = this;

        reloadFiles();

        hologramManager = new HologramManager();
        parkourManager = new ParkourManager();
        checkpointManager = new CheckpointManager();
        storageManager = new StorageManager(DataSource.getDataSource(Settings.STORAGE_TYPE.getValue()));
        userManager = new UserManager();
        parkourEntryManager = new ParkourEntryManager();

        parkourManager.loadParkours();

        registerCommands();

        registerListeners(new UserListener(), new MenuListener(), new HologramListener(), new ParkourProtectionListener(), new ParkourEntryListener());

        new AutoSaveTask().start();
    }

    @Override
    public void onDisable() {
        parkourManager.saveParkours();
        userManager.saveUsers();
    }

    private void registerCommands() {
        PaperCommandManager commandManager = new PaperCommandManager(this);

        CommandContexts<BukkitCommandExecutionContext> commandContexts = commandManager.getCommandContexts();
        commandContexts.registerContext(Parkour.class, c ->
                parkourManager.findByName(c.popFirstArg())
                        .orElseThrow(() -> new InvalidCommandArgument("Could not find a parkour with that name.")));

        commandContexts.registerContext(Checkpoint.class, c ->
                checkpointManager.findByName(c.popFirstArg())
                        .orElseThrow(() -> new InvalidCommandArgument("Could not find a checkpoint with that name.")));

        CommandCompletions<BukkitCommandCompletionContext> commandCompletions = commandManager.getCommandCompletions();
        commandCompletions.registerCompletion("parkours", c -> parkourManager.getParkours().stream().map(Parkour::getName).toList());
        commandCompletions.registerCompletion("checkpoints", c ->  c.getContextValue(Parkour.class).getCheckpoints().stream().map(Checkpoint::name).toList());

        commandManager.registerCommand(new ParkourCommand());
    }

    @SafeVarargs
    private <T extends Listener> void registerListeners(T... listeners) {
        for (T listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    public void reloadFiles() {
        reloadConfig("settings", "settings.yml", ConfigType.SETTINGS);
        reloadConfig("messages", "messages.yml", ConfigType.MESSAGES);
    }

    private void reloadConfig(String name, String path, ConfigType configType) {
        ClassLoader classLoader = getClassLoader();
        String folderPath = getDataFolder().getAbsolutePath();

        Config config = configManager.createConfig(name, String.format("%s/%s", folderPath, path), classLoader.getResourceAsStream(path));
        configManager.reloadConfig(config, configType);
    }
}
