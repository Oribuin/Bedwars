package in.oribu.bedwars;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import in.oribu.bedwars.listener.CustomItemListener;
import org.bukkit.Bukkit;

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
    public void enable() {
        // Register Listeners
        Bukkit.getPluginManager().registerEvents(new CustomItemListener(this), this);

    }

    @Override
    public void disable() {

    }

    @Override
    public List<Class<? extends Manager>> getManagerLoadPriority() {
        return List.of();
    }

}
