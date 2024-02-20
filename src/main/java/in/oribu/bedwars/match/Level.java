package in.oribu.bedwars.match;

import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import in.oribu.bedwars.match.generator.Generator;
import in.oribu.bedwars.storage.FinePosition;
import in.oribu.bedwars.util.BedwarsUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Level {

    private final @NotNull String name; // The name of the map
    private final @NotNull Location center; // The center of the map
    private final @NotNull List<Generator> generators; // The generators in the map
    private final @NotNull List<FinePosition> bedPositions; // The positions of the beds
    private CommentedFileConfiguration config; // The configuration of the map
    private File file; // The file of the map
    private int islandRadius; // The radius of the island
    private int maxTeams; // The maximum amount of teams
    private int playersPerTeam; // The maximum amount of players per team

    public Level(@NotNull String name, @NotNull Location center, @NotNull File file) {
        this.name = name;
        this.center = center;
        this.generators = new ArrayList<>();
        this.bedPositions = new ArrayList<>();
        this.islandRadius = 25;
        this.file = file;
        this.config = CommentedFileConfiguration.loadConfiguration(file);
        this.maxTeams = this.bedPositions.size();
    }

    /**
     * Load the map into the world
     */
    public void load() {
        this.generators.forEach(Generator::create);

        // TODO: Place the beds and other items
    }

    /**
     * Save all the data of the map to the file
     */
    public void save() {
        this.config.set("name", this.name);
        this.config.set("world", this.center.getWorld().getName());
        this.config.set("center.x", this.center.getX());
        this.config.set("center.y", this.center.getY());
        this.config.set("center.z", this.center.getZ());
        this.config.set("center.yaw", this.center.getYaw());
        this.config.set("center.pitch", this.center.getPitch());
        this.config.set("island-radius", this.islandRadius);

        CommentedConfigurationSection generatorsSection = this.config.getConfigurationSection("generators");
        if (generatorsSection == null) generatorsSection = this.config.createSection("generators");

        // Save all the generators to the config file
        for (int i = 0; i < this.generators.size(); i++) {
            Generator generator = this.generators.get(i);
            generatorsSection.set(i + ".center.x", generator.getCenter().getX());
            generatorsSection.set(i + ".center.y", generator.getCenter().getY());
            generatorsSection.set(i + ".center.z", generator.getCenter().getZ());
            generatorsSection.set(i + ".radius", generator.getRadius());
            generatorsSection.set(i + ".max-drops", generator.getMaxAmount());
            generatorsSection.set(i + ".share-drops", generator.isShareDrops());
            generatorsSection.set(i + ".cooldown", BedwarsUtil.formatTime(generator.getCooldown()));

            for (Map.Entry<Material, Integer> entry : generator.getMaterials().entrySet()) {
                generatorsSection.set(i + ".materials." + entry.getKey().name(), entry.getValue());
            }

            this.config.set("generators", generatorsSection);
        }

        // Save all the teams to the config file
        CommentedConfigurationSection teamsSection = this.config.getConfigurationSection("teams");
        if (teamsSection == null) teamsSection = this.config.createSection("teams");

    }

    public @NotNull String getName() {
        return this.name;
    }

    public @NotNull Location getCenter() {
        return this.center;
    }

    public @NotNull List<Generator> getGenerators() {
        return this.generators;
    }

    public List<FinePosition> getBedPositions() {
        return bedPositions;
    }

    public int getIslandRadius() {
        return this.islandRadius;
    }

    public void setIslandRadius(int islandRadius) {
        this.islandRadius = islandRadius;
    }

}
