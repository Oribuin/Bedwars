package in.oribu.bedwars.listener;

import in.oribu.bedwars.BedwarsPlugin;
import in.oribu.bedwars.item.CustomItem;
import in.oribu.bedwars.item.ItemRegistry;
import in.oribu.bedwars.storage.DataKeys;
import in.oribu.bedwars.upgrade.ContextHandler;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
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

        // We have to assume all projectiles being shot are custom projectiles
        String projectileType = switch (event.getEntity().getType()) {
            case EGG -> "bridge_egg";
            case FIREBALL -> "fireball";
            case SNOWBALL -> "slow_ball";
            default -> null;
        };

        // Get the custom item from the registry
        CustomItem item = ItemRegistry.get(projectileType);
        if (item == null) return;

        // Add the persistent data type.
        PersistentDataContainer container = event.getEntity().getPersistentDataContainer();
        container.set(DataKeys.CUSTOM_PROJECTILE, PersistentDataType.STRING, projectileType);

        item.event(new ContextHandler(event, null, player));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        PersistentDataContainer container = projectile.getPersistentDataContainer();
        String projectileType = container.get(DataKeys.CUSTOM_PROJECTILE, PersistentDataType.STRING);
        CustomItem customItem = ItemRegistry.get(projectileType);
        if (customItem == null) return;

        customItem.event(new ContextHandler(event, null, projectile.getShooter() instanceof Player player ? player : null));
    }

    @EventHandler(ignoreCancelled = true)
    public void onSpawn(CreatureSpawnEvent event) {
        // Stop eggs from spawning
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.EGG) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        String projectileType = container.get(DataKeys.CUSTOM_ITEM, PersistentDataType.STRING);
        CustomItem customItem = ItemRegistry.get(projectileType);
        if (customItem == null) return;

        customItem.event(new ContextHandler(event, item, event.getPlayer()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        String projectileType = container.get(DataKeys.CUSTOM_ITEM, PersistentDataType.STRING);
        CustomItem customItem = ItemRegistry.get(projectileType);
        if (customItem == null) return;

        customItem.event(new ContextHandler(event, item, event.getPlayer()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onBreak(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        String projectileType = container.get(DataKeys.CUSTOM_ITEM, PersistentDataType.STRING);
        CustomItem customItem = ItemRegistry.get(projectileType);
        if (customItem == null) return;

        customItem.event(new ContextHandler(event, item, event.getPlayer()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onEntityExplode(EntityExplodeEvent event) {
        PersistentDataContainer container = event.getEntity().getPersistentDataContainer();
        String projectileType = container.get(DataKeys.CUSTOM_PROJECTILE, PersistentDataType.STRING);
        CustomItem customItem = ItemRegistry.get(projectileType);
        if (customItem == null) return;

        customItem.event(new ContextHandler(event, null, null));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onBlockDamage(BlockDamageEvent event) {
        ItemStack item = event.getItemInHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        String projectileType = container.get(DataKeys.CUSTOM_ITEM, PersistentDataType.STRING);
        CustomItem customItem = ItemRegistry.get(projectileType);
        if (customItem == null) return;

        customItem.event(new ContextHandler(event, item, event.getPlayer()));
    }

}
