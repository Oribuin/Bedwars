package in.oribu.bedwars.match;

import in.oribu.bedwars.storage.Stats;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class MatchPlayer {

    private final @NotNull UUID uuid; // The player's UUID
    private final @NotNull String name; // The player's name
    private final @NotNull String team; // The player's team
    private boolean dead; // If the player is deadge
    private Stats stats; // The player's stats

    /**
     * The constructor for the match player
     *
     * @param uuid The player's UUID
     * @param team The player's team
     */
    public MatchPlayer(@NotNull UUID uuid, @NotNull String team) {
        this.uuid = uuid;
        this.name = this.getPlayer().getName();
        this.team = team;
        this.dead = false;
        this.stats = new Stats();
    }

    /**
     * The constructor for the match player
     *
     * @param player The player
     * @param team   The player's team
     */
    public MatchPlayer(@NotNull Player player, @NotNull String team) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.team = team;
        this.dead = false;
    }

    /**
     * Eliminate the user from the match.
     *
     * @param match The match to eliminate the player from
     */
    public void eliminate(@NotNull Match match) {
        this.dead = true;
        this.stats.setDeaths(this.stats.getDeaths() + 1);

        // TODO: Check how many members of the team are alive
        // TODO: Play elimination sound, message and finisher
        // TODO: Drop ender chest contents into team generator.

        Player player = this.getPlayer();

        // Make the player a spectator
        player.setGameMode(GameMode.CREATIVE);
        player.setAllowFlight(true);
        player.getInventory().clear();
        player.setInvisible(true);
//        if (NMSUtil.isPaper()) {
//            player.teleportAsync(match.getMap().getCenter());
//        } else {
        player.teleport(match.getMap().getCenter());
//        }

        // TODO: Hide the player from other players
    }

    /**
     * Get the team the player is on
     *
     * @param match The match the player is in
     * @return The team the player is on
     */
    @Nullable
    public Team getTeam(@NotNull Match match) {
        return match.getTeams().get(this.team);
    }

    /**
     * Get the player's name from the UUID
     *
     * @return The player's name
     */
    @NotNull
    public Player getPlayer() {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            throw new IllegalArgumentException("Player with UUID " + uuid + " is not online");
        }

        return player;
    }

    @NotNull
    public UUID getUUID() {
        return this.uuid;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @NotNull String getTeam() {
        return this.team;
    }

    public boolean isDead() {
        return this.dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public Stats getStats() {
        return this.stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

}
