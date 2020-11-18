package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.command.CommandManager;
import me.hsgamer.hscore.bukkit.gui.GUIHolder;
import me.hsgamer.hscore.bukkit.gui.GUIListener;
import me.hsgamer.hscore.bukkit.gui.button.AnimatedButton;
import me.hsgamer.hscore.bukkit.gui.button.DummyButton;
import me.hsgamer.hscore.bukkit.gui.button.SimpleButton;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
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

    @Override
    public void onEnable() {
        holder.setCloseFilter(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                return true;
            }

            if (Math.random() > 0.7) {
                player.sendMessage("You are lucky");
                return true;
            } else {
                player.sendMessage("You are bad");
                return false;
            }
        });

        holder.setSize(54);
        holder.setTitle("GUI Test");
        holder.setButton(0, new DummyButton(new ItemStack(Material.STONE)));
        holder.setButton(8, new DummyButton(new ItemStack(Material.COBBLESTONE)));

        for (int i = 9; i < 18; i++) {
            holder.setButton(i, new CountButton());
        }

        CountButton button = new CountButton();
        for (int i = 18; i < 27; i++) {
            holder.setButton(i, button);
        }

        AnimatedButton animatedButton = new AnimatedButton(
                this, 5, false,
                new DummyButton(new ItemStack(Material.SAND)),
                new DummyButton(new ItemStack(Material.IRON_SWORD)),
                new DummyButton(new ItemStack(Material.DIAMOND_SWORD))
        );

        for (int i = 27; i < 35; i++) {
            holder.setButton(i, animatedButton);
        }

        holder.setButton(35, new SimpleButton(new ItemStack(Material.BARRIER), (uuid, event) -> event.getWhoClicked().closeInventory()));

        for (int i = 36; i < 45; i++) {
            holder.setButton(i, new AnimatedButton(
                    this, (i - 35), true,
                    new DummyButton(new ItemStack(Material.IRON_SWORD)),
                    new DummyButton(new ItemStack(Material.DIAMOND_SWORD))
            ));
        }

        for (int i = 45; i < 54; i++) {
            holder.setButton(i, new AnimatedButton(
                    this, (i - 44), true,
                    new AnimatedButton(
                            this, 10, false,
                            new SimpleButton(new ItemStack(Material.STONE), (uuid, inventoryClickEvent) -> inventoryClickEvent.getWhoClicked().sendMessage("Click 1")),
                            new SimpleButton(new ItemStack(Material.SAND), (uuid, inventoryClickEvent) -> inventoryClickEvent.getWhoClicked().sendMessage("Click 2")),
                            new SimpleButton(new ItemStack(Material.GLASS), (uuid, inventoryClickEvent) -> inventoryClickEvent.getWhoClicked().sendMessage("Click 3"))
                    ),
                    new AnimatedButton(
                            this, 20, false,
                            new SimpleButton(new ItemStack(Material.STONE), (uuid, inventoryClickEvent) -> inventoryClickEvent.getWhoClicked().sendMessage("Click 1")),
                            new SimpleButton(new ItemStack(Material.SAND), (uuid, inventoryClickEvent) -> inventoryClickEvent.getWhoClicked().sendMessage("Click 2")),
                            new SimpleButton(new ItemStack(Material.GLASS), (uuid, inventoryClickEvent) -> inventoryClickEvent.getWhoClicked().sendMessage("Click 3"))
                    ),
                    new AnimatedButton(
                            this, 30, false,
                            new SimpleButton(new ItemStack(Material.STONE), (uuid, inventoryClickEvent) -> inventoryClickEvent.getWhoClicked().sendMessage("Click 1")),
                            new SimpleButton(new ItemStack(Material.SAND), (uuid, inventoryClickEvent) -> inventoryClickEvent.getWhoClicked().sendMessage("Click 2")),
                            new SimpleButton(new ItemStack(Material.GLASS), (uuid, inventoryClickEvent) -> inventoryClickEvent.getWhoClicked().sendMessage("Click 3"))
                    )
            ));
        }

        Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, holder::updateAll, 0, 0);

        commandManager.register(new BukkitCommand("guitest") {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                if (sender instanceof Player) {
                    holder.createDisplay(((Player) sender).getUniqueId()).setForceUpdate(true).init();
                }
                return true;
            }
        });
    }

    @Override
    public void onDisable() {
        commandManager.unregisterAll();
        holder.clearAll();
    }
}
