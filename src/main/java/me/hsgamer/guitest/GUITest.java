package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.command.CommandManager;
import me.hsgamer.hscore.bukkit.gui.Button;
import me.hsgamer.hscore.bukkit.gui.GUIHolder;
import me.hsgamer.hscore.bukkit.gui.GUIListener;
import me.hsgamer.hscore.bukkit.gui.button.AirButton;
import me.hsgamer.hscore.bukkit.gui.button.AnimatedButton;
import me.hsgamer.hscore.bukkit.gui.button.DummyButton;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class GUITest extends JavaPlugin {

    private final CommandManager commandManager = new CommandManager(this);
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

    private void initHolder() {
        holder.setSize(27);
        Button frame1 = new DummyButton(new ItemStack(Material.STONE));
        frame1.init();
        Button frame2 = new DummyButton(new ItemStack(Material.GLASS));
        frame2.init();
        Button frame3 = new DummyButton(new ItemStack(Material.GRASS));
        frame3.init();
        for (int i = 0; i < 9; i++) {
            AnimatedButton animatedButton = new AnimatedButton(this, 5L * i, true);
            animatedButton.addChildButtons(frame1, frame2, frame3);
            animatedButton.init();
            holder.setButton(i, animatedButton);
        }
        Button button = new DummyButton(new ItemStack(Material.STAINED_GLASS_PANE));
        button.init();
        holder.setDefaultButton(button);

        Button airButton = new AirButton((uuid, event) -> holder.removeButton(event.getRawSlot()));
        button.init();
        holder.setButton(9, airButton);
        holder.setButton(17, airButton);

        holder.init();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, holder::update, 0, 0);
        commandManager.register(new Command("guitest") {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                if (sender instanceof Player) {
                    holder.createDisplay(((Player) sender).getUniqueId()).setForceUpdate(false).init();
                }
                return true;
            }
        });
    }

    @Override
    public void onEnable() {
        GUIListener.init(this);
        initHolder();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        commandManager.unregisterAll();
        holder.stop();
    }
}
