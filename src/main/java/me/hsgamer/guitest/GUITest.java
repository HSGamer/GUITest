package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.gui.GUIListener;
import me.hsgamer.hscore.bukkit.gui.button.impl.DummyButton;
import me.hsgamer.hscore.bukkit.gui.simple.SimpleGUIHolder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class GUITest extends BasePlugin {

    @Override
    public void enable() {
        GUIListener.init(this);
        initHolder();
    }

    private void initHolder() {
        SimpleGUIHolder simpleGUIHolder = new SimpleGUIHolder(this);
        simpleGUIHolder.allowMoveItemOnBottom();
        simpleGUIHolder.cancelDragEvent();
        simpleGUIHolder.setSize(9);
        simpleGUIHolder.setDefaultButton(new DummyButton(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));
        simpleGUIHolder.setButton(4, new LogButton());
        simpleGUIHolder.init();

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
