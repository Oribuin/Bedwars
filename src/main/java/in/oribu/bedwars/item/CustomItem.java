package in.oribu.bedwars.item;

import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import in.oribu.bedwars.storage.DataKeys;
import in.oribu.bedwars.upgrade.ContextHandler;
import in.oribu.bedwars.util.BedwarsUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class CustomItem {

    protected final String name;
    private ItemStack item;

    protected CustomItem(String name) {
        this.name = name;
    }

    /**
     * The event called with the item
     *
     * @param handler The context handler
     */
    public abstract void event(ContextHandler handler);

    /**
     * Create the custom item from a CommentedConfigurationSection
     *
     * @param section The section to create from
     * @param key     The key to create from
     * @return The created custom item
     */
    @Nullable
    public ItemStack getItem(@NotNull CommentedConfigurationSection section, @NotNull String key) {
        final ItemStack item = BedwarsUtil.deserialize(section, key);
        if (item == null) return null;
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        final PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(DataKeys.CUSTOM_ITEM, PersistentDataType.STRING, this.name);

        meta.setDisplayName(this.name);
        item.setItemMeta(meta);

        this.item = item;
        return this.item;
    }

    public ItemStack getItem() {
        if (this.item == null) {
            throw new IllegalStateException("Item has not been initialized yet");
        }

        return item;
    }

    public String getName() {
        return name;
    }

}
