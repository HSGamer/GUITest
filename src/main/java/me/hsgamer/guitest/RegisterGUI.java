package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.gui.GUIListener;
import me.hsgamer.hscore.bukkit.gui.GUIUtils;
import me.hsgamer.hscore.bukkit.gui.button.impl.DummyButton;
import me.hsgamer.hscore.bukkit.gui.button.impl.InputButton;
import me.hsgamer.hscore.bukkit.gui.button.impl.OutputButton;
import me.hsgamer.hscore.bukkit.gui.button.impl.SimpleButton;
import me.hsgamer.hscore.bukkit.gui.simple.SimpleGUIHolder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;

public class RegisterGUI {
    private final GUITest plugin;

    public RegisterGUI(GUITest plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        GUIListener.init(plugin);
        initHolder();
    }

    private void initHolder() {
        SimpleGUIHolder simpleGUIHolder = new UpdateGUIHolder(plugin);
        GUIUtils.allowMoveItemOnBottom(simpleGUIHolder);
        GUIUtils.cancelDragEvent(simpleGUIHolder);
        simpleGUIHolder.setSize(9);
        simpleGUIHolder.setDefaultButton(new DummyButton(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));
        simpleGUIHolder.init();

        InputButton input = new InputButton();
        input.setDisplayItemFunction((uuid, itemStack) -> Optional.ofNullable(itemStack).orElse(new ItemStack(Material.GREEN_STAINED_GLASS_PANE)));
        OutputButton output = new OutputButton();
        output.setDisplayItemFunction((uuid, itemStack) -> Optional.ofNullable(itemStack).orElse(new ItemStack(Material.WHITE_STAINED_GLASS_PANE)));
        SimpleButton convert = new SimpleButton(new ItemStack(Material.GRASS_BLOCK), (uuid, event) -> Optional.ofNullable(input.getInputItem(uuid))
                .map(ItemStack::clone)
                .ifPresent(itemStack -> {
                    ItemMeta meta = itemStack.getItemMeta();
                    meta.setDisplayName("CONVERTED");
                    itemStack.setItemMeta(meta);
                    output.setOutputItem(uuid, itemStack);
                    input.setInputItem(uuid, null);
                }));

        simpleGUIHolder.setButton(3, input);
        simpleGUIHolder.setButton(4, convert);
        simpleGUIHolder.setButton(5, output);

        plugin.registerCommand(new Command("opengui") {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                if (sender instanceof Player) {
                    simpleGUIHolder.createDisplay(((Player) sender).getUniqueId()).init();
                }
                return false;
            }
        });
    }
}
