package in.oribu.bedwars.match;

import in.oribu.bedwars.BedwarsPlugin;
import in.oribu.bedwars.manager.GameManager;
import in.oribu.bedwars.match.generator.Generator;
import in.oribu.bedwars.storage.FinePosition;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Match {

    private final String level; // The map of the match
    private final Map<String, Team> teams; // The teams in the match
    private final Set<FinePosition> placedBlocks; // The blocks placed in the match
    private MatchStatus status; // The status of the match
    private long startTime; // The time the match started

    public Match(String level) {
        this.level = level;
        this.teams = new HashMap<>();
        this.placedBlocks = new HashSet<>();
        this.status = MatchStatus.WAITING;
        this.startTime = 0;
    }

    /**
     * Add a team to the match
     *
     * @param player The player to add
     */
    public void join(Player player) {
        if (this.status != MatchStatus.WAITING) {
            player.sendMessage("The match has already started!");
            return;
        }

        // Get the team with the least players
        Team team = this.teams.values().stream()
                .filter(t -> t.getPlayers().size() < this.getLevel().getPlayersPerTeam())
                .min(Comparator.comparingInt(t -> t.getPlayers().size()))
                .orElse(null);

        if (team == null) {
            player.sendMessage("There are no teams available to join!");
            return;
        }

        // Add the player to the team
        team.join(this, player);
    }

    /**
     * Start the match timer (called when the match starts)
     */
    public void start() {
        // Set the start time
        this.startTime = System.currentTimeMillis();
        this.status = MatchStatus.RUNNING;

        Bukkit.getScheduler().runTaskTimer(BedwarsPlugin.get(), this::tick, 0, 1);
    }

    /**
     * Tick the match (called every tick)
     */
    public void tick() {
        this.getLevel().getGenerators().forEach(Generator::tick);
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

    /**
     * Check if a block has been placed by a player already, If not, the block cannot be broken
     *
     * @param block The block
     * @return True if the block can be broken
     */
    public boolean canBeBroken(Block block) {
        return this.placedBlocks.stream().noneMatch(pos -> pos.matches(block));
    }

    public Level getLevel() {
        return BedwarsPlugin.get().getManager(GameManager.class).getLevels().get(this.level);
    }

    public Map<String, Team> getTeams() {
        return this.teams;
    }

    public Set<FinePosition> getPlacedBlocks() {
        return placedBlocks;
    }

    public void addPlacedBlock(FinePosition position) {
        this.placedBlocks.add(position);
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

}


