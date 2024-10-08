package in.oribu.bedwars.storage;

import in.oribu.bedwars.BedwarsPlugin;
import org.bukkit.NamespacedKey;

public final class DataKeys {

    public static final NamespacedKey CUSTOM_ENTITY = new NamespacedKey(BedwarsPlugin.get(), "custom_entity");
    public static final NamespacedKey CUSTOM_ITEM = new NamespacedKey(BedwarsPlugin.get(), "custom_item");

    // Unique
    public static final NamespacedKey GENERATOR_ITEM = new NamespacedKey(BedwarsPlugin.get(), "generator_item");
    public static final NamespacedKey CUSTOM_PROJECTILE = new NamespacedKey(BedwarsPlugin.get(), "custom_projectile");
    public static final NamespacedKey GENERATOR_DISPLAY = new NamespacedKey(BedwarsPlugin.get(), "generator_display");

}
