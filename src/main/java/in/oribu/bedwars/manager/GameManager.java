package in.oribu.bedwars.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.manager.Manager;
import in.oribu.bedwars.match.Level;
import in.oribu.bedwars.match.Match;
import in.oribu.bedwars.match.MatchStatus;
import in.oribu.bedwars.match.Team;
import in.oribu.bedwars.match.generator.Generator;
import in.oribu.bedwars.storage.DataKeys;
import in.oribu.bedwars.util.BedwarsUtil;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameManager extends Manager {

    private final Map<String, Level> levels = new HashMap<>(); // The levels that are loaded
    private @Nullable Match activeMatch;
    private BukkitTask countdownTask; // The task that makes sure theres enough players to start the match

    public GameManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {
        this.levels.clear();
        this.rosePlugin.getLogger().info("Loading all levels from the Bedwars/levels folder.");

        // Create the folder
        File folder = new File(this.rosePlugin.getDataFolder(), "levels");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Create the file
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            this.rosePlugin.getLogger().info("No levels found in the Bedwars/levels folder.");
            return;
        }

        Arrays.stream(files)
                .filter(file -> file.getName().endsWith(".yml"))
                .forEach(file -> {
                    Level level = this.loadLevel(file);
                    if (level != null) {
                        this.levels.put(level.getName(), level);
                    }
                });

        // Check and remove any entities that are not supposed to be in the world
        this.checkForEntities();
    }

    /**
     * Create a new match from the level and load it
     *
     * @param level The level to create the match from
     */
    public Match createNewMatch(Level level) {
        if (this.activeMatch != null) {
            Bukkit.getLogger().severe("Tried to create a new match while another match is active!");
            return null;
        }

        // Wait for the level to load before creating the match and setting it as the active match
        level.load();

        this.activeMatch = new Match(level.getName());
        this.activeMatch.setStatus(MatchStatus.WAITING);

        // Tell all the players that they can join the match when its ready
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Component.text("A new match has been created! Type /bw join to join the match!")));
        return this.activeMatch;
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
     * Cancel the active match and set it to null
     */
    public void remove() {
        if (this.activeMatch == null) return;

        this.activeMatch = null;
    }

    /**
     * Cache a level into the manager
     *
     * @param level The level to cache
     */
    public void cache(Level level) {
        this.levels.put(level.getName().toLowerCase(), level);
    }

    /**
     * Load the level from a file in the /Bedwars/levels folder
     *
     * @param file The file
     *
     * @return The loaded level
     */
    @SuppressWarnings("deprecation")
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

        // Load Level Teams
        CommentedConfigurationSection teamSettingsSection = config.getConfigurationSection("team-settings");
        if (teamSettingsSection != null) {
            level.setMaxTeams(config.getInt("team-settings.max-teams", 8));
            level.setPlayersPerTeam(teamSettingsSection.getInt("team-settings.players-per-team", 1));

            CommentedConfigurationSection teamsSection = config.getConfigurationSection("team-settings.teams");
            if (teamsSection != null) {
                for (String key : teamsSection.getKeys(false)) {
                    int teamNumber = Integer.parseInt(key);
                    String teamPath = "team-settings.teams." + teamNumber;
                    String teamName = teamsSection.getString(teamPath + ".name");

                    if (teamName == null) {
                        this.rosePlugin.getLogger().warning("Unable to find the name of the team " + teamNumber + " in the level " + file.getName() + ".");
                        return null;
                    }

                    // Load the bed positions
                    int bedX = teamsSection.getInt(teamPath + ".bed.x");
                    int bedY = teamsSection.getInt(teamPath + ".bed.y");
                    int bedZ = teamsSection.getInt(teamPath + ".bed.z");

                    // Load the spawn positions
                    int xSpawn = teamsSection.getInt(teamPath + ".spawn.x");
                    int ySpawn = teamsSection.getInt(teamPath + ".spawn.y");
                    int zSpawn = teamsSection.getInt(teamPath + ".spawn.z");
                    float yawSpawn = (float) teamsSection.getDouble(teamPath + ".spawn.yaw");
                    float pitchSpawn = (float) teamsSection.getDouble(teamPath + ".spawn.pitch");
                    Location spawn = new Location(world, xSpawn, ySpawn, zSpawn, yawSpawn, pitchSpawn);

                    // Load Team Color
                    ChatColor color = ChatColor.valueOf(teamsSection.getString(teamPath + ".color", "WHITE").toUpperCase());

                    // Load the team generator location
                    Team team = new Team(teamName, spawn, level.getGenerators().get(teamNumber), color);
                    team.setBed(new Location(world, bedX, bedY, bedZ));
                    level.getTeams().put(teamName, team);
                }
            } else {
                this.rosePlugin.getLogger().warning("Unable to find the teams of the level " + file.getName() + ".");
            }


        }

        return level;
    }

    /**
     * Check the world for any entities that are not supposed to be there
     */
    public void checkForEntities() {
        List<World> worlds = this.levels.values().stream()
                .map(level -> level.getCenter().getWorld())
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // performance :-)
        worlds.forEach(world -> world.getEntities().forEach(entity -> {
            PersistentDataContainer container = entity.getPersistentDataContainer();
            if (this.hasAny(container, DataKeys.CUSTOM_ENTITY, DataKeys.CUSTOM_PROJECTILE, DataKeys.CUSTOM_PROJECTILE)) {
                entity.remove();
            }
        }));
    }

    /**
     * Check if a PersistentDataContainer has any of the following keys in it
     *
     * @param container The container to check
     * @param keys      The keys to check for
     *
     * @return True if the container has any of the keys
     */
    private boolean hasAny(PersistentDataContainer container, NamespacedKey... keys) {
        return Arrays.stream(keys).anyMatch(container::has);
    }

    public Map<String, Level> getLevels() {
        return this.levels;
    }

}
