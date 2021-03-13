package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.gui.GUIListener;
import me.hsgamer.hscore.bukkit.gui.advanced.AdvancedGUIHolder;
import me.hsgamer.hscore.bukkit.gui.button.impl.SimpleButton;
import me.hsgamer.hscore.bukkit.gui.mask.MaskUtils;
import me.hsgamer.hscore.bukkit.gui.mask.impl.PaginatedMask;
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

        PaginatedMask mask = new PaginatedMask("pagemask", MaskUtils.generateAreaSlots(0, 0, 8, 4).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
        for (Material material : Material.values()) {
            if (material.isItem() && !material.isAir()) {
                mask.addButtons(new SimpleButton(new ItemStack(material), (uuid, inventoryClickEvent) -> inventoryClickEvent.getWhoClicked().sendMessage(material.name())));
            }
        }
        mask.init();

        SingleMask nextMask = new SingleMask("nextmask", Position2D.of(0, 5), new SimpleButton(new ItemStack(Material.RED_STAINED_GLASS_PANE), (uuid, inventoryClickEvent) -> mask.previousPage(uuid)));
        SingleMask previousMask = new SingleMask("previousmask", Position2D.of(8, 5), new SimpleButton(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), (uuid, inventoryClickEvent) -> mask.nextPage(uuid)));
        nextMask.init();
        previousMask.init();

        holder.addMask(mask);
        holder.addMask(nextMask);
        holder.addMask(previousMask);

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
