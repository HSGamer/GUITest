package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.gui.GUIListener;
import me.hsgamer.hscore.bukkit.gui.advanced.AdvancedGUIHolder;
import me.hsgamer.hscore.bukkit.gui.button.impl.DummyButton;
import me.hsgamer.hscore.bukkit.gui.button.impl.NullButton;
import me.hsgamer.hscore.bukkit.gui.button.impl.SimpleButton;
import me.hsgamer.hscore.bukkit.gui.mask.MaskUtils;
import me.hsgamer.hscore.bukkit.gui.mask.impl.ButtonPaginatedMask;
import me.hsgamer.hscore.bukkit.gui.mask.impl.MultiSlotsMask;
import me.hsgamer.hscore.bukkit.gui.mask.impl.SequencePaginatedMask;
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

        SequencePaginatedMask sequencePaginatedMask = new SequencePaginatedMask("button_mask", MaskUtils.generateAreaSlots(0, 0, 8, 4).collect(ArrayList::new, ArrayList::add , ArrayList::addAll));
        ButtonPaginatedMask buttonPaginatedMask = new ButtonPaginatedMask("button_mask_2", MaskUtils.generateAreaSlots(1, 5, 7, 5).collect(ArrayList::new, ArrayList::add , ArrayList::addAll));
        for (Material material : Material.values()) {
            if (material.isItem() && !material.isAir()) {
                SimpleButton simpleButton = new SimpleButton(new ItemStack(material), (uuid, inventoryClickEvent) -> inventoryClickEvent.getWhoClicked().sendMessage(material.name()));
                sequencePaginatedMask.addButtons(simpleButton);
                buttonPaginatedMask.addButtons(simpleButton);
            }
        }
        sequencePaginatedMask.setCycle(true);
        sequencePaginatedMask.init();
        buttonPaginatedMask.setCycle(true);
        buttonPaginatedMask.init();

        MultiSlotsMask multiSlotsMask = new MultiSlotsMask("override_mask", MaskUtils.generateAreaSlots(1, 1, 7, 4).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
        multiSlotsMask.addButtons(new NullButton((uuid, inventoryClickEvent) -> inventoryClickEvent.getWhoClicked().sendMessage("Hello")));
        multiSlotsMask.addButtons(new NullButton((uuid, inventoryClickEvent) -> {}));
        multiSlotsMask.init();

        SingleMask previous = new SingleMask("pre", Position2D.of(0, 5), new SimpleButton(new ItemStack(Material.RED_STAINED_GLASS_PANE), (uuid, inventoryClickEvent) -> {
            sequencePaginatedMask.previousPage(uuid);
            buttonPaginatedMask.previousPage(uuid);
        }));
        SingleMask next = new SingleMask("next", Position2D.of(8, 5), new SimpleButton(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), (uuid, inventoryClickEvent) -> {
            sequencePaginatedMask.nextPage(uuid);
            buttonPaginatedMask.nextPage(uuid);
        }));

        holder.addMask(sequencePaginatedMask);
        holder.addMask(buttonPaginatedMask);
        holder.addMask(multiSlotsMask);
        holder.addMask(previous);
        holder.addMask(next);
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
