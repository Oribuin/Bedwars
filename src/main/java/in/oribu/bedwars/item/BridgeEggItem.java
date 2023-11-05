package in.oribu.bedwars.item;

import in.oribu.bedwars.upgrade.ContextHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.ArrayList;
import java.util.List;

public class BridgeEggItem extends CustomItem {

    private final List<Projectile> activeProjectiles = new ArrayList<>();
    private static final int MAX_BRIDGE_LENGTH = 15;

    protected BridgeEggItem() {
        super("bridge_egg");
    }

    @Override
    public void event(ContextHandler handler) {
        if (handler.player() == null) return;
        if (handler.itemStack() == null) return;

        final ProjectileLaunchEvent launchEvent = handler.as(ProjectileLaunchEvent.class);
        if (launchEvent != null) {
            final Projectile projectile = launchEvent.getEntity();
            if (!projectile.isValid()) return;

            final Location location = projectile.getLocation().clone();
            final Block block = location.getBlock();
            if (!block.getType().isAir()) return;

            // TODO: Make the bridge 3 blocks wide
            this.createBridge(location);
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
