package in.oribu.bedwars.item;

import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.utils.HexUtils;
import in.oribu.bedwars.storage.DataKeys;
import in.oribu.bedwars.upgrade.ContextHandler;
import in.oribu.bedwars.util.BedwarsUtil;
import in.oribu.bedwars.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class CustomItem {

    private final String name;
    private ItemStack item;

    public CustomItem(String name) {
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
        ItemStack item = BedwarsUtil.deserialize(section, key);
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(DataKeys.CUSTOM_ITEM, PersistentDataType.STRING, this.name);

        item.setItemMeta(meta);

        this.item = item;
        return this.item;
    }

    public ItemStack getItem() {
        if (this.item == null) {

            // Placeholder stuff until the shop is implemented
            this.item = new ItemBuilder(Material.DIAMOND)
                    .name(HexUtils.colorify("<r:0.5>" + this.name))
                    .build();

            ItemMeta meta = this.item.getItemMeta();
            if (meta == null) return this.item;

            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(DataKeys.CUSTOM_ITEM, PersistentDataType.STRING, this.name);

            this.item.setItemMeta(meta);
        }

        return item;
    }

    public String getName() {
        return name;
    }

}
