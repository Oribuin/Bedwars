package in.oribu.bedwars.listener;

import in.oribu.bedwars.BedwarsPlugin;
import in.oribu.bedwars.storage.DataKeys;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CustomItemListener implements Listener {

    private final BedwarsPlugin plugin;

    public CustomItemListener(BedwarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onLaunch(ProjectileLaunchEvent event) {
        final PersistentDataContainer container = event.getEntity().getPersistentDataContainer();
        final String projectileType = container.get(DataKeys.CUSTOM_PROJECTILE, PersistentDataType.STRING);
        if (projectileType == null) return;

        // Get the custom item from the registry
    }

}
