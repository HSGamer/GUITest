package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.gui.Button;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.UUID;

public class CountButton implements Button {

    private int count = 0;

    @Override
    public ItemStack getItemStack(UUID uuid) {
        ItemStack itemStack = new ItemStack(Material.DIAMOND);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Count");
        itemMeta.setLore(Collections.singletonList(String.valueOf(count)));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public void handleAction(UUID uuid, InventoryClickEvent event) {
        event.getWhoClicked().sendMessage("COUNT: " + count++);
    }

    @Override
    public void init() {

    }

    @Override
    public void stop() {

    }
}
