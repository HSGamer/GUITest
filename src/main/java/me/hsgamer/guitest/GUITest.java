package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.libbyloader.LibbyLoaderAPI;
import me.hsgamer.libbyloader.LibraryManagerWrapper;
import net.byteflux.libby.Library;

public final class GUITest extends BasePlugin {
    private RegisterGUI registerGUI;

    @Override
    public void load() {
        LibraryManagerWrapper manager = LibbyLoaderAPI.getManager();
        manager.addRepository("https://repo.codemc.io/repository/maven-public/");
        manager.addLibrary(
                Library.builder()
                        .groupId("me.HSGamer")
                        .artifactId("HSCore-bukkit-item")
                        .classifier("jar-with-dependencies")
                        .version("3.17")
                        .build(),
                Library.builder()
                        .groupId("me.HSGamer")
                        .artifactId("HSCore-bukkit-gui-simple")
                        .classifier("jar-with-dependencies")
                        .version("3.17")
                        .build()
        );

        registerGUI = new RegisterGUI(this);
    }

    @Override
    public void enable() {
        registerGUI.enable();
    }
}
