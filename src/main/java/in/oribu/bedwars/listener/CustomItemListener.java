package in.oribu.bedwars.listener;

import in.oribu.bedwars.BedwarsPlugin;
import in.oribu.bedwars.item.CustomItem;
import in.oribu.bedwars.item.ItemRegistry;
import in.oribu.bedwars.storage.DataKeys;
import in.oribu.bedwars.upgrade.ContextHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CustomItemListener implements Listener {

    private final BedwarsPlugin plugin;

    public CustomItemListener(BedwarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player player)) return;

        final PersistentDataContainer container = event.getEntity().getPersistentDataContainer();
        final String projectileType = container.get(DataKeys.CUSTOM_PROJECTILE, PersistentDataType.STRING);
        if (projectileType == null) return;

        // Get the custom item from the registry
        final CustomItem item = ItemRegistry.get(projectileType);
        if (item == null) return;

        // Add the persistent data type.
        // TODO: Check if ItemInMainHand is the projectile
        item.event(new ContextHandler(event, player.getInventory().getItemInMainHand(), player));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        if (item == null) return;

        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        final PersistentDataContainer container = meta.getPersistentDataContainer();
        final String projectileType = container.get(DataKeys.CUSTOM_PROJECTILE, PersistentDataType.STRING);
        final CustomItem customItem = ItemRegistry.get(projectileType);
        if (customItem == null) return;

        customItem.event(new ContextHandler(event, item, event.getPlayer()));
    }

}
