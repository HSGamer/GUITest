package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.command.CommandManager;
import me.hsgamer.hscore.bukkit.gui.GUIHolder;
import me.hsgamer.hscore.bukkit.gui.GUIListener;
import me.hsgamer.hscore.bukkit.gui.button.AnimatedButton;
import me.hsgamer.hscore.bukkit.gui.button.DummyButton;
import me.hsgamer.hscore.bukkit.gui.button.SimpleButton;
import me.hsgamer.hscore.bukkit.item.ItemBuilder;
import me.hsgamer.hscore.bukkit.item.modifier.AmountModifier;
import me.hsgamer.hscore.bukkit.item.modifier.LoreModifier;
import me.hsgamer.hscore.bukkit.item.modifier.MaterialModifier;
import me.hsgamer.hscore.bukkit.item.modifier.NameModifier;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
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

import java.util.concurrent.ThreadLocalRandom;

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
    private final GUIHolder randomHolder = new GUIHolder(this) {
        @Override
        public void onOpen(InventoryOpenEvent event) {
            getLogger().info(event.getPlayer().getName() + " opened the random menu");
        }

        @Override
        public void onClick(InventoryClickEvent event) {
            getLogger().info(event.getWhoClicked().getName() + " clicked the random menu");
        }

        @Override
        public void onClose(InventoryCloseEvent event) {
            getLogger().info(event.getPlayer().getName() + " closed the random menu");
        }
    };

    private void initHolder() {
        holder.init();
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
        holder.setTitleFunction(uuid -> "Hello " + Bukkit.getOfflinePlayer(uuid).getName());
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

        for (int i = 27; i < 34; i++) {
            holder.setButton(i, animatedButton);
        }

        holder.setButton(34, new SimpleButton(new ItemStack(Material.PAPER), (uuid, inventoryClickEvent) ->
                holder.getDisplay(uuid).ifPresent(guiDisplay -> {
                    guiDisplay.setForceUpdate(!guiDisplay.isForceUpdate());
                    inventoryClickEvent.getWhoClicked().sendMessage("State switched to " + guiDisplay.isForceUpdate());
                })
        ));
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

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, holder::update, 0, 0);
    }

    private void initRandomHolder() {
        randomHolder.init();
        randomHolder.setTitle("Random");
        randomHolder.setButton(0, new DynamicDummyButton(
                new ItemBuilder()
                        .addStringReplacer("player", (original, uuid) -> original.replace("{player}", Bukkit.getOfflinePlayer(uuid).getName()))
                        .addStringReplacer("colorize", (original, uuid) -> MessageUtils.colorize(original))
                        .addItemModifier("material", new MaterialModifier().setMaterial(Material.DIAMOND_SWORD))
                        .addItemModifier("name", new NameModifier().setName("&cHello &6{player}"))
                        .addItemModifier("amount", new AmountModifier().setAmount(34))
                        .addItemModifier("lore", new LoreModifier()
                                .addLore("")
                                .addLore("This is a lore")
                                .addLore("")
                                .addLore("&7This is another lore")
                                .addLore("")
                                .addLore("&eViewer: &b&l{player}")
                        )
        ));
        randomHolder.setButton(1, new AnimatedButton(
                this, 0, false,
                new SimpleButton(new ItemStack(Material.STONE), (uuid, inventoryClickEvent) -> inventoryClickEvent.getWhoClicked().sendMessage("Click 1")),
                new SimpleButton(new ItemStack(Material.SAND), (uuid, inventoryClickEvent) -> inventoryClickEvent.getWhoClicked().sendMessage("Click 2")),
                new SimpleButton(new ItemStack(Material.GLASS), (uuid, inventoryClickEvent) -> inventoryClickEvent.getWhoClicked().sendMessage("Click 3"))
        ));
        randomHolder.addEventConsumer(InventoryClickEvent.class, event -> randomHolder.getButton(event.getRawSlot()).ifPresent(button -> {
            randomHolder.removeButton(event.getRawSlot());
            int slot;
            do {
                slot = ThreadLocalRandom.current().nextInt(0, randomHolder.getSize());
            } while (randomHolder.getButton(slot).isPresent());
            randomHolder.setButton(slot, button);
        }));
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, randomHolder::update, 0, 0);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new GUIListener(), this);

        initHolder();
        commandManager.register(new BukkitCommand("guitest") {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                if (sender instanceof Player) {
                    holder.createDisplay(((Player) sender).getUniqueId()).setForceUpdate(false).init();
                }
                return true;
            }
        });

        initRandomHolder();
        commandManager.register(new BukkitCommand("guitestrandom") {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                if (sender instanceof Player) {
                    randomHolder.createDisplay(((Player) sender).getUniqueId()).setForceUpdate(true).init();
                }
                return true;
            }
        });
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        commandManager.unregisterAll();
        holder.stop();
        randomHolder.stop();
    }
}
