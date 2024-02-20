package in.oribu.bedwars.util;

import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import in.oribu.bedwars.BedwarsPlugin;
import in.oribu.bedwars.manager.LocaleManager;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class BedwarsUtil {

    public BedwarsUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    /**
     * Deserialize an ItemStack from a CommentedConfigurationSection with placeholders
     *
     * @param section      The section to deserialize from
     * @param sender       The CommandSender to apply placeholders from
     * @param key          The key to deserialize from
     * @param placeholders The placeholders to apply
     * @return The deserialized ItemStack
     */
    @Nullable
    public static ItemStack deserialize(
            @NotNull CommentedConfigurationSection section,
            @Nullable CommandSender sender,
            @NotNull String key,
            @NotNull StringPlaceholders placeholders
    ) {
        LocaleManager locale = BedwarsPlugin.get().getManager(LocaleManager.class);
        Material material = Material.getMaterial(section.getString(key + ".material", ""));
        if (material == null) return null;


        // Load enchantments
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        CommentedConfigurationSection enchantmentSection = section.getConfigurationSection(key + ".enchantments");
        if (enchantmentSection != null) {
            for (String enchantmentKey : enchantmentSection.getKeys(false)) {
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentKey.toLowerCase()));
                if (enchantment == null) continue;

                enchantments.put(enchantment, enchantmentSection.getInt(enchantmentKey, 1));
            }
        }

        // Load potion item flags
        ItemFlag[] flags = section.getStringList(key + ".flags").stream()
                .map(ItemFlag::valueOf)
                .toArray(ItemFlag[]::new);

        return new ItemBuilder(material)
                .name(locale.format(sender, section.getString(key + ".name"), placeholders))
                .amount(Math.max(1, Math.min(material.getMaxStackSize(), section.getInt(key + ".amount", 1))))
                .lore(section.getStringList(key + ".lore"))
                .glow(section.getBoolean(key + ".glowing", false))
                .unbreakable(section.getBoolean(key + ".unbreakable", false))
                .model(section.getInt(key + ".model-data", -1))
                .enchant(enchantments)
                .flags(flags)
                .texture(locale.format(sender, section.getString(key + ".texture"), placeholders))
                .build();
    }

    /**
     * Deserialize an ItemStack from a CommentedConfigurationSection
     *
     * @param section The section to deserialize from
     * @param key     The key to deserialize from
     * @return The deserialized ItemStack
     */
    @Nullable
    public static ItemStack deserialize(@NotNull CommentedConfigurationSection section, @NotNull String key) {
        return deserialize(section, null, key, StringPlaceholders.empty());
    }

    /**
     * Deserialize an ItemStack from a CommentedConfigurationSection with placeholders
     *
     * @param section The section to deserialize from
     * @param sender  The CommandSender to apply placeholders from
     * @param key     The key to deserialize from
     * @return The deserialized ItemStack
     */
    @Nullable
    public static ItemStack deserialize(@NotNull CommentedConfigurationSection section, @Nullable CommandSender sender, @NotNull String key) {
        return deserialize(section, sender, key, StringPlaceholders.empty());
    }

    /**
     * Convert a ChatColor to a DyeColor if possible
     *
     * @param color The ChatColor to convert
     * @return The DyeColor
     */
    @SuppressWarnings("deprecation")
    public static DyeColor getDyeColor(ChatColor color) {
        return switch (color.getName().toLowerCase()) {
            case "black" -> DyeColor.BLACK;
            case "dark_blue", "blue" -> DyeColor.BLUE;
            case "dark_green" -> DyeColor.GREEN;
            case "dark_aqua" -> DyeColor.CYAN;
            case "dark_red", "red" -> DyeColor.RED;
            case "dark_purple" -> DyeColor.PURPLE;
            case "gold" -> DyeColor.ORANGE;
            case "gray" -> DyeColor.GRAY;
            case "dark_gray" -> DyeColor.LIGHT_GRAY;
            case "green" -> DyeColor.LIME;
            case "aqua" -> DyeColor.LIGHT_BLUE;
            case "light_purple" -> DyeColor.MAGENTA;
            case "yellow" -> DyeColor.YELLOW;
            default -> DyeColor.WHITE;
        };
    }

    /**
     * Convert a ChatColor to a NamedTextColor if possible
     *
     * @param color The ChatColor to convert
     * @return The NamedTextColor
     */
    @SuppressWarnings("deprecation")
    public static NamedTextColor getNamedTextColor(ChatColor color) {
        return switch (color.getName().toLowerCase()) {
            case "black" -> NamedTextColor.BLACK;
            case "dark_blue" -> NamedTextColor.DARK_BLUE;
            case "blue" -> NamedTextColor.BLUE;
            case "dark_green" -> NamedTextColor.DARK_GREEN;
            case "dark_aqua" -> NamedTextColor.DARK_AQUA;
            case "dark_red" -> NamedTextColor.DARK_RED;
            case "dark_purple" -> NamedTextColor.DARK_PURPLE;
            case "dark_gray" -> NamedTextColor.DARK_GRAY;
            case "red" -> NamedTextColor.RED;
            case "gold" -> NamedTextColor.GOLD;
            case "gray" -> NamedTextColor.GRAY;
            case "green" -> NamedTextColor.GREEN;
            case "aqua" -> NamedTextColor.AQUA;
            case "yellow" -> NamedTextColor.YELLOW;
            case "light_purple" -> NamedTextColor.LIGHT_PURPLE;
            default -> NamedTextColor.WHITE;
        };
    }

    /**
     * Format a time in milliseconds into a string
     *
     * @param time Time in milliseconds
     * @return Formatted time
     */
    public static String formatTime(long time) {
        long totalSeconds = time / 1000;

        if (totalSeconds <= 0)
            return "";

        long days = (int) Math.floor(totalSeconds / 86400.0);
        totalSeconds %= 86400;

        long hours = (int) Math.floor(totalSeconds / 3600.0);
        totalSeconds %= 3600;

        long minutes = (int) Math.floor(totalSeconds / 60.0);
        long seconds = (totalSeconds % 60);

        final StringBuilder builder = new StringBuilder();

        if (days > 0)
            builder.append(days).append("d, ");

        builder.append(hours).append("h, ");
        builder.append(minutes).append("m, ");
        builder.append(seconds).append("s");

        return builder.toString();
    }

    public static long getTimeFromString(String time) {
        String[] split = time.split(" ");
        long total = 0;

        try {
            for (String s : split) {
                String num = s.substring(0, s.length() - 1);

                long value = Long.parseLong(num);
                if (s.endsWith("d")) {
                    total += value * 86400000;
                } else if (s.endsWith("h")) {
                    total += value * 3600000;
                } else if (s.endsWith("m")) {
                    total += value * 60000;
                } else if (s.endsWith("s")) {
                    total += value * 1000;
                }
            }
        } catch (NumberFormatException ignored) {
        }

        return total;
    }

    /**
     * Get an enum from a string value
     *
     * @param enumClass The enum class
     * @param name      The name of the enum
     * @param <T>       The enum type
     * @return The enum
     */
    public static <T extends Enum<T>> T getEnum(Class<T> enumClass, String name) {
        if (name == null)
            return null;

        try {
            return Enum.valueOf(enumClass, name.toUpperCase());
        } catch (IllegalArgumentException ignored) {
        }

        return null;
    }

    /**
     * Get an enum from a string value
     *
     * @param enumClass The enum class
     * @param name      The name of the enum
     * @param <T>       The enum type
     * @return The enum
     */
    public static <T extends Enum<T>> T getEnum(Class<T> enumClass, String name, T def) {
        if (name == null)
            return def;

        try {
            return Enum.valueOf(enumClass, name.toUpperCase());
        } catch (IllegalArgumentException ignored) {
        }

        return def;
    }


}
