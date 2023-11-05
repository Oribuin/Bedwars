package in.oribu.bedwars.item.impl;

import in.oribu.bedwars.BedwarsPlugin;
import in.oribu.bedwars.item.CustomItem;
import in.oribu.bedwars.storage.DataKeys;
import in.oribu.bedwars.upgrade.ContextHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Egg;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

public class BridgeEggItem extends CustomItem {

    private static final int MAX_BRIDGE_LENGTH = 15;

    public BridgeEggItem() {
        super("bridge_egg");
    }

    @Override
    public void event(ContextHandler handler) {
        if (handler.player() == null) return;
        if (handler.itemStack() == null) return;

        final PlayerInteractEvent event = handler.as(PlayerInteractEvent.class);
        if (event != null) {
            final Location location = event.getPlayer().getLocation().clone();
            final Egg egg = event.getPlayer().launchProjectile(
                    Egg.class,
                    location.getDirection().multiply(2)
            );

            egg.getPersistentDataContainer().set(
                    DataKeys.CUSTOM_PROJECTILE,
                    PersistentDataType.STRING,
                    this.name
            );

            Bukkit.getScheduler().runTaskLater(BedwarsPlugin.get(), task -> {
                // Check if the egg is still valid
                if (!egg.isValid()) {
                    task.cancel();
                    return;
                }

                // Check if the egg is too far away
                if (egg.getLocation().distance(location) > MAX_BRIDGE_LENGTH) {
                    egg.remove();
                    task.cancel();
                    return;
                }

                // Create the bridge
                this.createBridge(egg.getLocation());
            }, 3);
        }
    }

    /**
     * Create a bridge at the specified location
     *
     * @param location The location to create the bridge
     */
    private void createBridge(Location location) {
        final Block block = location.getBlock();
        if (!block.getType().isAir()) return;

        // Create a star shape for the bridge
        block.setType(Material.WHITE_WOOL);
        block.getRelative(BlockFace.NORTH).setType(Material.WHITE_WOOL);
        block.getRelative(BlockFace.SOUTH).setType(Material.WHITE_WOOL);
        block.getRelative(BlockFace.EAST).setType(Material.WHITE_WOOL);
        block.getRelative(BlockFace.WEST).setType(Material.WHITE_WOOL);
    }

}
