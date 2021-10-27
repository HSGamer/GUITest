package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.gui.GUIListener;
import me.hsgamer.hscore.bukkit.gui.button.impl.DummyButton;
import me.hsgamer.hscore.bukkit.gui.button.impl.SimpleButton;
import me.hsgamer.hscore.bukkit.gui.simple.SimpleGUIHolder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;

public final class GUITest extends BasePlugin {

    @Override
    public void enable() {
        GUIListener.init(this);
        initHolder();
    }

    private void initHolder() {
        SimpleGUIHolder simpleGUIHolder = new UpdateGUIHolder(this);
        simpleGUIHolder.allowMoveItemOnBottom();
        simpleGUIHolder.cancelDragEvent();
        simpleGUIHolder.setSize(9);
        simpleGUIHolder.setDefaultButton(new DummyButton(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));
        simpleGUIHolder.init();

        InputButton input = new InputButton();
        OutputButton output = new OutputButton();
        SimpleButton convert = new SimpleButton(new ItemStack(Material.GRASS_BLOCK), (uuid, event) -> {
            Optional.ofNullable(input.getStoreItem(uuid))
                    .map(ItemStack::clone)
                    .ifPresent(itemStack -> {
                        ItemMeta meta = itemStack.getItemMeta();
                        meta.setDisplayName("CONVERTED");
                        itemStack.setItemMeta(meta);
                        output.setItemStack(uuid, itemStack);
                        input.removeStoreItem(uuid);
                    });
        });

        simpleGUIHolder.setButton(3, input);
        simpleGUIHolder.setButton(4, convert);
        simpleGUIHolder.setButton(5, output);

        registerCommand(new Command("opengui") {
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
