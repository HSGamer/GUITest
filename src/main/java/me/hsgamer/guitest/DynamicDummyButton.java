package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.gui.Button;
import me.hsgamer.hscore.bukkit.item.ItemBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class DynamicDummyButton implements Button {
    private final ItemBuilder builder;

    public DynamicDummyButton(ItemBuilder builder) {
        this.builder = builder;
    }

    @Override
    public ItemStack getItemStack(UUID uuid) {
        return builder.build(uuid);
    }

    @Override
    public void handleAction(UUID uuid, InventoryClickEvent event) {
        // IGNORED
    }

    @Override
    public void init() {
        // IGNORED
    }

    @Override
    public void stop() {
        // IGNORED
    }
}
