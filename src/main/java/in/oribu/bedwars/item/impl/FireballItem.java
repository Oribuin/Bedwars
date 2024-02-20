package in.oribu.bedwars.item.impl;

import in.oribu.bedwars.item.CustomItem;
import in.oribu.bedwars.item.ItemRegistry;
import in.oribu.bedwars.storage.DataKeys;
import in.oribu.bedwars.upgrade.ContextHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.Duration;

public class FireballItem extends CustomItem {

    private static final float EXPLOSION_POWER = 3.0F;

    public FireballItem() {
        super("fireball");
    }

    /**
     * The event called with the item
     *
     * @param handler The context handler
     */
    @Override
    public void event(ContextHandler handler) {
        PlayerInteractEvent interactEvent = handler.as(PlayerInteractEvent.class);
        if (interactEvent != null) {
            if (handler.player() == null) return;

            ItemStack itemStack = interactEvent.getItem();
            if (itemStack == null) return;
            if (ItemRegistry.isOnCooldown(handler.player().getUniqueId(), this)) return;

            interactEvent.getPlayer().launchProjectile(
                    Fireball.class,
                    interactEvent.getPlayer().getLocation().getDirection().multiply(2),
                    fireball -> {
                        PersistentDataContainer container = fireball.getPersistentDataContainer();
                        container.set(DataKeys.CUSTOM_PROJECTILE, PersistentDataType.STRING, this.getName());

                        fireball.setDirection(interactEvent.getPlayer().getLocation().getDirection());
                        fireball.setIsIncendiary(false);
                    }
            );

            ItemRegistry.setCooldown(handler.player().getUniqueId(), this);
        }

        ProjectileHitEvent hitEvent = handler.as(ProjectileHitEvent.class);
        if (hitEvent != null && hitEvent.getEntity() instanceof Fireball fireball) {
            hitEvent.setCancelled(true);
            fireball.getWorld().createExplosion(fireball.getLocation(), EXPLOSION_POWER, false, true);

            for (Entity nearby : fireball.getLocation().getWorld().getNearbyEntities(fireball.getLocation(), EXPLOSION_POWER, EXPLOSION_POWER, EXPLOSION_POWER)) {
                nearby.setVelocity(nearby.getVelocity().clone().multiply(2));
            }
        }

    }

    @Override
    public Duration getCooldown() {
        return Duration.ofSeconds(1);
    }

}
