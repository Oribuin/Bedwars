package in.oribu.bedwars.item;

import in.oribu.bedwars.storage.DataKeys;
import in.oribu.bedwars.upgrade.ContextHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class CustomItem {

    protected final String name;

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
     * Give the item to the player
     *
     * @param player The player to give the item to
     */
    public void give(Player player) {
        final ItemStack item = new ItemStack(Material.EGG);
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        final PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(DataKeys.CUSTOM_PROJECTILE, PersistentDataType.STRING, this.name);

        meta.setDisplayName(this.name);
        item.setItemMeta(meta);

        player.getInventory().addItem(item);
    }

    public String getName() {
        return name;
    }

}
