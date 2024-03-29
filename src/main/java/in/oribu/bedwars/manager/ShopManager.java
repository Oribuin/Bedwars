package in.oribu.bedwars.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.manager.Manager;
import in.oribu.bedwars.item.CustomItem;
import in.oribu.bedwars.item.ItemRegistry;
import in.oribu.bedwars.shop.Shop;
import in.oribu.bedwars.shop.ShopItem;
import in.oribu.bedwars.util.BedwarsUtil;
import in.oribu.bedwars.util.FileUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ShopManager extends Manager {

    private final Map<String, Shop> shops = new HashMap<>();

    public ShopManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {
        this.shops.clear();
        this.rosePlugin.getLogger().info("Loading all the shops from the /Bedwars/shops folder.");

        File folder = new File(this.rosePlugin.getDataFolder(), "shops");

        // Create the folder
        if (!folder.exists()) {
            folder.mkdirs();
            FileUtils.createFile(this.rosePlugin, "shops", "default.yml");
        }

        // Create the file
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            this.rosePlugin.getLogger().warning("Unable to find any shops in the /Bedwars/shops folder.");
            return;
        }

        // Load all the shops
        Arrays.stream(files)
                .filter(file -> file.getName().endsWith(".yml"))
                .forEach(this::loadShop);
    }

    /**
     * Load and cache a shop from a file in the /Bedwars/shops folder
     *
     * @param file The file
     */
    public void loadShop(File file) {
        CommentedFileConfiguration config = CommentedFileConfiguration.loadConfiguration(file);

        String shopName = config.getString("name");
        int size = config.getInt("size", 27);
        if (shopName == null) {
            this.rosePlugin.getLogger().warning("Unable to find the name of the shop " + file.getName() + ".");
            return;
        }

        Map<Integer, ShopItem> items = new HashMap<>();
        CommentedConfigurationSection itemsSection = config.getConfigurationSection("items");
        if (itemsSection == null || itemsSection.getKeys(false).isEmpty()) {
            this.rosePlugin.getLogger().warning("Unable to find any items in the shop " + file.getName() + ".");
            return;
        }

        // Load all the shop items.
        for (String key : itemsSection.getKeys(false)) {

            int slot;
            try {
                slot = Integer.parseInt(key);
            } catch (NumberFormatException ignored) {
                this.rosePlugin.getLogger().warning("Unable to parse the slot " + key + " in the shop " + file.getName() + ".");
                continue;
            }

            // Load the itemstack from the shop
            CustomItem item = ItemRegistry.get(itemsSection.getString(key + ".custom-item"));
            ItemStack result;
            if (item != null) {
                result = item.getItem(itemsSection, key);
            } else {
                result = BedwarsUtil.deserialize(itemsSection, key);
            }

            if (result == null) {
                String isCustom = itemsSection.getString(key + ".custom-item") == null ? "vanilla" : "custom";
                this.rosePlugin.getLogger().warning("Unable to parse the " + isCustom + " item " + file.getName() + ".");
                continue;
            }

            // Load the cost of the item
            CommentedConfigurationSection costSection = itemsSection.getConfigurationSection(key + ".cost");
            if (costSection == null) {
                this.rosePlugin.getLogger().warning("Unable to parse the cost of the item in the shop " + file.getName() + ".");
                continue;
            }

            Map<Material, Integer> cost = new HashMap<>();
            for (String costKey : costSection.getKeys(false)) {
                Material costMaterial = Material.getMaterial(costKey);
                int amount = costSection.getInt(costKey);

                if (costMaterial == null) {
                    continue;
                }

                cost.put(costMaterial, amount);
            }

            items.put(slot, new ShopItem(result, cost));
        }

        Shop shop = new Shop(shopName, items, size);
        this.shops.put(shopName, shop);
    }

    public Map<String, Shop> getShops() {
        return shops;
    }

    @Override
    public void disable() {
        this.shops.clear();
    }

}
