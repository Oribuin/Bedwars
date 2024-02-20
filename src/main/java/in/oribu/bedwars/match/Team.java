package in.oribu.bedwars.match;

import in.oribu.bedwars.match.generator.Generator;
import in.oribu.bedwars.upgrade.UpgradeType;
import in.oribu.bedwars.util.BedwarsUtil;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Team {

    private final @NotNull String name;
    private final @NotNull Location spawn;
    private final @NotNull Location bed;
    private final @NotNull ChatColor teamColor;
    private @NotNull Generator generator;
    private @NotNull Map<UUID, MatchPlayer> players;
    private final @NotNull Map<UpgradeType, Integer> upgrades;
    private boolean hasBed;
    private boolean eliminated;
    private int maxPlayers;
    private @Nullable org.bukkit.scoreboard.Team scoreboardTeam;

    /**
     * The constructor for the team
     *
     * @param name      The name of the team
     * @param spawn     The spawn location of the team
     * @param generator The generator of the team
     * @param teamColor The color of the team
     */
    public Team(@NotNull String name, @NotNull Location spawn, @NotNull Generator generator, @NotNull ChatColor teamColor) {
        this.name = name;
        this.spawn = spawn;
        this.bed = spawn;
        this.generator = generator;
        this.teamColor = teamColor;
        this.players = new HashMap<>();
        this.upgrades = new HashMap<>();
        this.hasBed = true;
        this.eliminated = false;
        this.maxPlayers = 1;
        this.scoreboardTeam = null;
    }

    /**
     * Tick all the island upgrades and such
     */
    public void tick(Match match) {
        this.upgrades.forEach((type, level) -> {
            if (level == 0) return;

            type.getUpgrade().tick(match, this, level);
        });

        this.generator.tick();
    }

    /**
     * Add a player into the team
     *
     * @param player The player to join the team
     */
    public void join(Player player) {
        if (this.players.size() >= this.maxPlayers) {
            Bukkit.getLogger().warning("Player " + player.getName() + " tried to join team " + this.name + " but it was full!");
            return;
        }

        // TODO: Check if the player is already in a team
        this.players.put(player.getUniqueId(), new MatchPlayer(player, this.name));

        // Add the player to the scoreboard team
        if (this.scoreboardTeam == null) return;

        if (this.scoreboardTeam.hasEntry(player.getName())) {
            this.scoreboardTeam.addEntry(player.getName());
        }

        player.sendMessage("You have joined team " + this.name + "!");
    }

    /**
     * Upgrade a team to the upgrade type
     *
     * @param type The type of upgrade
     */
    public void upgrade(Match match, UpgradeType type) {
        // TODO: Add check to see if the team can upgrade
        // TODO: Sent message to the team that they have upgraded

        int newLevel = this.upgrades.getOrDefault(type, 0) + 1;
        this.upgrades.put(type, newLevel);

        type.getUpgrade().equip(match, this, newLevel);
        this.getPlayers().values().stream().map(MatchPlayer::getPlayer)
                .forEach(player -> player.sendMessage("Your team has upgraded " + type.name() + " to level " + newLevel + "!"));
    }

    /**
     * Get the amount of players alive on the team
     *
     * @return The amount
     */
    public int getAlive() {
        return (int) this.players.values()
                .stream()
                .filter(player -> !player.isDead())
                .count();
    }

    /**
     * Setup the scoreboard team with all their values.
     *
     * @param scoreboardTeam The scoreboard team to setup
     */
    public void setupScoreboardTeam(@NotNull org.bukkit.scoreboard.Team scoreboardTeam) {

        if (this.scoreboardTeam == null) {
            scoreboardTeam.setAllowFriendlyFire(false);
            scoreboardTeam.setCanSeeFriendlyInvisibles(true);
            scoreboardTeam.color(BedwarsUtil.getNamedTextColor(this.teamColor));
            scoreboardTeam.displayName(Component.text(this.name));
            scoreboardTeam.setOption(
                    org.bukkit.scoreboard.Team.Option.COLLISION_RULE,
                    org.bukkit.scoreboard.Team.OptionStatus.NEVER
            );
        }

        this.scoreboardTeam = scoreboardTeam;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public Location getSpawn() {
        return this.spawn;
    }

    @NotNull
    public Generator getGenerator() {
        return this.generator;
    }

    public void setGenerator(@NotNull Generator generator) {
        this.generator = generator;
    }

    @NotNull
    public ChatColor getTeamColor() {
        return this.teamColor;
    }

    @NotNull
    public Map<UUID, MatchPlayer> getPlayers() {
        return this.players;
    }

    public void setPlayers(@NotNull Map<UUID, MatchPlayer> players) {
        this.players = players;
    }

    @NotNull
    public Map<UpgradeType, Integer> getUpgrades() {
        return this.upgrades;
    }

    public boolean isHasBed() {
        return this.hasBed;
    }

    public void setHasBed(boolean hasBed) {
        this.hasBed = hasBed;
    }

    public boolean isEliminated() {
        return this.eliminated;
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @Nullable
    public org.bukkit.scoreboard.Team getScoreboardTeam() {
        return scoreboardTeam;
    }

    public void setScoreboardTeam(@Nullable org.bukkit.scoreboard.Team scoreboardTeam) {
        this.scoreboardTeam = scoreboardTeam;
    }

}
