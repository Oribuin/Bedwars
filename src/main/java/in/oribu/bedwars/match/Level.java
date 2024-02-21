package in.oribu.bedwars.match;

import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import in.oribu.bedwars.match.generator.Generator;
import in.oribu.bedwars.util.BedwarsUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Level {

    private final @NotNull String name; // The name of the map
    private final @NotNull Location center; // The center of the map
    private final @NotNull List<Generator> generators; // The generators in the map
    private final @NotNull Map<String, Team> teams; // The teams in the map
    private final CommentedFileConfiguration config; // The configuration of the map
    private final File file; // The file of the map
    private int islandRadius; // The radius of the island
    private int maxTeams; // The maximum amount of teams
    private int playersPerTeam; // The maximum amount of players per team

    public Level(@NotNull String name, @NotNull Location center, @NotNull File file) {
        this.name = name;
        this.center = center;
        this.generators = new ArrayList<>();
        this.teams = new HashMap<>();
        this.islandRadius = 25;
        this.file = file;
        this.config = CommentedFileConfiguration.loadConfiguration(file);
        this.maxTeams = 8;
        this.playersPerTeam = 1;
    }

    /**
     * Load the map into the world
     */
    public void load() {
        this.generators.forEach(Generator::create);

        // TODO: Place the beds and other items

    }

    /**
     * Save all the data of the map to the the configuration section
     */
    @SuppressWarnings("deprecation")
    public void save() {
        this.config.set("name", this.name);
        this.config.set("world", this.center.getWorld().getName());
        this.config.set("center.x", this.center.getX());
        this.config.set("center.y", this.center.getY());
        this.config.set("center.z", this.center.getZ());
        this.config.set("center.yaw", this.center.getYaw());
        this.config.set("center.pitch", this.center.getPitch());

        // Save all the generators to the config file
        for (int i = 0; i < this.generators.size(); i++) {
            Generator generator = this.generators.get(i);
            if (generator == null) continue;
            String genPath = "generators." + i;

            this.config.set(genPath + ".center.x", generator.getCenter().getX());
            this.config.set(genPath + ".center.y", generator.getCenter().getY());
            this.config.set(genPath + ".center.z", generator.getCenter().getZ());
            this.config.set(genPath + ".radius", generator.getRadius());
            this.config.set(genPath + ".max-drops", generator.getMaxAmount());
            this.config.set(genPath + ".share-drops", generator.isShareDrops());
            this.config.set(genPath + ".cooldown", BedwarsUtil.formatTime(generator.getCooldown()));
            this.config.set(genPath + ".hologram-icon", generator.getHologramIcon().name());

            for (Map.Entry<Material, Integer> entry : generator.getMaterials().entrySet()) {
                this.config.set(genPath + ".materials." + entry.getKey().name(), entry.getValue());
            }
        }

        // Save all the teams to the config file
        this.config.set("team-settings.max-teams", this.maxTeams);
        this.config.set("team-settings.players-per-team", this.playersPerTeam);

        for (Team team : this.teams.values()) {
            String teamPath = "team-settings.teams." + team.getName();

            // Set the spawn location of the team
            this.config.set(teamPath + ".spawn.world", team.getSpawn().getWorld().getName());
            this.config.set(teamPath + ".spawn.x", team.getSpawn().getX());
            this.config.set(teamPath + ".spawn.y", team.getSpawn().getY());
            this.config.set(teamPath + ".spawn.z", team.getSpawn().getZ());
            this.config.set(teamPath + ".spawn.yaw", team.getSpawn().getYaw());
            this.config.set(teamPath + ".spawn.pitch", team.getSpawn().getPitch());

            // Set the bed location of the team
            this.config.set(teamPath + ".bed.world", team.getBed().getWorld().getName());
            this.config.set(teamPath + ".bed.x", team.getBed().getX());
            this.config.set(teamPath + ".bed.y", team.getBed().getY());
            this.config.set(teamPath + ".bed.z", team.getBed().getZ());

            // Set the color of the team
            this.config.set(teamPath + ".color", team.getTeamColor().name());
        }

        // Save the config to the file
        this.config.save(this.file);
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

    public @NotNull Map<String, Team> getTeams() {
        return teams;
    }

    public int getIslandRadius() {
        return this.islandRadius;
    }

    public void setIslandRadius(int islandRadius) {
        this.islandRadius = islandRadius;
    }

    public int getMaxTeams() {
        return this.maxTeams;
    }

    public void setMaxTeams(int maxTeams) {
        this.maxTeams = maxTeams;
    }

    public int getPlayersPerTeam() {
        return this.playersPerTeam;
    }

    public void setPlayersPerTeam(int playersPerTeam) {
        this.playersPerTeam = playersPerTeam;
    }

}
