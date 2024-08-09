package in.oribu.bedwars;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import in.oribu.bedwars.listener.CustomItemListener;
import in.oribu.bedwars.listener.MatchListener;
import in.oribu.bedwars.listener.PlayerListeners;
import in.oribu.bedwars.manager.CommandManager;
import in.oribu.bedwars.manager.DataManager;
import in.oribu.bedwars.manager.GameManager;
import in.oribu.bedwars.manager.LocaleManager;
import in.oribu.bedwars.manager.ScoreboardManager;
import in.oribu.bedwars.manager.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.List;

public class BedwarsPlugin extends RosePlugin {

    private static BedwarsPlugin instance;

    public BedwarsPlugin() {
        super(
                -1, -1,
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
        PluginManager pluginManager = Bukkit.getPluginManager();

        // Register Listeners
        pluginManager.registerEvents(new CustomItemListener(this), this);
        pluginManager.registerEvents(new PlayerListeners(this), this);
        pluginManager.registerEvents(new MatchListener(this), this);
    }

    @Override
    public void disable() {

    }

    @Override
    public List<Class<? extends Manager>> getManagerLoadPriority() {
        return List.of(ScoreboardManager.class, ShopManager.class, GameManager.class);
    }

}
