package me.hsgamer.guitest;

import io.github.projectunified.minelib.plugin.base.BasePlugin;
import io.github.projectunified.minelib.plugin.command.CommandComponent;

import java.util.Collections;
import java.util.List;

public final class GUITest extends BasePlugin {
    private final RegisterGUI registerGUI = new RegisterGUI(this);

    @Override
    protected List<Object> getComponents() {
        return Collections.singletonList(new CommandComponent(this));
    }

    @Override
    public void enable() {
        registerGUI.enable();
    }
}
