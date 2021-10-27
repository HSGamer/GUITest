package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.gui.simple.SimpleGUIDisplay;
import me.hsgamer.hscore.bukkit.gui.simple.SimpleGUIHolder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UpdateGUIHolder extends SimpleGUIHolder {
    private final Map<UUID, BukkitTask> map = new ConcurrentHashMap<>();

    public UpdateGUIHolder(Plugin plugin) {
        super(plugin);
    }

    @Override
    public SimpleGUIDisplay createDisplay(UUID uuid) {
        SimpleGUIDisplay guiDisplay = super.createDisplay(uuid);
        map.put(uuid, Bukkit.getScheduler().runTaskTimerAsynchronously(getPlugin(), guiDisplay::update, 0, 0));
        return guiDisplay;
    }

    @Override
    public void removeDisplay(UUID uuid) {
        super.removeDisplay(uuid);
        Optional.ofNullable(map.remove(uuid)).ifPresent(BukkitTask::cancel);
    }
}
