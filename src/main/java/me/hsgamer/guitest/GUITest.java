package me.hsgamer.guitest;

import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.libbyloader.LibbyLoaderAPI;
import net.byteflux.libby.Library;

public final class GUITest extends BasePlugin {
    private final RegisterGUI registerGUI = new RegisterGUI(this);

    static {
        LibbyLoaderAPI.loadRepository("https://repo.codemc.io/repository/maven-public/");
        LibbyLoaderAPI.loadLibrary(
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
    }

    @Override
    public void enable() {
        registerGUI.enable();
    }
}
