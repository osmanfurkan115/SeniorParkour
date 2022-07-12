package me.heymrau.seniorparkour.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.heymrau.seniorparkour.SeniorParkour;
import me.heymrau.seniorparkour.checkpoint.Checkpoint;
import me.heymrau.seniorparkour.config.ConfigKeys;
import me.heymrau.seniorparkour.config.ConfigKeys.Messages;
import me.heymrau.seniorparkour.menu.type.InfoMenu;
import me.heymrau.seniorparkour.menu.type.StatsMenu;
import me.heymrau.seniorparkour.menu.type.TopMenu;
import me.heymrau.seniorparkour.parkour.Parkour;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Map;

@SuppressWarnings("unused")
@CommandAlias("parkour")
public class ParkourCommand extends BaseCommand {

    private final SeniorParkour plugin;

    public ParkourCommand(SeniorParkour plugin) {
        this.plugin = plugin;
    }

    @Subcommand("create")
    @CommandPermission("parkour.admin.create")
    public void create(Player player, String name) {
        Parkour parkour = new Parkour(name, putPressurePlate(player.getLocation()));
        plugin.getParkourManager().getParkours().add(parkour);

        plugin.getHologramManager().createStartHologram(parkour);

        Messages.PARKOUR_CREATED.send(player);
    }

    @Subcommand("checkpoint")
    @CommandCompletion("@parkours @nothing")
    @CommandPermission("parkour.admin.checkpoint")
    public void checkpoint(Player player, Parkour parkour, String name) {
        Checkpoint checkpoint = new Checkpoint(name, putPressurePlate(player.getLocation()));
        parkour.getCheckpoints().add(checkpoint);

        plugin.getHologramManager().createCheckpointHologram(parkour, checkpoint);

        Messages.CHECKPOINT_CREATED.send(player);
    }


    @Subcommand("end")
    @CommandCompletion("@parkours")
    @CommandPermission("parkour.admin.end")
    public void end(Player player, Parkour parkour) {
        parkour.setEndBlock(putPressurePlate(player.getLocation()));

        plugin.getHologramManager().createEndHologram(parkour);

        Messages.PARKOUR_SET_END_LOCATION.send(player);
    }

    @Subcommand("delete")
    @CommandCompletion("@parkours @checkpoints")
    @CommandPermission("parkour.admin.delete")
    public void delete(Player player, Parkour parkour, @Optional Checkpoint checkpoint) {
        if (checkpoint != null) {
            parkour.getCheckpoints().remove(checkpoint);
            checkpoint.deleteHolograms();
            Messages.CHECKPOINT_DELETED.send(player);
            return;
        }
        plugin.getParkourManager().getParkours().remove(parkour);
        parkour.deleteHolograms();
        Messages.PARKOUR_DELETED.send(player);
    }

    @Subcommand("teleport")
    @CommandCompletion("@parkours @checkpoints")
    @CommandPermission("parkour.admin.teleport")
    public void teleport(Player player, Parkour parkour, @Optional Checkpoint checkpoint) {
        if (checkpoint == null) {
            player.teleport(parkour.getStartLocation());
            Messages.PARKOUR_TELEPORT.send(player);
            return;
        }
        player.teleport(checkpoint.block().getLocation().add(0, 1, 0));
        Messages.CHECKPOINT_TELEPORT.send(player, Map.of("checkpoint", checkpoint.name()));
    }

    @Subcommand("top")
    @CommandCompletion("@parkours")
    @CommandPermission("parkour.top")
    public void top(Player player, Parkour parkour) {
        new TopMenu(plugin, parkour).open(player);
    }

    @Subcommand("info")
    @CommandCompletion("@parkours")
    @CommandPermission("parkour.info")
    public void info(Player player, Parkour parkour) {
        new InfoMenu(parkour).open(player);
    }

    @Subcommand("stats")
    @CommandCompletion("@players")
    @CommandPermission("parkour.stats")
    public void stats(Player player, Player target) {
        new StatsMenu(plugin, target).open(player);
    }

    @Subcommand("reload")
    @CommandPermission("parkour.admin.reload")
    public void reload(Player player) {
        plugin.reloadFiles();
    }

    private Block putPressurePlate(Location location) {
        Block block = location.getBlock();
        block.setType(Material.getMaterial(ConfigKeys.Settings.PRESSURE_PLATE_MATERIAL.getValue()));
        return block;
    }
}
