package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class LogButton implements Button {
    private final Map<UUID, ItemStack> map = new HashMap<>();

    @Override
    public ItemStack getItemStack(UUID uuid) {
        return map.getOrDefault(uuid, new ItemStack(Material.AIR));
    }

    @Override
    public void handleAction(UUID uuid, InventoryClickEvent event) {
        event.setCancelled(false);
        switch (event.getAction()) {
            case DROP_ALL_SLOT:
            case DROP_ONE_SLOT:
            case PICKUP_ALL:
            case PICKUP_HALF:
            case PICKUP_ONE:
            case PICKUP_SOME:
            case HOTBAR_MOVE_AND_READD:
            case PLACE_ALL:
            case PLACE_ONE:
            case PLACE_SOME:
            case HOTBAR_SWAP:
            case SWAP_WITH_CURSOR:
                ItemStack itemStack = Optional.ofNullable(event.getCursor())
                        .map(ItemStack::clone)
                        .orElse(null);
                map.put(uuid, itemStack);
                break;
            default:
                break;
        }
        System.out.println();
        System.out.println("Action: " + event.getAction());
        System.out.println("ClickType: " + event.getClick());
        System.out.println("Cursor: " + event.getCursor());
        System.out.println("Current Item: " + event.getCurrentItem());
        System.out.println();
    }

    @Override
    public void init() {
        // EMPTY
    }

    @Override
    public void stop() {
        // EMPTY
    }
}
