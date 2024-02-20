package in.oribu.bedwars.match.generator;

import in.oribu.bedwars.storage.DataKeys;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Resource generator for items such as iron, gold, and diamonds
 */
public class Generator {

    private final Map<Material, Integer> materials; // The material to drop
    private final Location center; // The location of the generator
    private long cooldown; // The cooldown between drops (in millis)
    private long lastDrop; // The last time the generator dropped an item
    private int maxAmount; // The maximum amount of items that can be inside a generator
    private int radius; // The radius of the generator (Default: 3x3)
    private boolean shareDrops; // Should the item drops be shared amongst players.

    /**
     * Represents a generator for a resource
     *
     * @param materials The materials to drop
     * @param center    The location of the generator
     * @param cooldown  The cooldown between drops (in millis)
     */
    public Generator(Map<Material, Integer> materials, Location center, int cooldown) {
        this.materials = materials;
        this.center = center;
        this.cooldown = cooldown;
        this.lastDrop = 0;
        this.maxAmount = 48;
        this.radius = 3;
        this.shareDrops = false;
    }

    /**
     * Create all the display functions for the generator (if needed)
     */
    public void create() {
        // TODO: Create a hologram with timers
        // TODO: Create spinny cube :)
        // TODO: Entity creation has to be done synchronously
    }

    /**
     * Drop an item from the generator
     *
     * @param itemStack The item to drop
     */
    public void dropItem(ItemStack itemStack) {
        World world = this.center.getWorld();
        if (world == null) return;

        world.dropItem(this.center.clone(), itemStack.clone(), item -> {
            item.setUnlimitedLifetime(true);
            item.getPersistentDataContainer().set(
                    DataKeys.GENERATOR_ITEM,
                    PersistentDataType.INTEGER,
                    1
            );
        });
    }

    /**
     * Called when the generator is ticked
     */
    public void tick() {
        // Make sure we're not trying to spawn entities asynchronously
        if (System.currentTimeMillis() - this.lastDrop < this.cooldown)
            return;

        this.lastDrop = System.currentTimeMillis();

        if (this.getItemsInside() >= this.maxAmount)
            return;

        World world = this.center.getWorld();
        List<ItemStack> toGive = this.materials.entrySet()
                .stream()
                .map(entry -> new ItemStack(entry.getKey(), entry.getValue()))
                .toList();

        if (world == null) return;

        // Share the itemstack with all the players inside the generator if enabled.
        if (shareDrops) {
            List<Player> inside = this.getPlayersInside();
            if (inside.size() > 1) {
                inside.forEach(player -> player.getInventory().addItem(toGive.toArray(ItemStack[]::new)));
                return;
            }
        }

        // Create the item on the ground.
        toGive.forEach(this::dropItem);
    }

    /**
     * Get the time until the next generator spawn.
     *
     * @return The time
     */
    public long getTimeUntilSpawn() {
        return this.cooldown - (System.currentTimeMillis() - this.lastDrop);
    }

    /**
     * Gets the materials inside the current generator.
     *
     * @return The amount of materials inside the generator.
     */
    public int getItemsInside() {
        World world = this.center.getWorld();
        if (world == null) return 0;

        return world.getNearbyEntities(this.center, this.radius, this.radius, this.radius).stream()
                .filter(entity -> entity.getType() == EntityType.DROPPED_ITEM)
                .map(entity -> ((Item) entity).getItemStack())
                .filter(item -> this.materials.containsKey(item.getType()))
                .mapToInt(ItemStack::getAmount)
                .sum();

    }

    /**
     * Get all the players inside the generator.
     *
     * @return The players inside
     */
    public List<Player> getPlayersInside() {
        World world = this.center.getWorld();
        if (world == null) return new ArrayList<>();

        return world.getNearbyEntities(this.center, this.radius, this.radius, this.radius).stream()
                .filter(entity -> entity.getType() == EntityType.PLAYER)
                .map(entity -> (Player) entity)
                .collect(Collectors.toList());
    }

    /**
     * Upgrade the generator level
     *
     * @param material The material to upgrade
     */
    public void upgrade(Material material) {
        int level = this.materials.getOrDefault(material, 0);

        this.materials.put(material, level + 1);
    }

    public Map<Material, Integer> getMaterials() {
        return this.materials;
    }

    public Location getCenter() {
        return this.center;
    }

    public long getCooldown() {
        return this.cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public long getLastDrop() {
        return this.lastDrop;
    }

    public void setLastDrop(long lastDrop) {
        this.lastDrop = lastDrop;
    }

    public int getMaxAmount() {
        return this.maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public boolean isShareDrops() {
        return shareDrops;
    }

    public void setShareDrops(boolean shareDrops) {
        this.shareDrops = shareDrops;
    }

}

