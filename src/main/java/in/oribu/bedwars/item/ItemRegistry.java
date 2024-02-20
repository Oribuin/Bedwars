package in.oribu.bedwars.item;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import in.oribu.bedwars.item.impl.BridgeEggItem;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Registry for all different types of custom items
 */
public final class ItemRegistry {

    private static final Map<String, CustomItem> items = new HashMap<>();
    private static final Table<UUID, String, Long> cooldowns = HashBasedTable.create();

    static {
        register(new BridgeEggItem());
    }

    /**
     * Register a custom item into the registry
     *
     * @param item The custom item
     */
    public static void register(@NotNull CustomItem item) {
        items.put(item.getName().toLowerCase(), item);
    }

    /**
     * Register a custom item into the registry
     *
     * @param name The name of the custom item
     * @param item The custom item
     */
    public static void register(@NotNull String name, @NotNull CustomItem item) {
        items.put(name.toLowerCase(), item);
    }

    /**
     * Get a custom item from the registry
     *
     * @param name The name of the custom item
     * @return The custom item
     */
    @Nullable
    public static CustomItem get(@Nullable String name) {
        return name == null ? null : items.get(name);
    }

    /**
     * Get all custom items from the registry
     *
     * @return The custom items
     */
    @NotNull
    public static Map<String, CustomItem> getItems() {
        return items;
    }

    /**
     * Get the cooldown of a custom item for a player
     *
     * @param player The player
     * @param item   The custom item
     * @return The cooldown of the custom item
     */
    public static boolean isOnCooldown(UUID player, CustomItem item) {
        if (item.getCooldown().isZero()) return false;

        long itemCooldown = item.getCooldown().toMillis();
        Long cooldown = cooldowns.get(player, item.getName());

        if (cooldown == null) return false;

        return System.currentTimeMillis() - cooldown < itemCooldown;
    }

    /**
     * Set the cooldown of a custom item for a player
     *
     * @param player The player
     * @param item   The custom item
     */
    public static void setCooldown(UUID player, CustomItem item) {
        if (item.getCooldown().isZero()) return;

        cooldowns.put(player, item.getName(), System.currentTimeMillis());
    }

    /**
     * Get the time left of a custom item for a player
     *
     * @param player The player
     * @param item   The custom item
     * @return The time left of the custom item
     */
    public long getTimeLeft(UUID player, CustomItem item) {
        if (item.getCooldown().isZero()) return 0;

        long itemCooldown = item.getCooldown().toMillis();
        Long cooldown = cooldowns.get(player, item.getName());

        if (cooldown == null) return 0;

        return itemCooldown - (System.currentTimeMillis() - cooldown);
    }

    /**
     * This class should not be instantiated
     */
    public ItemRegistry() {
        throw new UnsupportedOperationException();
    }

}
