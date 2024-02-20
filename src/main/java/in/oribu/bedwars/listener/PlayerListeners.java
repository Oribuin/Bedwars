package in.oribu.bedwars.listener;

import in.oribu.bedwars.BedwarsPlugin;
import in.oribu.bedwars.manager.ScoreboardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListeners implements Listener {

    private final ScoreboardManager manager;

    public PlayerListeners(BedwarsPlugin plugin) {
        this.manager = plugin.getManager(ScoreboardManager.class);
    }

//    private final Map<UUID, ItemStack> lastThrown = new HashMap<>();
//    private final Map<Material, EntityType> projectileTypes = Map.of(
//            Material.EGG, EntityType.EGG,
//            Material.ENDER_PEARL, EntityType.ENDER_PEARL,
//            Material.ENDER_EYE, EntityType.ENDER_SIGNAL,
//            Material.SNOWBALL, EntityType.SNOWBALL
//    );
//
//    @EventHandler
//    public void onInteract(PlayerInteractEvent event) {
//      ItemStack hand = event.getItem();
//        if (hand == null) return;
//
//        if (this.projectileTypes.containsKey(hand.getType())) {
//            this.lastThrown.put(event.getPlayer().getUniqueId(), hand);
//        }
//    }
//
//    @EventHandler
//    public void onLaunch(ProjectileLaunchEvent event) {
//      Projectile projectile = event.getEntity();
//      ProjectileSource shooter = projectile.getShooter();
//        if (!(projectile instanceof ThrowableProjectile)) return;
//        if (!(shooter instanceof Player player)) return;
//        if (!(this.projectileTypes.containsValue(projectile.getType()))) return;
//
//      ItemStack item = this.lastThrown.get(player.getUniqueId());
//      EntityType type = this.projectileTypes.get(item.getType());
//
//        if (projectile.getType() != type) return;
//
//        this.lastThrown.remove(player.getUniqueId());
//        player.sendMessage("You threw a " + item + "!");
//    }


}
