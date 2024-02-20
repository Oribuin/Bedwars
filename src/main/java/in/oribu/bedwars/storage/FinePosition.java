package in.oribu.bedwars.storage;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public record FinePosition(String world, int x, int y, int z) {

    /**
     * Create a FinePosition from a Location
     *
     * @param location The location
     * @return The FinePosition
     */
    public static FinePosition from(Location location) {
        return new FinePosition(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /**
     * Convert a FinePosition to a Location
     *
     * @return The location
     */
    public Location toLocation() {
        return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z);
    }

    /**
     * Check if the FinePosition matches a Location
     *
     * @param location The location
     * @return True if the FinePosition matches the location
     */
    public boolean matches(Location location) {
        return location.getWorld().getName().equals(this.world)
               && location.getBlockX() == this.x
               && location.getBlockY() == this.y
               && location.getBlockZ() == this.z;
    }

    /**
     * Check if the FinePosition matches a Block
     *
     * @param block The block
     * @return True if the FinePosition matches the block
     */
    public boolean matches(Block block) {
        return block.getWorld().getName().equals(this.world)
               && block.getX() == this.x
               && block.getY() == this.y
               && block.getZ() == this.z;
    }

    /**
     * Check if the FinePosition matches to another FinePosition
     *
     * @param finePosition The FinePosition
     * @return True if the FinePosition matches the chunk
     */
    public boolean matches(FinePosition finePosition) {
        return finePosition.world.equals(this.world)
               && finePosition.x == this.x
               && finePosition.y == this.y
               && finePosition.z == this.z;
    }

    /**
     * Get the chunk of the FinePosition
     *
     * @return The chunk
     */
    public Chunk getChunk() {
        World world = Bukkit.getWorld(this.world);
        int chunkX = this.x >> 4;
        int chunkZ = this.z >> 4;

        if (world == null) return null;

        return world.getChunkAt(chunkX, chunkZ);
    }

    /**
     * Get the world of the FinePosition
     *
     * @return The world
     */
    public World getWorld() {
        return Bukkit.getWorld(this.world);
    }

}
