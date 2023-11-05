package in.oribu.bedwars.match;

import in.oribu.bedwars.BedwarsPlugin;
import in.oribu.bedwars.match.generator.Generator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Match {

    private final Level level; // The map of the match
    private final Map<String, Team> teams; // The teams in the match
    private long startTime; // The time the match started

    public Match(Level level) {
        this.level = level;
        this.teams = new HashMap<>();
        this.startTime = 0;
    }

    public void join(Player player) {
        // Get the team with the least players
        Team team = this.teams.values().stream()
                .filter(t -> t.getPlayers().size() < t.getMaxPlayers())
                .min(Comparator.comparingInt(t -> t.getPlayers().size()))
                .orElse(null);

        if (team == null) {
            player.sendMessage("There are no teams available to join!");
            return;
        }

        // Add the player to the team
        team.join(player);
    }

    /**
     * Load the match (called when it's decided to create a match)
     */
    public void load() {
        this.level.load();
    }

    /**
     * Start the match timer (called when the match starts)
     */
    public void start() {
        // Set the start time
        this.startTime = System.currentTimeMillis();

        Bukkit.getScheduler().runTaskTimer(BedwarsPlugin.get(), this::tick, 0, 1);
    }

    /**
     * Tick the match (called every tick)
     */
    public void tick() {
        // Tick the generators
        this.level.getGenerators().forEach(Generator::tick);
        this.teams.values().forEach(team -> team.tick(this));
    }

    /**
     * Get all the players in the match
     *
     * @return The players in the match
     */
    public List<MatchPlayer> getPlayers() {
        return this.teams.values().stream()
                .map(Team::getPlayers)
                .flatMap(map -> map.values().stream())
                .toList();
    }

    public Level getMap() {
        return level;
    }

    public Map<String, Team> getTeams() {
        return this.teams;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

}


