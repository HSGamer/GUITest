package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.gui.GUIListener;
import me.hsgamer.hscore.bukkit.gui.advanced.AdvancedGUIHolder;
import me.hsgamer.hscore.bukkit.gui.button.impl.SimpleButton;
import me.hsgamer.hscore.bukkit.gui.mask.MaskUtils;
import me.hsgamer.hscore.bukkit.gui.mask.impl.ButtonPaginatedMask;
import me.hsgamer.hscore.bukkit.gui.mask.impl.MaskPaginatedMask;
import me.hsgamer.hscore.bukkit.gui.mask.impl.SingleMask;
import me.hsgamer.hscore.ui.Position2D;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public final class GUITest extends BasePlugin {
    private final AdvancedGUIHolder holder = new AdvancedGUIHolder(this) {
        @Override
        public void onOpen(InventoryOpenEvent event) {
            getLogger().info(event.getPlayer().getName() + " opened the menu");
        }

        @Override
        public void onClick(InventoryClickEvent event) {
            getLogger().info(event.getWhoClicked().getName() + " clicked the menu");
        }

        @Override
        public void onClose(InventoryCloseEvent event) {
            getLogger().info(event.getPlayer().getName() + " closed the menu");
        }
    };

    @Override
    public void enable() {
        GUIListener.init(this);
        initHolder();
    }

    @Override
    public void disable() {
        holder.stop();
    }

    private void initHolder() {
        holder.setSize(54);

        MaskPaginatedMask maskPaginatedMask = new MaskPaginatedMask("mask_paginated_mask");
        for (int i = 0; i < 5; i++) {
            ButtonPaginatedMask buttonPaginatedMask = new ButtonPaginatedMask("button_paginated_mask_" + i, MaskUtils.generateAreaSlots(1, 0, 7, 5).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
            for (Material material : Material.values()) {
                if (material.isItem() && !material.isAir()) {
                    buttonPaginatedMask.addButtons(new SimpleButton(new ItemStack(material), (uuid, event) -> event.getWhoClicked().sendMessage(material.name())));
                }
            }
            maskPaginatedMask.addMasks(buttonPaginatedMask);
        }
        maskPaginatedMask.init();
        holder.addMask(maskPaginatedMask);
        SingleMask nextPage = new SingleMask("next_page", Position2D.of(8, 2), new SimpleButton(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), (uuid, event) -> {
            ButtonPaginatedMask paginatedMask = (ButtonPaginatedMask) maskPaginatedMask.getMasks().get(maskPaginatedMask.getPage(uuid));
            paginatedMask.nextPage(uuid);
        }));
        SingleMask prevPage = new SingleMask("prev_page", Position2D.of(0, 2), new SimpleButton(new ItemStack(Material.RED_STAINED_GLASS_PANE), (uuid, event) -> {
            ButtonPaginatedMask paginatedMask = (ButtonPaginatedMask) maskPaginatedMask.getMasks().get(maskPaginatedMask.getPage(uuid));
            paginatedMask.previousPage(uuid);
        }));
        SingleMask nextMaskPage = new SingleMask("next_mask_page", Position2D.of(8, 5), new SimpleButton(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), (uuid, event) -> maskPaginatedMask.nextPage(uuid)));
        SingleMask prevMaskPage = new SingleMask("prev_mask_page", Position2D.of(0, 5), new SimpleButton(new ItemStack(Material.RED_STAINED_GLASS_PANE), (uuid, event) -> maskPaginatedMask.previousPage(uuid)));
        holder.addMask(nextMaskPage);
        holder.addMask(prevMaskPage);
        holder.addMask(nextPage);
        holder.addMask(prevPage);

        holder.init();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, this.holder::update, 0, 0);
        registerCommand(new Command("guitest") {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                if (sender instanceof Player) {
                    holder.createDisplay(((Player) sender).getUniqueId()).setForceUpdate(false).init();
                }
                return true;
            }
        });
    }
}
