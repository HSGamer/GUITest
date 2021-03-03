package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.gui.GUIListener;
import me.hsgamer.hscore.bukkit.gui.advanced.AdvancedGUIHolder;
import me.hsgamer.hscore.bukkit.gui.button.*;
import me.hsgamer.hscore.bukkit.gui.mask.Mask;
import me.hsgamer.hscore.bukkit.gui.mask.MaskUtils;
import me.hsgamer.hscore.bukkit.gui.mask.impl.MultiSlotsMask;
import me.hsgamer.hscore.bukkit.gui.mask.impl.MultiUserMultiSlotsMask;
import me.hsgamer.hscore.bukkit.gui.mask.impl.SingleMask;
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
        Mask mask1 = new MultiSlotsMask("mask1", MaskUtils.generateOutlineSlots(0, 1, 8, 5).collect(ArrayList::new, ArrayList::add, ArrayList::addAll), new DummyButton(new ItemStack(Material.STAINED_GLASS_PANE)));
        mask1.init();
        MultiUserMultiSlotsMask mask2 = new MultiUserMultiSlotsMask("mask2", MaskUtils.generateAreaSlots(1, 2, 7, 4).collect(ArrayList::new, ArrayList::add, ArrayList::addAll), Button.EMPTY);
        mask2.init();

        Button button1 = new SimpleButton(new ItemStack(Material.GLASS_BOTTLE), (uuid, event) -> mask2.setButtons(uuid, new AirButton((uuid1, event1) -> event1.getWhoClicked().sendMessage("I'm invisible"))));
        Button button2 = new SimpleButton(new ItemStack(Material.DIAMOND_SWORD), (uuid, event) -> mask2.setButtons(uuid, new DummyButton(new ItemStack(Material.DIAMOND_SWORD))));
        Button animatedButton = new AnimatedButton(this, 20, true, new AirButton((uuid, event) -> {}), new DummyButton(new ItemStack(Material.GLASS)), new DummyButton(new ItemStack(Material.REDSTONE)));
        animatedButton.init();
        Button button3 = new SimpleButton(new ItemStack(Material.DIRT), (uuid, event) -> mask2.setButtons(uuid, animatedButton));

        holder.addMask(mask1);
        holder.addMask(mask2);
        holder.addMask(new SingleMask("button1", 0, button1));
        holder.addMask(new SingleMask("button2", 1, button2));
        holder.addMask(new SingleMask("button3", 2, button3));
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
