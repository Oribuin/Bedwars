package in.oribu.bedwars;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;

import java.util.List;

public class BedwarsPlugin extends RosePlugin {

    private static BedwarsPlugin instance;

    public BedwarsPlugin() {
        super(-1, -1, null, null, null, null);

        instance = this;
    }

    public static BedwarsPlugin get() {
        return instance;
    }

    @Override
    protected void enable() {

    }

    @Override
    protected void disable() {

    }

    @Override
    protected List<Class<? extends Manager>> getManagerLoadPriority() {
        return List.of();
    }

}
