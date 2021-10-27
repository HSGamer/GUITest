package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.UUID;

public class OutputButton implements Button {
    private final Map<UUID, ItemStack> map = new IdentityHashMap<>();

    @Override
    public ItemStack getItemStack(UUID uuid) {
        return map.getOrDefault(uuid, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));
    }

    public void setItemStack(UUID uuid, ItemStack stack) {
        map.put(uuid, stack);
    }

    @Override
    public void handleAction(UUID uuid, InventoryClickEvent event) {
        ItemStack item = event.getCursor();
        if (item != null && item.getType() != Material.AIR) {
            return;
        }
        ItemStack storeItem = map.remove(uuid);
        event.getWhoClicked().setItemOnCursor(storeItem);
    }

    @Override
    public boolean forceSetAction(UUID uuid) {
        return true;
    }

    @Override
    public void init() {
        // EMPTY
    }

    @Override
    public void stop() {
        map.clear();
    }
}
