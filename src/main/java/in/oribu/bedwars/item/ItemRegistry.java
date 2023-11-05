package in.oribu.bedwars.item;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry for all different types of custom items
 */
public final class ItemRegistry {

    private static final Map<String, CustomItem> items = new HashMap<>();

    static {
        register(new BridgeEggItem());
    }
    
    /**
     * Register a custom item into the registry
     *
     * @param item The custom item
     */
    public static void register(@NotNull CustomItem item) {
        items.put(item.name, item);
    }

    /**
     * Get a custom item from the registry
     *
     * @param name The name of the custom item
     * @return The custom item
     */
    @Nullable
    public static CustomItem get(String name) {
        return items.get(name);
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
     * This class should not be instantiated
     */
    public ItemRegistry() {
        throw new UnsupportedOperationException();
    }

}
