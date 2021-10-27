package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InputButton implements Button {
    private final Map<UUID, ItemStack> map = new IdentityHashMap<>();

    @Override
    public ItemStack getItemStack(UUID uuid) {
        return map.getOrDefault(uuid, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));
    }

    public ItemStack getStoreItem(UUID uuid) {
        return map.get(uuid);
    }

    public void removeStoreItem(UUID uuid) {
        map.remove(uuid);
    }

    @Override
    public void handleAction(UUID uuid, InventoryClickEvent event) {
        ItemStack cursorItem = Optional.ofNullable(event.getCursor())
                .filter(itemStack -> itemStack.getType() != Material.AIR)
                .map(ItemStack::clone)
                .orElse(null);
        ItemStack storeItem = map.get(uuid);
        event.getWhoClicked().setItemOnCursor(storeItem);
        map.compute(uuid, (uuid1, item) -> cursorItem);
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
