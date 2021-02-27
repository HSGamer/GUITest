package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.gui.advanced.GUIHolder;
import me.hsgamer.hscore.bukkit.gui.advanced.GUIListener;
import me.hsgamer.hscore.bukkit.gui.button.DummyButton;
import me.hsgamer.hscore.bukkit.gui.button.SimpleButton;
import me.hsgamer.hscore.bukkit.gui.mask.Mask;
import me.hsgamer.hscore.bukkit.gui.mask.MaskUtils;
import me.hsgamer.hscore.bukkit.gui.mask.simple.MultiSlotsMask;
import me.hsgamer.hscore.bukkit.gui.mask.simple.SingleMask;
import me.hsgamer.hscore.common.Pair;
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
    private final GUIHolder holder = new GUIHolder(this) {
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
        Mask mask1 = new SingleMask("mask1", 0, new DummyButton(new ItemStack(Material.GLASS)));
        mask1.init();
        Mask mask2 = new SingleMask("mask2", 1, new DummyButton(new ItemStack(Material.STONE)));
        mask2.init();
        Mask mask3 = new MultiSlotsMask("mask3", MaskUtils.generateAreaSlots(MaskUtils.toPosition(2), MaskUtils.toPosition(8)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll), new DummyButton(new ItemStack(Material.REDSTONE)));
        mask3.init();
        Mask mask4 = new MultiSlotsMask("mask4", MaskUtils.generateAreaSlots(Pair.of(0, 1), Pair.of(8, 5)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll), new DummyButton(new ItemStack(Material.DIAMOND)));
        mask4.init();
        Mask mask5 = new MultiSlotsMask("mask5", MaskUtils.generateAreaSlots(Pair.of(1, 2), Pair.of(7, 4)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll), new SimpleButton(new ItemStack(Material.DIAMOND_SWORD), (uuid, event) -> {
            getLogger().info(event.getWhoClicked().getName() + " clicked this diamond sword");
        }));
        mask5.init();
        holder.addMask(mask1);
        holder.addMask(mask2);
        holder.addMask(mask3);
        holder.addMask(mask4);
        holder.addMask(mask5);
        holder.init();
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
