package in.oribu.bedwars.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.manager.Manager;
import in.oribu.bedwars.shop.Shop;
import in.oribu.bedwars.shop.ShopItem;
import in.oribu.bedwars.util.FileUtils;

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

        final File folder = new File(this.rosePlugin.getDataFolder(), "shops");

        // Create the folder
        if (!folder.exists()) {
            folder.mkdirs();
            FileUtils.createFile(this.rosePlugin, "shops", "default.yml");
        }

        // Create the file
        final File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            this.rosePlugin.getLogger().warning("Unable to find any shops in the /Bedwars/shops folder.");
            return;
        }

        Arrays.stream(files)
                .filter(file -> file.getName().endsWith(".yml"))
                .forEach(file -> {
                    final CommentedFileConfiguration config = CommentedFileConfiguration.loadConfiguration(file);

                    final String name = config.getString("name");
                    final int size = config.getInt("size");

                    final Map<Integer, ShopItem> items = new HashMap<>();
                    final CommentedConfigurationSection section = config.getConfigurationSection("items");
                    if (section == null || section.getKeys(false).isEmpty()) {
                        this.rosePlugin.getLogger().warning("Unable to find any items in the shop " + file.getName() + ".");
                        return;
                    }

                    // TODO: Load the shop items
                    // Load all the shop costs
//                    for (String key : section.getKeys(false)) {
//
//                        final String itemName = section.getString(key + ".name");
//                        final int itemAmount = section.getInt(key + ".amount", -1);
//
//                        if (itemName == null || itemAmount <= 0)
//                            continue;
//
//                        // TODO: Add custom itemstack loading
//                        // TODO: Add custom item loading
//
//                        int slot = 0;
//                        try {
//                            slot = Integer.parseInt(key);
//                        } catch (NumberFormatException ignored) {
//                            this.rosePlugin.getLogger().warning("Unable to parse the slot " + key + " in the shop " + file.getName() + ".");
//                            continue;
//                        }
//
//                    }

                });
    }

    @Override
    public void disable() {

    }

}
