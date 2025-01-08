package me.hsgamer.guitest;

import io.github.projectunified.minelib.plugin.command.CommandComponent;
import me.hsgamer.hscore.bukkit.gui.BukkitGUIListener;
import me.hsgamer.hscore.bukkit.gui.BukkitGUIUtils;
import me.hsgamer.hscore.bukkit.gui.button.impl.InputButton;
import me.hsgamer.hscore.bukkit.gui.button.impl.OutputButton;
import me.hsgamer.hscore.bukkit.gui.object.BukkitItem;
import me.hsgamer.hscore.minecraft.gui.button.impl.DummyButton;
import me.hsgamer.hscore.minecraft.gui.button.impl.SimpleButton;
import me.hsgamer.hscore.minecraft.gui.simple.SimpleButtonMap;
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
        BukkitGUIListener.init(plugin);
        initHolder();
    }

    private void initHolder() {
        UpdateGUIHolder simpleGUIHolder = new UpdateGUIHolder(plugin);
        BukkitGUIUtils.allowMoveItemOnBottom(simpleGUIHolder);
        BukkitGUIUtils.cancelDragEvent(simpleGUIHolder);
        simpleGUIHolder.setSize(9);

        InputButton input = new InputButton();
        input.setDisplayItemFunction((uuid, itemStack) -> Optional.ofNullable(itemStack).orElse(new ItemStack(Material.GREEN_STAINED_GLASS_PANE)));
        OutputButton output = new OutputButton();
        output.setDisplayItemFunction((uuid, itemStack) -> Optional.ofNullable(itemStack).orElse(new ItemStack(Material.WHITE_STAINED_GLASS_PANE)));
        SimpleButton convert = new SimpleButton(new BukkitItem(new ItemStack(Material.GRASS_BLOCK)), event -> Optional.ofNullable(input.getInputItem(event.getViewerID()))
                .map(ItemStack::clone)
                .ifPresent(itemStack -> {
                    ItemMeta meta = itemStack.getItemMeta();
                    meta.setDisplayName("CONVERTED");
                    itemStack.setItemMeta(meta);
                    output.setOutputItem(event.getViewerID(), itemStack);
                    input.setInputItem(event.getViewerID(), null);
                }));


        SimpleButtonMap buttonMap = new SimpleButtonMap();
        buttonMap.setButton(3, input);
        buttonMap.setButton(4, convert);
        buttonMap.setButton(5, output);
        buttonMap.setDefaultButton(new DummyButton(new BukkitItem(new ItemStack(Material.RED_STAINED_GLASS_PANE))));

        simpleGUIHolder.setButtonMap(buttonMap);
        simpleGUIHolder.init();

        plugin.get(CommandComponent.class).register(new Command("opengui") {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                if (sender instanceof Player) {
                    simpleGUIHolder.createDisplay(((Player) sender).getUniqueId()).open();
                }
                return false;
            }
        });
    }
}
