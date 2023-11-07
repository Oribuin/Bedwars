package in.oribu.bedwars.item.impl;

import in.oribu.bedwars.BedwarsPlugin;
import in.oribu.bedwars.item.CustomItem;
import in.oribu.bedwars.storage.DataKeys;
import in.oribu.bedwars.upgrade.ContextHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Egg;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.persistence.PersistentDataType;

/**
 * Spigot API Is terrible when it comes to projectiles, so we have to assume all egs are custom eggs
 * only trigger these eggs inside a match
 */
public class BridgeEggItem extends CustomItem {

    private static final int MAX_BRIDGE_LENGTH = 45;

    public BridgeEggItem() {
        super("bridge_egg");
    }

    @Override
    public void event(ContextHandler handler) {
        if (handler.player() == null) return;

        final ProjectileLaunchEvent event = handler.as(ProjectileLaunchEvent.class);
        if (event == null) {
            return;
        }

        if (!(event.getEntity() instanceof Egg egg)) return;

        egg.getPersistentDataContainer().set(
                DataKeys.CUSTOM_PROJECTILE,
                PersistentDataType.STRING,
                this.name
        );

        Bukkit.getScheduler().runTaskTimer(BedwarsPlugin.get(), task -> {
            // Check if the egg is still valid
            if (!egg.isValid()) {
                task.cancel();
                return;
            }

            // Check if the egg is too far away
            if (egg.getLocation().distance(handler.player().getLocation()) > MAX_BRIDGE_LENGTH) {
                egg.remove();
                task.cancel();
                return;
            }

            // Create the bridge
            this.createBridge(egg.getLocation());
        }, 0, 1);
    }

    /**
     * Create a bridge at the specified location
     *
     * @param location The location to create the bridge
     */
    private void createBridge(Location location) {
        // Make the center the block behind the egg
        final Block center = location.clone().subtract(0, 2, 0).getBlock();

        // Create a square around the center
//        for (int x = -2; x <= 0; x++) {
//            for (int z = -2; z <= 0; z++) {
//                final Block block = center.getRelative(x, 0, z);
//                if (!block.getType().isAir()) continue;
//                block.setType(Material.WHITE_WOOL);
//            }
//        }

        // Create a star around the center
        final BlockData data = Material.WHITE_WOOL.createBlockData();

        center.getRelative(1, 0, 0).setBlockData(data, false);
        center.getRelative(-1, 0, 0).setBlockData(data, false);
        center.getRelative(0, 0, 1).setBlockData(data, false);
        center.getRelative(0, 0, -1).setBlockData(data, false);
        center.setBlockData(data, false);
    }

}
