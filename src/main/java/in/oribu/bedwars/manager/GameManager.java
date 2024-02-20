package in.oribu.bedwars.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.manager.Manager;
import in.oribu.bedwars.match.Level;
import in.oribu.bedwars.match.Match;
import in.oribu.bedwars.match.MatchStatus;
import in.oribu.bedwars.match.generator.Generator;
import in.oribu.bedwars.util.BedwarsUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class GameManager extends Manager {

    private Map<String, Level> levels = new HashMap<>(); // The levels that are loaded
    private File levelFolder; // The folder where the levels are stored

    private @Nullable Match activeMatch;
    private BukkitTask countdownTask; // The task that makes sure theres enough players to start the match

    public GameManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {

    }

    /**
     * Create a new match from the level and load it
     *
     * @param level The level to create the match from
     */
    public void createNewMatch(Level level) {
        if (this.activeMatch != null) {
            Bukkit.getLogger().severe("Tried to create a new match while another match is active!");
            return;
        }

        // Wait for the level to load before creating the match and setting it as the active match
        level.load();

        this.activeMatch = new Match(level);
        this.activeMatch.setStatus(MatchStatus.WAITING);

        // Tell all the players that they can join the match when its ready
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Component.text("A new match has been created! Type /bw join to join the match!")));
    }

    @Override
    public void disable() {
        if (this.countdownTask != null) {
            this.countdownTask.cancel();
            this.countdownTask = null;
        }
    }

    @Nullable
    public Match getActiveMatch() {
        return this.activeMatch;
    }

    /**
     * Load the level from a file in the /Bedwars/levels folder
     *
     * @param file The file
     * @return The loaded level
     */
    public Level loadLevel(File file) {
        CommentedFileConfiguration config = CommentedFileConfiguration.loadConfiguration(file);

        String name = config.getString("name");
        if (name == null) {
            this.rosePlugin.getLogger().warning("Unable to find the name of the level " + file.getName() + ".");
            return null;
        }

        int centerX = config.getInt("center.x");
        int centerY = config.getInt("center.y");
        int centerZ = config.getInt("center.z");
        float centerYaw = (float) config.getDouble("center.yaw");
        float centerPitch = (float) config.getDouble("center.pitch");
        String worldName = config.getString("world");
        World world = Bukkit.getWorld(worldName == null ? "unknown-world-1" : worldName);

        if (world == null) {
            this.rosePlugin.getLogger().warning("Unable to find the world of the level " + file.getName() + ".");
            return null;
        }

        Location center = new Location(world, centerX, centerY, centerZ, centerYaw, centerPitch);
        Level level = new Level(name, center, file);

        level.setIslandRadius(config.getInt("island-radius", 25));
        level.setMaxTeams(config.getInt("max-teams", 8));
        level.setPlayersPerTeam(config.getInt("players-per-team", 1));

        // Load Level Generators
        CommentedConfigurationSection generatorsSection = config.getConfigurationSection("generators");
        if (generatorsSection != null) {
            for (String key : generatorsSection.getKeys(false)) {

                // Load all the materials and their amounts
                Map<Material, Integer> materials = new HashMap<>();
                CommentedConfigurationSection materialsSection = generatorsSection.getConfigurationSection(key + ".materials");
                if (materialsSection != null) {
                    materialsSection.getKeys(false).forEach(s -> {
                        Material material = Material.matchMaterial(s.toUpperCase(), false);
                        if (material == null) return;

                        materials.put(material, materialsSection.getInt(s));
                    });
                }

                // Center Generator Location
                Location generatorLocation = new Location(world,
                        generatorsSection.getInt(key + ".center.x"),
                        generatorsSection.getInt(key + ".center.y"),
                        generatorsSection.getInt(key + ".center.z")
                );

                // Load the cooldown of the generator
                String cooldown = generatorsSection.getString(key + ".cooldown");
                String hologramIcon = generatorsSection.getString(key + ".hologram-icon", "unknown");
                long cooldownLong = BedwarsUtil.getTimeFromString(cooldown);

                Generator generator = new Generator(materials, generatorLocation);
                generator.setMaxAmount(generatorsSection.getInt(key + ".max-drops"));
                generator.setShareDrops(generatorsSection.getBoolean(key + ".share-drops"));
                generator.setHologramIcon(Material.matchMaterial(hologramIcon, false));
                generator.setRadius(generatorsSection.getInt(key + ".radius"));
                generator.setCooldown(cooldownLong <= 0 ? Duration.ofSeconds(30).toMillis() : cooldownLong);
                level.getGenerators().add(generator);
            }
        }


        return level;
    }
}
