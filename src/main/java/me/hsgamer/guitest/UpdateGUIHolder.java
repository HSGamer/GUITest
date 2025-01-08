package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.gui.BukkitGUIDisplay;
import me.hsgamer.hscore.bukkit.gui.BukkitGUIHolder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UpdateGUIHolder extends BukkitGUIHolder {
    private final Map<UUID, BukkitTask> map = new ConcurrentHashMap<>();

    public UpdateGUIHolder(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected BukkitGUIDisplay newDisplay(UUID uuid) {
        BukkitGUIDisplay guiDisplay = super.newDisplay(uuid);
        map.put(uuid, Bukkit.getScheduler().runTaskTimerAsynchronously(getPlugin(), guiDisplay::update, 0, 0));
        return guiDisplay;
    }

    @Override
    public void removeDisplay(UUID uuid) {
        super.removeDisplay(uuid);
        Optional.ofNullable(map.remove(uuid)).ifPresent(BukkitTask::cancel);
    }
}
