package in.oribu.bedwars;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import in.oribu.bedwars.listener.CustomItemListener;
import in.oribu.bedwars.manager.CommandManager;
import in.oribu.bedwars.manager.ConfigurationManager;
import in.oribu.bedwars.manager.DataManager;
import in.oribu.bedwars.manager.LocaleManager;
import org.bukkit.Bukkit;

import java.util.List;

public class BedwarsPlugin extends RosePlugin {

    private static BedwarsPlugin instance;

    public BedwarsPlugin() {
        super(
                -1, -1,
                ConfigurationManager.class,
                DataManager.class,
                LocaleManager.class,
                CommandManager.class
        );

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
